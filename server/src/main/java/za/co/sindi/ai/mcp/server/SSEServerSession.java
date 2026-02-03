package za.co.sindi.ai.mcp.server;

import java.util.concurrent.CompletionStage;

/**
 * @author Buhake Sindi
 * @since 20 March 2025
 */
@Deprecated
public interface SSEServerSession {

	public <T> CompletionStage<?> broadcast(final String mediaType, final String eventType, final T data);
	public void close();
}