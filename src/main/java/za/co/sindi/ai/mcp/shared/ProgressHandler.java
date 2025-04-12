package za.co.sindi.ai.mcp.shared;

import za.co.sindi.ai.mcp.schema.Notification;
import za.co.sindi.ai.mcp.schema.ProgressNotification;

/**
 * @author Buhake Sindi
 * @since 13 March 2025
 */
public abstract class ProgressHandler implements NotificationHandler {
	
	public void handle(Notification notification) {
		if (notification instanceof ProgressNotification progressNotification) handle(progressNotification);
	}

	public void handle(final ProgressNotification progressNotification) {
		handle(progressNotification.getParameters().getProgressToken(), progressNotification.getParameters().getProgress(), progressNotification.getParameters().getTotal());
	}
	
	public abstract void handle(final ProgressToken progressToken, final long progress, final Long total);
}
