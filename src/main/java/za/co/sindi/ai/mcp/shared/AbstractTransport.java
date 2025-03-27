package za.co.sindi.ai.mcp.shared;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

/**
 * @author Buhake Sindi
 * @since 16 February 2025
 */
public abstract class AbstractTransport implements Transport {
	
	protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	
	private Duration requestTimeout;
	
	private Executor executor;
	
	private JSONRPCMessageHandler messageHandler;;
	
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
