package za.co.sindi.ai.mcp.server;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import za.co.sindi.ai.mcp.mapper.ObjectMapper;
import za.co.sindi.ai.mcp.schema.JSONRPCMessage;
import za.co.sindi.ai.mcp.shared.AbstractTransport;
import za.co.sindi.ai.mcp.shared.ServerTransport;
import za.co.sindi.ai.mcp.shared.TransportException;

/**
 * @author Buhake Sindi
 * @since 20 March 2025
 */
public class SSEServerTransport extends AbstractTransport implements ServerTransport {
	
	/** Event type for regular messages */
	private static final String MESSAGE_EVENT_TYPE = "message";

	/** Event type for endpoint information */
	private static final String ENDPOINT_EVENT_TYPE = "endpoint";
	
	private static final String UTF_8 = "UTF-8";

	private static final String APPLICATION_JSON = "application/json";
	
	private static final String TEXT_PLAIN = "text/plain";
	
	private final AtomicBoolean initialized = new AtomicBoolean(false);
	
	private final String messageEndpoint;
	
	private final String sessionIdParameterName;
	
	private final String sessionId;
	
	private final SSEServerSession sseSession;
	
	/**
	 * @param mapper
	 * @param messageEndpoint
	 * @param sseSession
	 */
	public SSEServerTransport(ObjectMapper mapper, String messageEndpoint, SSEServerSession sseSession) {
		this(mapper, messageEndpoint, "sessionId", UUID.randomUUID().toString(), sseSession);
	}
	
	/**
	 * @param mapper
	 * @param messageEndpoint
	 * @param sessionIdParameterName
	 * @param sseSession
	 */
	public SSEServerTransport(ObjectMapper mapper, String messageEndpoint, String sessionIdParameterName, SSEServerSession sseSession) {
		this(mapper, messageEndpoint,sessionIdParameterName, UUID.randomUUID().toString(), sseSession);
	}

	/**
	 * @param mapper
	 * @param messageEndpoint
	 * @param sessionIdParameterName
	 * @param sessionId
	 * @param sseSession
	 */
	public SSEServerTransport(ObjectMapper mapper, String messageEndpoint, String sessionIdParameterName,
			String sessionId, SSEServerSession sseSession) {
		super(mapper);
		this.messageEndpoint = messageEndpoint;
		this.sessionIdParameterName = sessionIdParameterName;
		this.sessionId = sessionId;
		this.sseSession = sseSession;
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Transport#start()
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		if (!initialized.compareAndSet(false, true)) {
            throw new TransportException("SSEServerTransport already started! If using Server class, note that connect() calls start() automatically.");
        }
		
		Runnable runner = () -> {
			sseSession.broadcast(TEXT_PLAIN, ENDPOINT_EVENT_TYPE, messageEndpoint + "?" + sessionIdParameterName + "=" + sessionId);
		};
		CompletableFuture<Void> future = getExecutor() != null ?  CompletableFuture.runAsync(runner, getExecutor()) : CompletableFuture.runAsync(runner);
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
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
			throw new TransportException("Transport not connected.");
		}
		
		Runnable runner = () -> {
			sseSession.broadcast(APPLICATION_JSON, MESSAGE_EVENT_TYPE, getMapper().map(message));
		};
		return getExecutor() != null ?  CompletableFuture.runAsync(runner, getExecutor()) : CompletableFuture.runAsync(runner);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.AbstractTransport#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		initialized.compareAndSet(true, false);
		sseSession.close();
		super.close();
	}
	
	public CompletableFuture<Void> handleMessage(final String message) {
		
		return handleMessage(getMapper().map(message));
	}
	
	public CompletableFuture<Void> handleMessage(final JSONRPCMessage message) {
		
		return CompletableFuture.runAsync(()-> getMessageHandler().onMessage(message));
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
}
