/**
 * 
 */
package za.co.sindi.ai.mcp.server;

/**
 * @author Buhake Sindi
 * @since 02 June 2025
 */
@FunctionalInterface
public interface SessionInitializationEvent {

	public void onSessionInitialized(final String sessionId);
}
