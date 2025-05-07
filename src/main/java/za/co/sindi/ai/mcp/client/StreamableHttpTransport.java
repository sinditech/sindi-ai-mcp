/**
 * 
 */
package za.co.sindi.ai.mcp.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse.BodySubscribers;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import za.co.sindi.ai.mcp.mapper.JSONObjectMapper;
import za.co.sindi.ai.mcp.mapper.ObjectMapper;
import za.co.sindi.ai.mcp.schema.InitializedNotification;
import za.co.sindi.ai.mcp.schema.JSONRPCMessage;
import za.co.sindi.ai.mcp.schema.JSONRPCNotification;
import za.co.sindi.ai.mcp.schema.JSONRPCRequest;
import za.co.sindi.ai.mcp.schema.JSONRPCResponse;
import za.co.sindi.ai.mcp.schema.MCPSchema;
import za.co.sindi.ai.mcp.schema.RequestId;
import za.co.sindi.ai.mcp.shared.AbstractTransport;
import za.co.sindi.ai.mcp.shared.ClientTransport;
import za.co.sindi.ai.mcp.shared.TransportException;
import za.co.sindi.commons.net.sse.Event;
import za.co.sindi.commons.net.sse.EventHandler;
import za.co.sindi.commons.net.sse.MessageEvent;
import za.co.sindi.commons.net.sse.SSEEventSubscriber;
import za.co.sindi.commons.util.Either;
import za.co.sindi.commons.utils.Arrays;
import za.co.sindi.commons.utils.Strings;

/**
 * @author Buhake Sindi
 * @since 24 April 2025
 */
public class StreamableHttpTransport extends AbstractTransport implements ClientTransport {
	
	/** SSE event type for JSON-RPC messages */
	private static final String MESSAGE_EVENT_TYPE = "message";

	/** SSE event type for error messages */
	private static final String ERROR_EVENT_TYPE = "error";
	
	private static final ObjectMapper MAPPER = JSONObjectMapper.newInstance();
	
	private final AtomicBoolean initialized = new AtomicBoolean(false);
	
	private final AtomicReference<String> sessionId = new AtomicReference<>();

	private final String mcpUrl;
	
	private final String accessToken;
	
	private HttpClient httpClient;
	
	/**
	 * @param mcpUrl
	 * @param accessToken
	 */
	public StreamableHttpTransport(String mcpUrl, String accessToken) {
		super();
		this.mcpUrl = mcpUrl;
		this.accessToken = accessToken;
	}

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
		
