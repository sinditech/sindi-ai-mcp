package za.co.sindi.ai.mcp.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import za.co.sindi.ai.mcp.schema.EmptyResult;
import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.LoggingLevel;
import za.co.sindi.ai.mcp.schema.LoggingMessageNotification;
import za.co.sindi.ai.mcp.schema.LoggingMessageNotification.LoggingMessageNotificationParameters;
import za.co.sindi.ai.mcp.schema.Schema;
import za.co.sindi.ai.mcp.schema.ServerCapabilities;
import za.co.sindi.ai.mcp.schema.SetLevelRequest;
import za.co.sindi.ai.mcp.shared.RequestHandler;
import za.co.sindi.ai.mcp.shared.ServerTransport;
import za.co.sindi.commons.utils.Strings;

/**
 * @author Buhake Sindi
 * @since 25 March 2025
 */
public class DefaultServer extends Server {
	
	private final Map<String, LoggingLevel> loggingLevels = new HashMap<>();
	
	/**
	 * @param serverInfo
	 * @param serverCapabilities
	 * @param instructions
	 */
	public DefaultServer(Implementation serverInfo, ServerCapabilities serverCapabilities, String instructions) {
		super(serverInfo, serverCapabilities, instructions);
		
		if (getCapabilities() != null && getCapabilities().getLogging() != null) {
			addRequestHandler(SetLevelRequest.METHOD_LOGGING_SETLEVEL, setLoggingLevelRequestHandler());
		}
	}
	
	/**
	 * @param transport
	 * @param serverInfo
	 * @param serverCapabilities
	 * @param instructions
	 */
	public DefaultServer(ServerTransport transport, Implementation serverInfo, ServerCapabilities serverCapabilities, String instructions) {
		this(serverInfo, serverCapabilities, instructions);
		
		setTransport(transport);
	}
	
	private RequestHandler<EmptyResult> setLoggingLevelRequestHandler() {
	
		return (request, extra) -> {
			final String transportSessionId = extra.getSessionId();
			final LoggingLevel level = LoggingLevel.of(String.valueOf(request.getParams().get("level")));
			if (!Strings.isNullOrEmpty(transportSessionId) && level != null) {
				loggingLevels.put(transportSessionId, level);
			}
			
			return Schema.EMPTY_RESULT;
		};
	}

	@Override
	public CompletableFuture<Void> sendLoggingMessage(final LoggingMessageNotificationParameters parameters, final String sessionId) {
		if (getCapabilities().getLogging() == null || (!Strings.isNullOrEmpty(sessionId) && isMessageIgnored(parameters.getLevel(), sessionId))) {
			return CompletableFuture.completedFuture(null);
		}
		
		return sendNotification(new LoggingMessageNotification(parameters));
	}
	
	private boolean isMessageIgnored(final LoggingLevel level, final String sessionId) {
		final LoggingLevel currentLevel = loggingLevels.get(sessionId);
		return level.ordinal() < currentLevel.ordinal();
	}
}
