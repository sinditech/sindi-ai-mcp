package za.co.sindi.ai.mcp.server;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import za.co.sindi.ai.mcp.schema.JSONRPCMessage;
import za.co.sindi.ai.mcp.schema.MCPSchema;
import za.co.sindi.ai.mcp.shared.AbstractTransport;
import za.co.sindi.ai.mcp.shared.ServerTransport;
import za.co.sindi.ai.mcp.shared.TransportException;

/**
 * @author Buhake Sindi
 * @since 20 March 2025
 */
@Deprecated
public class SSEServerTransport extends AbstractTransport implements ServerTransport {
	
	/** Event type for regular messages */
	private static final String MESSAGE_EVENT_TYPE = "message";

	/** Event type for endpoint information */
	private static final String ENDPOINT_EVENT_TYPE = "endpoint";
	
	private static final String APPLICATION_JSON = "application/json";
	
	private static final String TEXT_PLAIN = "text/plain";
	
	private final AtomicBoolean initialized = new AtomicBoolean(false);
	
	private final String messageEndpoint;
	
	private final String sessionIdParameterName;
	
	private final String sessionId;
	
	private SSEServerSession sseSession;
	
	/**
	 * @param messageEndpoint
	 * @param sseSession
	 */
	public SSEServerTransport(String messageEndpoint, SSEServerSession sseSession) {
		this(messageEndpoint, "sessionId", UUID.randomUUID().toString(), sseSession);
	}
	
	/**
	 * @param messageEndpoint
	 * @param sessionIdParameterName
	 * @param sseSession
	 */
	public SSEServerTransport(String messageEndpoint, String sessionIdParameterName, SSEServerSession sseSession) {
		this(messageEndpoint,sessionIdParameterName, UUID.randomUUID().toString(), sseSession);
	}

	/**
	 * @param messageEndpoint
	 * @param sessionIdParameterName
	 * @param sessionId
	 * @param sseSession
	 */
	public SSEServerTransport(String messageEndpoint, String sessionIdParameterName,
			String sessionId, SSEServerSession sseSession) {
		super();
		this.messageEndpoint = messageEndpoint;
		this.sessionIdParameterName = sessionIdParameterName;
		this.sessionId = sessionId;
		this.sseSession = sseSession;
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Transport#startAsync()
	 */
	@Override
	public CompletableFuture<Void> startAsync() {
		// TODO Auto-generated method stub
		if (sseSession == null) {
			throw new TransportException("SSE session hasn't been initialized! Set the session with the setSseSession() method.");
		}
		
		if (!initialized.compareAndSet(false, true)) {
            throw new TransportException("SSEServerTransport already started! If using Server class, note that connect() calls start() automatically.");
        }
		
		Runnable runner = () -> {
			sseSession.broadcast(TEXT_PLAIN, ENDPOINT_EVENT_TYPE, messageEndpoint + "?" + sessionIdParameterName + "=" + sessionId);
		};
		return getExecutor() != null ?  CompletableFuture.runAsync(runner, getExecutor()) : CompletableFuture.runAsync(runner);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Transport#sendAsync(za.co.sindi.ai.mcp.schema.JSONRPCMessage)
	 */
	@Override
	public CompletableFuture<Void> sendAsync(JSONRPCMessage message) {
		// TODO Auto-generated method stub
		if (sseSession == null) {
			throw new TransportException("SSE session hasn't been initialized! Set the session with the setSseSession() method.");
		}
		
		if (!initialized.get()) {
			throw new TransportException("Transport not connected.");
		}
		
		Runnable runner = () -> {
			sseSession.broadcast(APPLICATION_JSON, MESSAGE_EVENT_TYPE, MCPSchema.serializeJSONRPCMessage(message)); // getMapper().map(message)
		};
		return getExecutor() != null ? CompletableFuture.runAsync(runner, getExecutor()) : CompletableFuture.runAsync(runner);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.AbstractTransport#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		initialized.compareAndSet(true, false);
		if (sseSession != null) {
			sseSession.close();
			sseSession = null;
		}
		super.close();
	}
	
	public CompletableFuture<Void> handleMessage(final String message) {
		
		return handleMessage(MCPSchema.deserializeJSONRPCMessage(message));
	}
	
	public CompletableFuture<Void> handleMessage(final JSONRPCMessage message) {
		
		return CompletableFuture.runAsync(() -> getMessageHandler().onMessage(message), getExecutor());
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
}
