package za.co.sindi.ai.mcp.shared;

import za.co.sindi.ai.mcp.schema.JSONRPCNotification;

/**
 * @author Buhake Sindi
 * @since 13 March 2025
 */
@FunctionalInterface
public interface NotificationHandler {

	public void handle(JSONRPCNotification notification);
}
