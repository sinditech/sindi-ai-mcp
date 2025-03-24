package za.co.sindi.ai.mcp.shared;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import za.co.sindi.ai.mcp.mapper.ObjectMapper;

/**
 * @author Buhake Sindi
 * @since 16 February 2025
 */
public abstract class AbstractTransport implements Transport {
	
	protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	
	private Duration requestTimeout;
	
	private Executor executor;
	
	private ObjectMapper mapper;
	
	private JSONRPCMessageHandler messageHandler;;
	
	/**
	 * @param mapper
	 */
	protected AbstractTransport(ObjectMapper mapper) {
		super();
		this.mapper = Objects.requireNonNull(mapper, "An object mapper is required.");
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Transport#setRequestTimeout(java.time.Duration)
	 */
	@Override
	public void setRequestTimeout(Duration requestTimeout) {
		// TODO Auto-generated method stub
		this.requestTimeout = requestTimeout;
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Transport#setExecutor(java.util.concurrent.Executor)
	 */
	@Override
	public void setExecutor(Executor executor) {
		// TODO Auto-generated method stub
		this.executor = executor;
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.shared.Transport#setMessageHandler(za.co.sindi.ai.mcp.shared.JSONRPCMessageHandler)
	 */
	@Override
	public void setMessageHandler(JSONRPCMessageHandler messageHandler) {
		// TODO Auto-generated method stub
		this.messageHandler = messageHandler;
	}

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		if (messageHandler != null) {
			messageHandler.onClose();
		}
	}

	/**
	 * @return the executor
	 */
	protected Executor getExecutor() {
		return executor;
	}

	/**
	 * @return the mapper
	 */
	protected ObjectMapper getMapper() {
		return mapper;
	}

	/**
	 * @return the messageHandler
	 */
	protected JSONRPCMessageHandler getMessageHandler() {
		return messageHandler;
	}

	/**
	 * @return the requestTimeout
	 */
	protected Duration getRequestTimeout() {
		return requestTimeout;
	}
}
