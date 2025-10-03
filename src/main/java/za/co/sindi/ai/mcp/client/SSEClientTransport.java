package za.co.sindi.ai.mcp.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import za.co.sindi.ai.mcp.client.EventSource.ReadyState;
import za.co.sindi.ai.mcp.schema.JSONRPCMessage;
import za.co.sindi.ai.mcp.schema.MCPSchema;
import za.co.sindi.ai.mcp.schema.ProtocolVersion;
import za.co.sindi.ai.mcp.shared.AbstractTransport;
import za.co.sindi.ai.mcp.shared.ClientTransport;
import za.co.sindi.ai.mcp.shared.TransportException;
import za.co.sindi.commons.net.sse.Event;
import za.co.sindi.commons.net.sse.EventHandler;
import za.co.sindi.commons.net.sse.MessageEvent;
import za.co.sindi.commons.utils.Strings;

/**
 * @author Buhake Sindi
 * @since 20 March 2025
 */
public class SSEClientTransport extends AbstractTransport implements ClientTransport {
	
	/** SSE event type for JSON-RPC messages */
	private static final String MESSAGE_EVENT_TYPE = "message";

	/** SSE event type for endpoint discovery */
	private static final String ENDPOINT_EVENT_TYPE = "endpoint";
	
	/** SSE event type for error messages */
	private static final String ERROR_EVENT_TYPE = "error";
	
	/** Default SSE endpoint path */
	private static final String SSE_ENDPOINT = "/sse";
	
	private final AtomicBoolean initialized = new AtomicBoolean(false);
	
	private final AtomicReference<String> messageEndpoint = new AtomicReference<>();

	private final AtomicReference<CompletableFuture<Void>> connectionFuture = new AtomicReference<>();
	
	private final String baseUrl;
	
	private String sseUrl;
	
	private String accessToken;
	
	private HttpClient httpClient;
	
	private EventSource eventSource;
	
	private ProtocolVersion protocolVersion;
	
	/**
	 * @param baseUrl
	 */
	public SSEClientTransport(String baseUrl) {
		this(baseUrl, null);
	}
	
	/**
	 * @param baseUrl
	 * @param accessToken
	 */
	public SSEClientTransport(String baseUrl, String accessToken) {
		this(baseUrl, baseUrl + SSE_ENDPOINT, accessToken);
	}

	/**
	 * @param baseUrl
	 * @param sseUrl
	 * @param accessToken
	 */
	public SSEClientTransport(final String baseUrl, final String sseUrl, final String accessToken) {
		super();
		this.baseUrl = baseUrl;
		this.sseUrl = sseUrl;
		this.accessToken = accessToken;
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Transport#startAsync()
	 */
	@SuppressWarnings("resource")
	@Override
	public CompletableFuture<Void> startAsync() {
		// TODO Auto-generated method stub
		if (!initialized.compareAndSet(false, true)) {
			throw new IllegalStateException("This stdio client transport has not been started.");
		}
		
		HttpClient.Builder httpClientBuilder = HttpClient.newBuilder();
		if (getRequestTimeout() != null) httpClientBuilder.connectTimeout(getRequestTimeout());
		if (getExecutor() != null) httpClientBuilder.executor(getExecutor());
		
		httpClient = httpClientBuilder.build();
		
		CompletableFuture<Void> future = new CompletableFuture<>();
		connectionFuture.set(future);
		eventSource = new EventSource(sseUrl, httpClient, builder -> applyCommonHeaders(builder))
						.onMessage(new EventHandler() {
							
								/* (non-Javadoc)
								 * @see za.co.sindi.commons.net.sse.EventHandler#onEvent(za.co.sindi.commons.net.sse.Event)
								 */
								@Override
								public void onEvent(Event event) {
									// TODO Auto-generated method stub
									if (!initialized.get()) {
										return ;
									}
									
									if (event instanceof MessageEvent messageEvent) {
										var type = messageEvent.getType();
										if (ENDPOINT_EVENT_TYPE.equals(type)) {
											messageEndpoint.set(messageEvent.getData());
											future.complete(null);
										} else if (MESSAGE_EVENT_TYPE.equals(type)) {
											if (!Strings.isNullOrEmpty(messageEvent.getData())) {
												JSONRPCMessage responseMessage = MCPSchema.deserializeJSONRPCMessage(messageEvent.getData());
												getMessageHandler().onMessage(responseMessage);
											}
										} else if (ERROR_EVENT_TYPE.equals(type)) {
											RuntimeException error = new IllegalStateException("SSE error: " + messageEvent.getData());
											getMessageHandler().onError(error);
											throw error;
										} else {
											LOGGER.warning("Received unrecognized SSE event type: " + type);
										}
									}
								}
					
								/* (non-Javadoc)
								 * @see za.co.sindi.commons.net.sse.EventHandler#onError(java.lang.Throwable)
								 */
								@Override
								public void onError(Throwable error) {
									// TODO Auto-generated method stub
									getMessageHandler().onError(error);
									future.completeExceptionally(error);
								}
							});
		eventSource.connect();
		return future;
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Transport#sendAsync(za.co.sindi.ai.mcp.schema.JSONRPCMessage)
	 */
	@Override
	public CompletableFuture<Void> sendAsync(JSONRPCMessage message) {
		// TODO Auto-generated method stub
		if (!initialized.get()) {
			throw new TransportException("This transport was not started or is closed..");
		}
		
		String endpoint = messageEndpoint.get();
		if (endpoint == null) {
			throw new TransportException("No message endpoint available.");
		}
		
		String jsonText = MCPSchema.serializeJSONRPCMessage(message);   //getMapper().map(message);
		HttpRequest.Builder builder = HttpRequest.newBuilder()
				.uri(URI.create(this.baseUrl + endpoint))
				.header("ContentBlock-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(jsonText));
		applyCommonHeaders(builder);
		
		return httpClient.sendAsync(builder.build(), BodyHandlers.ofString()).thenAccept(response -> {
			if (response.statusCode() != 200 && response.statusCode() != 201 && response.statusCode() != 202
					&& response.statusCode() != 206) {
				String body = response.body();
				LOGGER.warning("Error sending message: " + response.statusCode() + ": " + body);
			}
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.AbstractTransport#close()
	 */
	
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		CompletableFuture<Void> future = connectionFuture.get();
		if (future != null && !future.isDone()) {
			future.cancel(true);
		}
		
		if (eventSource != null && eventSource.getReadyState() != ReadyState.CLOSED) {
			try {
				eventSource.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new IOException(e);
			}
		}
		
		if (httpClient != null) {
			httpClient.shutdown();
			httpClient.close();
		}
		super.close();
	}
	
	private void applyCommonHeaders(final HttpRequest.Builder builder) {
		if (!Strings.isNullOrEmpty(accessToken)) builder.header("Authorization", "Bearer " + accessToken);
		if (protocolVersion != null) builder.header("mcp-protocol-version", protocolVersion.toString());
	}

	@Override
	public void setProtocolVersion(ProtocolVersion protocolVersion) {
		// TODO Auto-generated method stub
		this.protocolVersion = protocolVersion;
	}
}
