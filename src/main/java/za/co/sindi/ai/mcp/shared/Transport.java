package za.co.sindi.ai.mcp.shared;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import za.co.sindi.ai.mcp.schema.JSONRPCMessage;

/**
 * @author Buhake Sindi
 * @since 15 February 2025
 */
public interface Transport extends AutoCloseable {
	
	/**
	 * @param requestTimeout the requestTimeout to set
	 */
	public void setRequestTimeout(Duration requestTimeout);

	/**
	 * @param executor the executor to set
	 */
	public void setExecutor(Executor executor);
	
	public Executor getExecutor();
	
	public void setMessageHandler(final JSONRPCMessageHandler messageHandler);
	
	default void start() {
		try {
			startAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			throw new TransportException(e);
		}
	}
	
	public CompletableFuture<Void> startAsync();

	default void send(final JSONRPCMessage message) {
		try {
			sendAsync(message).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			throw new TransportException(e);
		}
	}
	
	public CompletableFuture<Void> sendAsync(final JSONRPCMessage message);
	
	default String getSessionId() {
		return null;
	}
}