		return CompletableFuture.completedFuture(null);
	}
	
	public CompletableFuture<Void> startSseAsync(final String resumptionToken, final RequestId replayMessageId, final Consumer<String> onResumptionTokenEvent) {
		
		HttpRequest.Builder builder = HttpRequest.newBuilder()
				.uri(URI.create(mcpUrl))
				.header("Accept", "application/json, text/event-stream")
				.header("Cache-Control", "no-cache, no-store")
				.GET();
		applyCommonHeaders(builder);
		
		if (!Strings.isNullOrEmpty(resumptionToken)) {
			builder.header("last-event-id", resumptionToken);
		}
		
		return httpClient.sendAsync(builder.build(), BodyHandlers.fromLineSubscriber(new SSEEventSubscriber(handleSseEvent(resumptionToken, replayMessageId, onResumptionTokenEvent))))
						 .thenAccept(response -> {
				        	int status = response.statusCode();
							if (status != 200 && status != 405) {
								throw new StreamableHTTPError("Failed to connect to SSE stream.", status);
							}
				        })
				        .exceptionally(ex -> {
				        	getMessageHandler().onError(ex);
				        	if (ex instanceof RuntimeException rex) throw rex;
				        	throw new IllegalStateException(ex);
				        });
	}
	
	private EventHandler handleSseEvent(final String resumptionToken, final RequestId replayMessageId, final Consumer<String> onResumptionTokenEvent) {
		final AtomicReference<RequestId> lastEventId = new AtomicReference<>();
		
		return new EventHandler() {
			
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
					if (Strings.isNullOrEmpty(messageEvent.getLastEventId())) {
						lastEventId.set(RequestId.of(messageEvent.getLastEventId()));
						if (onResumptionTokenEvent != null) {
							onResumptionTokenEvent.accept(messageEvent.getLastEventId());
						}
					}
					
					var type = messageEvent.getType();
					if (MESSAGE_EVENT_TYPE.equals(type)) {
						if (!Strings.isNullOrEmpty(messageEvent.getData())) {
							JSONRPCMessage responseMessage = MCPSchema.deserializeJSONRPCMessage(messageEvent.getData());
							if (replayMessageId != null && responseMessage instanceof JSONRPCResponse response) {
								response.setId(replayMessageId);
							}
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
			}
		};
	}

	@Override
	public CompletableFuture<Void> sendAsync(JSONRPCMessage message) {
		// TODO Auto-generated method stub
		return sendAsync(Arrays.toArray(message), null, null);
	}
	
	public CompletableFuture<Void> sendAsync(final JSONRPCMessage[] message, final String resumptionToken, final Consumer<String> onResumptionTokenEvent) {
		if (!initialized.get()) {
			throw new TransportException("This transport was not started or is closed..");
		}
		
		String jsonText = MAPPER.map(message);
		HttpRequest.Builder builder = HttpRequest.newBuilder()
				.uri(URI.create(mcpUrl))
				.header("Content-Type", "application/json")
				.header("Accept", "application/json, text/event-stream")
				.POST(HttpRequest.BodyPublishers.ofString(jsonText));
		applyCommonHeaders(builder);
		
		BodyHandler<Either<String, Void>> bodyHandler = (responseInfo) -> {
			if (responseInfo.statusCode() == 200) {
				// Get content type from response
	            String contentType = responseInfo.headers().firstValue("Content-Type").orElse("").toLowerCase();
	            if (contentType.contains("text/event-stream")) {
	            	return BodySubscribers.mapping(BodyHandlers.fromLineSubscriber(new SSEEventSubscriber(handleSseEvent(null, null, onResumptionTokenEvent))).apply(responseInfo), mapper -> Either.right(mapper));
	            }
	            
	            if (!contentType.contains("application/json")) {
	            	throw new StreamableHTTPError("Unexpected content type: " + contentType + ".", -1);
	            }
			}
			
			return BodySubscribers.mapping(BodyHandlers.ofString().apply(responseInfo), mapper -> Either.left(mapper));
		};
		
		return httpClient.sendAsync(builder.build(), bodyHandler).thenCompose(response -> {
			// Handle session ID received during initialization
			Optional<String> sessionId = response.headers().firstValue("mcp-session-id");
			if (sessionId.isPresent()) {
				this.sessionId.set(sessionId.get());
			}
			
			if (response.statusCode() != 200) {
				if (response.statusCode() == 202) {
					//Does the message has initialized notification.
					boolean containsInitializedNotification = java.util.Arrays.asList(message).stream().anyMatch(m -> {
						return (m instanceof JSONRPCNotification notification && notification.getMethod().equals(InitializedNotification.METHOD_NOTIFICATION_INITIALIZED));
					});
					
					if (containsInitializedNotification) {
						return startSseAsync(null, null, null);
					}
				}
				
				CompletableFuture<Void> future = new CompletableFuture<>();
                future.completeExceptionally(new IllegalStateException("Error sending message: " + response.statusCode() + ": " + response.body().getLeft()));
                return future;
			}
			
			// Get content type from response
            String contentType = response.headers().firstValue("Content-Type").orElse("").toLowerCase();
            boolean hasRequests = java.util.Arrays.asList(message).stream().anyMatch(m -> m instanceof JSONRPCRequest);
            
            if (hasRequests) {
            	if (contentType.contains("application/json")) {
            		JSONRPCMessage[] responseMessages = MCPSchema.deserializeJSONRPCMessages(response.body().getLeft());
            		for (JSONRPCMessage jsonRpcMessage : responseMessages) {
            			getMessageHandler().onMessage(jsonRpcMessage);
            		}
            		
            		return CompletableFuture.completedFuture(null);
            	}
            }
            
            return CompletableFuture.completedFuture(response.body().getRight());
		}).exceptionally(throwable -> {
			getMessageHandler().onError(throwable);
			return null;
		});
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		super.close();
	}
	
	private void applyCommonHeaders(final HttpRequest.Builder builder) {
		if (!Strings.isNullOrEmpty(accessToken)) builder.header("Authorization", "Bearer " + accessToken);
		String mcpSessionId = sessionId.get();
		if (!Strings.isNullOrEmpty(mcpSessionId)) builder.header("mcp-session-id", mcpSessionId);
	}

	@Override
	public String getSessionId() {
		// TODO Auto-generated method stub
		return sessionId.get();
	}
}
