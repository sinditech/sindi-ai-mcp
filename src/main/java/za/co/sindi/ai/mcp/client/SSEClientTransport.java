package za.co.sindi.ai.mcp.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

import za.co.sindi.ai.mcp.schema.JSONRPCMessage;
import za.co.sindi.ai.mcp.schema.MCPSchema;
import za.co.sindi.ai.mcp.shared.AbstractTransport;
import za.co.sindi.ai.mcp.shared.ClientTransport;
import za.co.sindi.ai.mcp.shared.TransportException;
import za.co.sindi.commons.net.sse.Event;
import za.co.sindi.commons.net.sse.EventHandler;
import za.co.sindi.commons.net.sse.MessageEvent;
import za.co.sindi.commons.net.sse.SSEEventSubscriber;

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
	
	private final String baseUrl;
	
	private String sseUrl;
	
	private HttpClient httpClient;
	
	private EventHandler eventSource;
	
	/**
	 * @param baseUrl
	 * @param mapper
	 */
	public SSEClientTransport(String baseUrl) {
		this(baseUrl, baseUrl + SSE_ENDPOINT);
	}

	/**
	 * @param baseUrl
	 * @param sseUrl
	 */
	public SSEClientTransport(final String baseUrl, final String sseUrl) {
		super();
		this.baseUrl = baseUrl;
		this.sseUrl = sseUrl;
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Transport#start()
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		if (!initialized.compareAndSet(false, true)) {
			throw new IllegalStateException("This stdio client transport has not been started.");
		}
		
		HttpClient.Builder httpClientBuilder = HttpClient.newBuilder().connectTimeout(getRequestTimeout());
		if (getExecutor() != null) httpClientBuilder.executor(getExecutor());
		
		httpClient = httpClientBuilder.build();
		
		eventSource = new EventHandler() {
		
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
					} else if (MESSAGE_EVENT_TYPE.equals(type)) {
						JSONRPCMessage responseMessage = MCPSchema.deserializeJSONRPCMessage(messageEvent.getData());
						getMessageHandler().onMessage(responseMessage);
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
			}
		};
		
		try {
			streamSSEMessages().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			LOGGER.log(Level.SEVERE, "SSE Streaming failed.", e);
			getMessageHandler().onError(e);
		}
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
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(this.baseUrl + endpoint))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(jsonText))
				.build();
		
		return httpClient.sendAsync(request, BodyHandlers.ofString()).thenAccept(response -> {
			if (response.statusCode() != 200 && response.statusCode() != 201 && response.statusCode() != 202
					&& response.statusCode() != 206) {
				String body = response.body();
				LOGGER.warning("Error sending message: " + response.statusCode() + ": " + body);
			}
		}).thenCompose(__ -> streamSSEMessages());
	}
	
	private CompletableFuture<Void> streamSSEMessages() {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(sseUrl))
				.header("Accept", "text/event-stream")
				.header("Cache-Control", "no-cache")
				.GET()
				.build();

		return httpClient.sendAsync(request, BodyHandlers.fromLineSubscriber(new SSEEventSubscriber(eventSource)))
					.thenAccept(response -> {
						int status = response.statusCode();
						if (status != 200 && status != 201 && status != 202 && status != 206) {
							throw new RuntimeException("Failed to connect to SSE stream. Unexpected status code: " + status);
						}
					}).exceptionally(throwable -> {
						getMessageHandler().onError(throwable);
						return null;
					});
	}
}
