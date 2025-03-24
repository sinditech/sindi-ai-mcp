package za.co.sindi.ai.mcp.shared;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
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
import za.co.sindi.ai.mcp.server.Server;
import za.co.sindi.ai.mcp.schema.JSONRPCMessage;
import za.co.sindi.ai.mcp.schema.JSONRPCNotification;
import za.co.sindi.ai.mcp.schema.JSONRPCRequest;
import za.co.sindi.ai.mcp.schema.JSONRPCResponse;
import za.co.sindi.ai.mcp.schema.JSONRPCResult;
import za.co.sindi.ai.mcp.schema.Notification;
import za.co.sindi.ai.mcp.schema.PingRequest;
import za.co.sindi.ai.mcp.schema.Request;
import za.co.sindi.ai.mcp.schema.Result;
import za.co.sindi.ai.mcp.schema.Schema;
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
	private final ConcurrentHashMap<String, RequestHandler<? extends RES>> requestHandlers = new ConcurrentHashMap<>();

	/** Map of notification handlers keyed by method name */
	private final ConcurrentHashMap<String, NotificationHandler> notificationHandlers = new ConcurrentHashMap<>();
	
	/** Map of response handlers keyed by request id */
	private final ConcurrentHashMap<Long, ResponseHandler> responseHandlers = new ConcurrentHashMap<>();
	
	/** Map of progress handlers keyed by request id */
	private final ConcurrentHashMap<Long, ProgressHandler> progressHandlers = new ConcurrentHashMap<>();
	
	private Executor executor;
	
	private T transport;
	
	private Duration requestTimeout;
	
	private RequestHandler<? extends RES> fallbackRequestHandler;
	
	private NotificationHandler fallbackNotificationHandler; 
	
	/**
	 * @param transport
	 */
	@SuppressWarnings("unchecked")
	protected Protocol(T transport) {
		super();
		this.transport = Objects.requireNonNull(transport, "A MCP Transport is required.");
		setRequestTimeout(DEFAULT_REQUEST_TIMEOUT_MSEC);
		
		requestHandlers.put(PingRequest.METHOD_PING, request -> (RES)Schema.EMPTY_RESULT);
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
	public void setFallbackRequestHandler(RequestHandler<RES> fallbackRequestHandler) {
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
		final String type = this instanceof Server ? "Server" : this instanceof Client ? "Client" : "Protocol";
		transport.setMessageHandler(new JSONRPCMessageHandler() {
			
			@Override
			public void onMessage(JSONRPCMessage message) {
				// TODO Auto-generated method stub
				if (message instanceof JSONRPCRequest request) {
					onJSONRPCRequest(request);
				} else if (message instanceof JSONRPCNotification notification) {
					onJSONRPCNotification(notification);
				} else if (message instanceof JSONRPCResponse response) {
					onJSONRPCResponse(response);
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
		
		transport.start();
	}
	
	public void sendNotification(N notification) {
		LOGGER.info("Sending notification: " + notification.getMethod());
		if (notification instanceof JSONRPCNotification) {
			transport.send((JSONRPCNotification)notification);
		}
	}
	
	@SuppressWarnings("hiding")
	public <T extends RES> T sendRequest(REQ request) {
		LOGGER.info("Sending request: " + request.getMethod());
		CompletableFuture<T> cf = new CompletableFuture<>();
		if (request instanceof JSONRPCRequest jsonRpcRequest) {
			long messageId = requestId.incrementAndGet();
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
					cf.completeExceptionally(cause);
				}

				@Override
				public void handle(Error error) {
					// TODO Auto-generated method stub
					cf.completeExceptionally(new IllegalStateException(error.getMessage()));
				}
				
				@SuppressWarnings("unchecked")
				@Override
				public void handle(Result result) {
					// TODO Auto-generated method stub
					try {
						cf.complete((T)result);
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
		        transport.send(cancelledNotification);
		        
		        cf.completeExceptionally(throwable);
			};
			
			try {
				transport.sendAsync(jsonRpcRequest).get(requestTimeout.toMillis(), TimeUnit.MILLISECONDS);
				return cf.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				throw new TransportException(e);
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				LOGGER.severe("Request timed out after " + requestTimeout.toMillis() + "ms: " + request.getMethod());
				cancel.accept(e);
				cf.cancel(true);
				throw new TransportException(e);
			}
		}
		
		return null;
	}
	
	public void addRequestHandler(final String method, final RequestHandler<? extends RES> handler) {
		requestHandlers.put(method, handler);
	}
	
	public void addNotificationHandler(final String method, final NotificationHandler handler) {
		notificationHandlers.put(method, handler);
	}
	
	private void onJSONRPCRequest(JSONRPCRequest request) {
		LOGGER.info("Received request: " + request.getMethod() + ", ID: " + request.getId());
		RequestHandler<? extends RES> handler = requestHandlers.containsKey(request.getMethod()) ? requestHandlers.get(request.getMethod()) : fallbackRequestHandler;
		
		if (handler == null) {
			LOGGER.info("No handler found for request: " + request.getMethod());
			try {
				sendError(request.getId(), ErrorCodes.METHOD_NOT_FOUND, "Server does not support " + request.getMethod());
			} catch(Throwable throwable) {
				LOGGER.severe("Error sending method not found response.");
				Protocol.this.onError(throwable);
			}
			return;
		}
		
		try {
			RES result = handler.handle(request);
			LOGGER.info("Request handled successfully: " + request.getMethod() + ", ID: " + request.getId());
			JSONRPCResult jsonRPCResult = new JSONRPCResult();
			jsonRPCResult.setId(request.getId());
			jsonRPCResult.setResult(result);
			transport.send(jsonRPCResult);
		} catch(Throwable throwable) {
			try {
				LOGGER.severe("Error handling request: " + request.getMethod() + ", ID: " + request.getId());
				if (throwable instanceof MCPError error) {
					sendError(request.getId(), error.getCode(), error.getMessage());
				} else {
					sendError(request.getId(), ErrorCodes.INTERNAL_ERROR, Strings.isNullOrEmpty(throwable.getMessage()) ? "Internal error." : throwable.getMessage());
				}
			} catch(Throwable sendError) {
				LOGGER.log(Level.SEVERE, "Failed to send error response for request: " + request.getMethod() + ", ID: " + request.getId(), sendError);
			}
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
	
	private void onJSONRPCResponse(JSONRPCResponse response) {
		ResponseHandler handler = responseHandlers.get(response.getId());
		if (handler == null) {
			Protocol.this.onError(new TransportException("Received a response for an unknown message ID: " + response.getId()));
			return;
		}
		
		responseHandlers.remove(response.getId());
		progressHandlers.remove(response.getId());
		
		if (response instanceof JSONRPCResult jsonRpcResult) {
			handler.handle(jsonRpcResult.getResult());
		} else if (response instanceof JSONRPCError jsonRpcError) {
			handler.handle(jsonRpcError.getError());
		}
	}
	
	private void sendError(final long id, final int errorCode, final String message) {
		JSONRPCError jsonRPCError = new JSONRPCError();
		jsonRPCError.setId(id);
		Error error = new Error();
		error.setCode(errorCode);
		error.setMessage(message);
		jsonRPCError.setError(error);
		
		transport.send(jsonRPCError);
	}
}
