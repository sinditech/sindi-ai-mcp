package za.co.sindi.ai.mcp.shared;

import za.co.sindi.ai.mcp.schema.JSONRPCNotification;
import za.co.sindi.ai.mcp.schema.MCPSchema;
import za.co.sindi.ai.mcp.schema.Notification;
import za.co.sindi.ai.mcp.schema.ProgressNotification;
import za.co.sindi.ai.mcp.schema.ProgressToken;

/**
 * @author Buhake Sindi
 * @since 13 March 2025
 */
@FunctionalInterface
public interface ProgressHandler extends NotificationHandler {
	
	@Override
	default void handle(JSONRPCNotification notification) {
		// TODO Auto-generated method stub
		handle((ProgressNotification)MCPSchema.toNotification(notification));
	}

	default void handle(Notification notification) {
		if (notification instanceof ProgressNotification progressNotification) handle(progressNotification);
	}

	default void handle(final ProgressNotification progressNotification) {
		handle(progressNotification.getParameters().getProgressToken(), progressNotification.getParameters().getProgress(), progressNotification.getParameters().getTotal());
	}
	
	public abstract void handle(final ProgressToken progressToken, final long progress, final Long total);
}
