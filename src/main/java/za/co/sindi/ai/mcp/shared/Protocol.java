package za.co.sindi.ai.mcp.shared;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import za.co.sindi.ai.mcp.client.Client;
import za.co.sindi.ai.mcp.schema.CancelledNotification;
import za.co.sindi.ai.mcp.schema.CancelledNotification.CancelledNotificationParameters;
import za.co.sindi.ai.mcp.schema.ErrorCodes;
import za.co.sindi.ai.mcp.schema.JSONRPCError;
import za.co.sindi.ai.mcp.schema.JSONRPCError.Error;
import za.co.sindi.ai.mcp.schema.JSONRPCMessage;
import za.co.sindi.ai.mcp.schema.JSONRPCNotification;
import za.co.sindi.ai.mcp.schema.JSONRPCRequest;
import za.co.sindi.ai.mcp.schema.JSONRPCResult;
import za.co.sindi.ai.mcp.schema.JSONRPCVersion;
import za.co.sindi.ai.mcp.schema.MCPSchema;
import za.co.sindi.ai.mcp.schema.Notification;
import za.co.sindi.ai.mcp.schema.PingRequest;
import za.co.sindi.ai.mcp.schema.Request;
import za.co.sindi.ai.mcp.schema.Result;
import za.co.sindi.ai.mcp.schema.Schema;
import za.co.sindi.ai.mcp.server.Server;
import za.co.sindi.commons.utils.Strings;

/**
 * @author Buhake Sindi
 * @since 15 February 2025
 */
public abstract class Protocol<T extends Transport, REQ extends Request, N extends Notification, RES extends Result> implements AutoCloseable {
	
	protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	
	private static final Duration DEFAULT_REQUEST_TIMEOUT_MSEC = Duration.ofMillis(60000);
	
	private final AtomicLong requestId = new AtomicLong(0);
	
	/** Map of request handlers keyed by method name */
	private final ConcurrentHashMap<String, RequestHandler<? extends Result>> requestHandlers = new ConcurrentHashMap<>();

	/** Map of notification handlers keyed by method name */
	private final ConcurrentHashMap<String, NotificationHandler> notificationHandlers = new ConcurrentHashMap<>();
	
	/** Map of response handlers keyed by request id */
	private final ConcurrentHashMap<Long, ResponseHandler> responseHandlers = new ConcurrentHashMap<>();
	
	/** Map of progress handlers keyed by request id */
	private final ConcurrentHashMap<Long, ProgressHandler> progressHandlers = new ConcurrentHashMap<>();
	
	private Executor executor = Executors.newVirtualThreadPerTaskExecutor();
	
	private T transport;
	
	private Duration requestTimeout;
	
	private RequestHandler<? extends Result> fallbackRequestHandler;
	
	private NotificationHandler fallbackNotificationHandler; 
	
	/**
	 * @param transport
	 */
	protected Protocol(T transport) {
		super();
		this.transport = Objects.requireNonNull(transport, "A MCP Transport is required.");
		setRequestTimeout(DEFAULT_REQUEST_TIMEOUT_MSEC);
		
		requestHandlers.put(PingRequest.METHOD_PING, request -> Schema.EMPTY_RESULT);
	}

	/**
	 * @return the transport
	 */
	public T getTransport() {
		return transport;
	}
	
	/**
	 * @return the executor
	 */
	public Executor getExecutor() {
		return executor;
	}

	/**
	 * @param executor the executor to set
	 */
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	/**
	 * @return the requestTimeout
	 */
	public Duration getRequestTimeout() {
		return requestTimeout;
	}

	/**
	 * @param requestTimeout the requestTimeout to set
	 */
	public void setRequestTimeout(Duration requestTimeout) {
		this.requestTimeout = requestTimeout;
	}

	/**
	 * @param fallbackRequestHandler the fallbackRequestHandler to set
	 */
	public void setFallbackRequestHandler(RequestHandler<? extends Result> fallbackRequestHandler) {
		this.fallbackRequestHandler = fallbackRequestHandler;
	}

