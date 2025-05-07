package za.co.sindi.ai.mcp.server;

import java.util.concurrent.CompletableFuture;

import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.LoggingMessageNotification;
import za.co.sindi.ai.mcp.schema.LoggingMessageNotification.LoggingMessageNotificationParameters;
import za.co.sindi.ai.mcp.schema.PromptListChangedNotification;
import za.co.sindi.ai.mcp.schema.ResourceListChangedNotification;
import za.co.sindi.ai.mcp.schema.ResourceUpdatedNotification;
import za.co.sindi.ai.mcp.schema.ResourceUpdatedNotification.ResourceUpdatedNotificationParameters;
import za.co.sindi.ai.mcp.schema.ServerCapabilities;
import za.co.sindi.ai.mcp.schema.ServerNotification;
import za.co.sindi.ai.mcp.schema.ServerRequest;
import za.co.sindi.ai.mcp.schema.ServerResult;
import za.co.sindi.ai.mcp.schema.ToolListChangedNotification;
import za.co.sindi.ai.mcp.shared.Protocol;
import za.co.sindi.ai.mcp.shared.ServerTransport;

/**
 * @author Buhake Sindi
 * @since 15 February 2025
 */
public abstract class Server extends Protocol<ServerTransport, ServerRequest, ServerNotification, ServerResult> {
	
	public CompletableFuture<Void> sendLoggingMessage(final LoggingMessageNotificationParameters parameters) {
		LoggingMessageNotification notification = new LoggingMessageNotification();
		notification.setParameters(parameters);
		return sendNotification(notification);
	}
	
	public CompletableFuture<Void> sendResourceUpdated(final String uri) {
		ResourceUpdatedNotification notification = new ResourceUpdatedNotification();
		ResourceUpdatedNotificationParameters parameters = new ResourceUpdatedNotificationParameters();
		parameters.setUri(uri);
		notification.setParameters(parameters);
		return sendNotification(notification);
	}
	
	public CompletableFuture<Void> sendResourceListChanged() {
		return sendNotification(new ResourceListChangedNotification());
	}
	
	public CompletableFuture<Void> sendToolListChanged() {
		return sendNotification(new ToolListChangedNotification());
	}
	
	public CompletableFuture<Void> sendPromptListChanged() {
		return sendNotification(new PromptListChangedNotification());
	}
	
	public abstract Implementation getServerInfo();

	public abstract ServerCapabilities getServerCapabilities();

	public abstract String getInstructions();
}
