/**
 * 
 */
package za.co.sindi.ai.mcp.server;

import za.co.sindi.ai.mcp.shared.ServerTransport;

/**
 * @author Buhake Sindi
 * @since 02 June 2025
 */
@FunctionalInterface
public interface SessionInitializationEvent<T extends ServerTransport> {

	public void onSessionInitialized(final String sessionId, final T transport);
}
