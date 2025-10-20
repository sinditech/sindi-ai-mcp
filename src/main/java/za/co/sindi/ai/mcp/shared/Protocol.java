package za.co.sindi.ai.mcp.shared;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
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
import za.co.sindi.ai.mcp.schema.JSONRPCResponse;
import za.co.sindi.ai.mcp.schema.JSONRPCResult;
import za.co.sindi.ai.mcp.schema.JSONRPCVersion;
import za.co.sindi.ai.mcp.schema.MCPSchema;
import za.co.sindi.ai.mcp.schema.Notification;
import za.co.sindi.ai.mcp.schema.PingRequest;
import za.co.sindi.ai.mcp.schema.ProgressNotification;
import za.co.sindi.ai.mcp.schema.ProgressToken;
import za.co.sindi.ai.mcp.schema.Request;
import za.co.sindi.ai.mcp.schema.RequestId;
import za.co.sindi.ai.mcp.schema.RequestMeta;
import za.co.sindi.ai.mcp.schema.Result;
import za.co.sindi.ai.mcp.schema.Schema;
import za.co.sindi.ai.mcp.server.Server;
import za.co.sindi.commons.utils.Preconditions;
import za.co.sindi.commons.utils.Strings;
import za.co.sindi.commons.utils.Throwables;

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
	private final ConcurrentHashMap<RequestId, ResponseHandler> responseHandlers = new ConcurrentHashMap<>();
	
	/** Map of progress handlers keyed by request id */
	private final ConcurrentHashMap<RequestId, ProgressHandler> progressHandlers = new ConcurrentHashMap<>();
	
	private final Set<String> pendingDebouncedNotifications = Collections.synchronizedSet(new HashSet<>());
	
	private Executor executor = Executors.newVirtualThreadPerTaskExecutor();
	
	private T transport;
	
	private Duration requestTimeout;
	
	private CloseCallback closeCallback;
	
	private RequestHandler<? extends Result> fallbackRequestHandler;
	
	private NotificationHandler fallbackNotificationHandler;
	
	private ErrorHandler errorHandler;
	
	/**
	 * @param transport
	 */
	protected Protocol() {
		super();
		setRequestTimeout(DEFAULT_REQUEST_TIMEOUT_MSEC);
		
		addNotificationHandler(ProgressNotification.METHOD_NOTIFICATION_PROGRESS, notification -> onProgress(MCPSchema.toNotification(notification)));
		
		addRequestHandler(PingRequest.METHOD_PING, (request, extra) -> Schema.EMPTY_RESULT);
	}

	/**
	 * @return the transport
	 */
	public T getTransport() {
		return transport;
	}
	
	/**
	 * @param transport the transport to set
	 */
	public void setTransport(T transport) {
		this.transport = transport;
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
	 * @param closeCallback the closeCallback to set
	 */
	public void setCloseCallback(CloseCallback closeCallback) {
		this.closeCallback = closeCallback;
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

	/**
	 * @param errorHandler the errorHandler to set
	 */
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		if (transport != null) transport.close();
	}
	
	public void closeQuietly() {
		try {
			close();
		} catch(Exception e) {
			LOGGER.log(Level.WARNING, "Error when closing protocol and/or transport.", e);
		}
	}
	
	public void onError(final Throwable throwable) {
		if (errorHandler != null) {
			errorHandler.onError(throwable);
		}
	}
	
	public CompletableFuture<Void> connect() {
		Preconditions.checkState(transport != null, "A MCP Transport is required.");
		transport.setMessageHandler(new JSONRPCMessageHandler() {
			
			@Override
			public void onMessage(JSONRPCMessage message) {
				// TODO Auto-generated method stub
				if (message instanceof JSONRPCRequest request) {
					onRequest(request);
				} else if (message instanceof JSONRPCNotification notification) {
					onNotification(notification);
				} else if (message instanceof JSONRPCResponse response) {
					onResponse(response);
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
				Protocol.this.onClose();
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
		return sendRequest(request, resultType, null);
	}
	
	@SuppressWarnings("hiding")
	public <T extends Result> CompletableFuture<T> sendRequest(REQ request, Class<T> resultType, final ProgressHandler progressHandler) {
		CompletableFuture<T> cf = new CompletableFuture<>();
		JSONRPCRequest jsonRpcRequest = MCPSchema.toJSONRPCRequest(request);
		LOGGER.info("Sending request: " + jsonRpcRequest.getMethod());
		
		long messageId = requestId.getAndIncrement();
		jsonRpcRequest.setId(RequestId.of(messageId));
		
		//TODO: Handle progress?
		if (progressHandler != null) {
			progressHandlers.put(jsonRpcRequest.getId(), progressHandler);
			jsonRpcRequest.getParams().put("_meta", Map.of("progressToken", ProgressToken.of(messageId)));
		}
		
		//continue...
		responseHandlers.put(jsonRpcRequest.getId(), new ResponseHandler() {
			
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
					handle(cause);
				}
			}
		});
		
		Consumer<Throwable> cancel = throwable -> {
			responseHandlers.remove(jsonRpcRequest.getId());
	        progressHandlers.remove(jsonRpcRequest.getId());
	        
	        CancelledNotification cancelledNotification = new CancelledNotification();
	        CancelledNotificationParameters parameters = new CancelledNotificationParameters();
	        parameters.setRequestId(jsonRpcRequest.getId());
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
	
	private void onClose() {
		final String type = this instanceof Server ? "Server" : this instanceof Client ? "Client" : "Protocol";
		LOGGER.info(type + " transport closed.");
		progressHandlers.clear();
		pendingDebouncedNotifications.clear();
		if (closeCallback != null) {
			closeCallback.onClose();
		}
		
		var mcpError = new MCPError(ErrorCodes.CONNECTION_CLOSED, type + " connection closed.");
		for (ResponseHandler handler: responseHandlers.values()) {
			handler.handle(mcpError);
		}
		
		responseHandlers.clear();
	}
	
	@SuppressWarnings("unchecked")
	private void onRequest(JSONRPCRequest request) {
		LOGGER.info("Received request: " + request.getMethod() + ", ID: " + request.getId());
		RequestHandler<? extends Result> handler = requestHandlers.containsKey(request.getMethod()) ? requestHandlers.get(request.getMethod()) : fallbackRequestHandler;
		
		if (handler == null) {
			LOGGER.info("No handler found for request: " + request.getMethod());
			sendError(request.getId(), ErrorCodes.METHOD_NOT_FOUND, "Method not found: " + request.getMethod())
			.exceptionally(throwable -> {
				LOGGER.severe("Error sending method not found response.");
				Protocol.this.onError(Throwables.getRootCause(throwable));
				return null;
			});
		} else {
			RequestHandlerExtra extra = new RequestHandlerExtra();
			extra.setRequestId(request.getId());
			extra.setSessionId(transport.getSessionId());
			if (request.getParams().containsKey("_meta")) {
				extra.setMeta(MCPSchema.toObject((Map<String, Object>)request.getParams().get("_meta"), RequestMeta.class));
			}
			
			CompletableFuture.supplyAsync(() -> {
				Result result = handler.handle(request, extra);
				LOGGER.info("Request handled successfully: " + request.getMethod() + ", ID: " + request.getId());
				return result;
			}, getExecutor()).thenCompose(result -> sendResult(request.getId(), result))
			.exceptionallyCompose(throwable -> {
				LOGGER.severe("Error handling request: " + request.getMethod() + ", ID: " + request.getId());
				Throwable rootCause = Throwables.getRootCause(throwable);
				Protocol.this.onError(rootCause);
				if (rootCause instanceof MCPError error) {
					return sendError(request.getId(), error.getCode(), error.getMessage());
				} else {
					return sendError(request.getId(), ErrorCodes.INTERNAL_ERROR, Strings.isNullOrEmpty(rootCause.getMessage()) ? "Internal error." : rootCause.getMessage());
				}
			});
		}
	}
	
	private void onProgress(ProgressNotification notification) {
		Object progressToken = notification.getParameters().getProgressToken().getValue();
		RequestId id = null;
		if (progressToken instanceof String)
			RequestId.of((String)progressToken);
		else if (int.class.equals(progressToken.getClass()))
			RequestId.of((int)progressToken);
		else if (long.class.equals(progressToken.getClass()))
			RequestId.of((long)progressToken);
		
		ProgressHandler handler = progressHandlers.get(id);
		if (handler == null) {
			onError(new IllegalStateException("Received a progress notification for an unknown token: " + String.valueOf(progressToken)));
			return ;
		}
		
		handler.handle(notification);
	}
	
	private void onNotification(JSONRPCNotification notification) {
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
	
	private void onResponse(JSONRPCResponse jsonRpcResponse) {
		ResponseHandler handler = responseHandlers.get(jsonRpcResponse.getId());
		if (handler == null) {
			Protocol.this.onError(new TransportException("Received a response for an unknown message ID: " + jsonRpcResponse.getId()));
			return;
		}
		
		responseHandlers.remove(jsonRpcResponse.getId());
		progressHandlers.remove(jsonRpcResponse.getId());
		
		if (jsonRpcResponse instanceof JSONRPCResult jsonRpcResult) {
			handler.handle(jsonRpcResult);
		} else if (jsonRpcResponse instanceof JSONRPCError jsonRpcError) {
			handler.handle(jsonRpcError.getError());
		}
	}
	
	private CompletableFuture<Void> sendResult(final RequestId requestId, final Result result) {
		JSONRPCResult jsonRPCResult = new JSONRPCResult();
		jsonRPCResult.setJsonrpc(JSONRPCVersion.getLatest());
		jsonRPCResult.setId(requestId);
		jsonRPCResult.setResult(result);
		
		return transport.sendAsync(jsonRPCResult);
	}
	
	private CompletableFuture<Void> sendError(final RequestId id, final int errorCode, final String message) {
		return sendError(id, errorCode, message, null);
	}
	
	private CompletableFuture<Void> sendError(final RequestId id, final int errorCode, final String message, final Object data) {
		JSONRPCError jsonRPCError = new JSONRPCError();
		jsonRPCError.setJsonrpc(JSONRPCVersion.getLatest());
		jsonRPCError.setId(id);
		Error error = new Error();
		error.setCode(errorCode);
		error.setMessage(message);
		error.setData(data);
		jsonRPCError.setError(error);
		
		return transport.sendAsync(jsonRPCError);
	}
}