	/**
	 * @param fallbackNotificationHandler the fallbackNotificationHandler to set
	 */
	public void setFallbackNotificationHandler(NotificationHandler fallbackNotificationHandler) {
		this.fallbackNotificationHandler = fallbackNotificationHandler;
	}

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		transport.close();
	}
	
	public void closeQuietly() {
		try {
			close();
		} catch(Exception e) {
			LOGGER.log(Level.WARNING, "Error when closing protocol and/or transport.", e);
		}
	}
	
	public void onClose() {}
	
	public void onError(final Throwable throwable) {}
	
	public void connect() {
		try {
			connectAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			throw new TransportException(e);
		}
	}
	
	public CompletableFuture<Void> connectAsync() {
		final String type = this instanceof Server ? "Server" : this instanceof Client ? "Client" : "Protocol";
		transport.setMessageHandler(new JSONRPCMessageHandler() {
			
			@Override
			public void onMessage(JSONRPCMessage message) {
				// TODO Auto-generated method stub
				if (message instanceof JSONRPCRequest request) {
					onJSONRPCRequest(request);
				} else if (message instanceof JSONRPCNotification notification) {
					onJSONRPCNotification(notification);
				} else if (message instanceof JSONRPCResult result) {
					onJSONRPCResponse(result, null);
				} else if (message instanceof JSONRPCError error) {
					onJSONRPCResponse(null, error);
				}
			}
			
			@Override
			public void onError(Throwable throwable) {
				// TODO Auto-generated method stub
				Protocol.this.onError(throwable);
			}
			
			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				LOGGER.info(type + " transport closed.");
				progressHandlers.clear();
				Protocol.this.onClose();
				
				var mcpError = new MCPError(ErrorCodes.CONNECTION_CLOSED, type + " connection closed.");
				for (ResponseHandler handler: responseHandlers.values()) {
					handler.handle(mcpError);
				}
				
				responseHandlers.clear();
			}
		});
		
		if (transport.getExecutor() == null) {
			transport.setExecutor(executor);
		}
		return transport.startAsync();
	}
	
	public CompletableFuture<Void> sendNotification(N notification) {
		JSONRPCNotification jsonRPCNotification = MCPSchema.toJSONRPCNotification(notification);
		LOGGER.info("Sending notification: " + jsonRPCNotification.getMethod());
		return transport.sendAsync(jsonRPCNotification);
	}
	
	@SuppressWarnings("hiding")
	public <T extends Result> CompletableFuture<T> sendRequest(REQ request, Class<T> resultType) {
		CompletableFuture<T> cf = new CompletableFuture<>();
		JSONRPCRequest jsonRpcRequest = MCPSchema.toJSONRPCRequest(request);
		LOGGER.info("Sending request: " + jsonRpcRequest.getMethod());
		
		long messageId = requestId.getAndIncrement();
		jsonRpcRequest.setId(messageId);
		
		//TODO: Handle progress?
		
		//continue...
		responseHandlers.put(messageId, new ResponseHandler() {
			
			/* (non-Javadoc)
			 * @see za.co.sindi.ai.mcp.shared.ResponseHandler#handle(java.lang.Throwable)
			 */
			@Override
			public void handle(Throwable cause) {
				// TODO Auto-generated method stub
				LOGGER.log(Level.SEVERE, "Encountered the following exception.", cause);
				cf.completeExceptionally(cause);
			}

			@Override
			public void handle(Error error) {
				// TODO Auto-generated method stub
				handle(new IllegalStateException(error.getMessage()));
			}
			
			@Override
			public void handle(JSONRPCResult result) {
				// TODO Auto-generated method stub
				try {
					cf.complete(MCPSchema.toResult(result, resultType));
				} catch (Throwable cause) {
					cf.completeExceptionally(cause);
				}
			}
		});
		
		Consumer<Throwable> cancel = throwable -> {
			responseHandlers.remove(messageId);
	        progressHandlers.remove(messageId);
	        
	        CancelledNotification cancelledNotification = new CancelledNotification();
	        CancelledNotificationParameters parameters = new CancelledNotificationParameters();
	        parameters.setRequestId(messageId);
	        parameters.setReason(Strings.isNullOrEmpty(throwable.getMessage()) ? "Unknown": throwable.getMessage());
	        cancelledNotification.setParameters(parameters);
	        transport.send(MCPSchema.toJSONRPCNotification(cancelledNotification));
	        
//	        cf.completeExceptionally(throwable);
		};
		
		transport.sendAsync(jsonRpcRequest)
		 .orTimeout(requestTimeout.toMillis(), TimeUnit.MILLISECONDS)
		 .whenComplete((result, exception) -> {
			 if (exception != null) {
				 // Handle timeout or other errors
				 if (exception instanceof TimeoutException) {
					 LOGGER.severe("Request timed out after " + requestTimeout.toMillis() + "ms: " + jsonRpcRequest.getMethod());
					 cancel.accept(exception);
					 cf.cancel(true);
				 }
				 cf.completeExceptionally(exception);
			 }
		 });
		
		return cf;
	}
	
	public void addRequestHandler(final String method, final RequestHandler<? extends Result> handler) {
		requestHandlers.put(method, handler);
	}
	
	public void addNotificationHandler(final String method, final NotificationHandler handler) {
		notificationHandlers.put(method, handler);
	}
	
	private void onJSONRPCRequest(JSONRPCRequest request) {
		LOGGER.info("Received request: " + request.getMethod() + ", ID: " + request.getId());
		RequestHandler<? extends Result> handler = requestHandlers.containsKey(request.getMethod()) ? requestHandlers.get(request.getMethod()) : fallbackRequestHandler;
		
		if (handler == null) {
			LOGGER.info("No handler found for request: " + request.getMethod());
			sendError(request.getId(), ErrorCodes.METHOD_NOT_FOUND, "Server does not support " + request.getMethod())
			.exceptionally(throwable -> {
				LOGGER.severe("Error sending method not found response.");
				Protocol.this.onError(throwable);
				return null;
			});
		} else {
			Result result = handler.handle(request);
			LOGGER.info("Request handled successfully: " + request.getMethod() + ", ID: " + request.getId());
			sendResult(request.getId(), result)
			.handle((response, throwable) -> {
				if (throwable != null) {
					LOGGER.severe("Error handling request: " + request.getMethod() + ", ID: " + request.getId());
					if (throwable instanceof MCPError error) {
						return sendError(request.getId(), error.getCode(), error.getMessage());
					} else {
						return sendError(request.getId(), ErrorCodes.INTERNAL_ERROR, Strings.isNullOrEmpty(throwable.getMessage()) ? "Internal error." : throwable.getMessage());
					}
				}
				
				// If no exception, return the original result
	            return CompletableFuture.completedFuture(response);
			}).thenCompose(future -> future);
		}
	}
	
	private void onJSONRPCNotification(JSONRPCNotification notification) {
		LOGGER.info("Received notification: " + notification.getMethod());
		NotificationHandler handler = notificationHandlers.containsKey(notification.getMethod()) ? notificationHandlers.get(notification.getMethod()) : fallbackNotificationHandler;
		if (handler == null) {
			LOGGER.info("No handler found for notification: " + notification.getMethod());
			return;
		}
		
		try {
			handler.handle(notification);
		} catch (Throwable throwable) {
			LOGGER.severe("Error handling notification: " + notification.getMethod());
			Protocol.this.onError(throwable);
		}
	}
	
	private void onJSONRPCResponse(JSONRPCResult jsonRpcResult, JSONRPCError jsonRpcError) {
		long responseId = jsonRpcResult != null ? jsonRpcResult.getId() : jsonRpcError.getId();
		ResponseHandler handler = responseHandlers.get(responseId);
		if (handler == null) {
			Protocol.this.onError(new TransportException("Received a response for an unknown message ID: " + responseId));
			return;
		}
		
		responseHandlers.remove(responseId);
		progressHandlers.remove(responseId);
		
		if (jsonRpcResult != null) {
			handler.handle(jsonRpcResult);
		} else if (jsonRpcError != null) {
			handler.handle(jsonRpcError.getError());
		}
	}
	
	private CompletableFuture<Void> sendResult(final long requestId, final Result result) {
		JSONRPCResult jsonRPCResult = new JSONRPCResult();
		jsonRPCResult.setJsonrpc(JSONRPCVersion.getLatest());
		jsonRPCResult.setId(requestId);
		jsonRPCResult.setResult(result);
		
		return transport.sendAsync(jsonRPCResult);
	}
	
	private CompletableFuture<Void> sendError(final long id, final int errorCode, final String message) {
		JSONRPCError jsonRPCError = new JSONRPCError();
		jsonRPCError.setJsonrpc(JSONRPCVersion.getLatest());
		jsonRPCError.setId(id);
		Error error = new Error();
		error.setCode(errorCode);
		error.setMessage(message);
		jsonRPCError.setError(error);
		
		return transport.sendAsync(jsonRPCError);
	}
}
