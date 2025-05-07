package za.co.sindi.ai.mcp.server;

import java.util.Objects;

import za.co.sindi.ai.mcp.schema.ClientCapabilities;
import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.InitializeRequest;
import za.co.sindi.ai.mcp.schema.InitializeResult;
import za.co.sindi.ai.mcp.schema.InitializedNotification;
import za.co.sindi.ai.mcp.schema.LoggingLevel;
import za.co.sindi.ai.mcp.schema.MCPSchema;
import za.co.sindi.ai.mcp.schema.ProtocolVersion;
import za.co.sindi.ai.mcp.schema.ServerCapabilities;
import za.co.sindi.ai.mcp.shared.ServerTransport;

/**
 * @author Buhake Sindi
 * @since 25 March 2025
 */
public class DefaultServer extends Server {

	private Implementation serverInfo;
	private ServerCapabilities serverCapabilities;
	private String instructions;
	private Implementation clientInfo;
	private ClientCapabilities clientCapabilities;
	
	private LoggingLevel loggingLevel = LoggingLevel.DEBUG;
	
	/**
	 * @param transport
	 * @param serverInfo
	 */
	public DefaultServer(ServerTransport transport, Implementation serverInfo) {
		this(transport, serverInfo, null, null);
	}
	
	/**
	 * @param transport
	 * @param serverInfo
	 * @param serverCapabilities
	 */
	public DefaultServer(ServerTransport transport, Implementation serverInfo, ServerCapabilities serverCapabilities) {
		this(transport, serverInfo, serverCapabilities, null);
	}
	
	/**
	 * @param transport
	 * @param serverInfo
	 * @param serverCapabilities
	 * @param instructions
	 */
	public DefaultServer(ServerTransport transport, Implementation serverInfo, ServerCapabilities serverCapabilities, String instructions) {
		super();
		this.serverInfo = Objects.requireNonNull(serverInfo, "A server info is required.");
		this.serverCapabilities = serverCapabilities; // Objects.requireNonNull(serverCapabilities, "A server capabilities is required.");
		this.instructions = instructions;
		setTransport(transport);
		
		addRequestHandler(InitializeRequest.METHOD_INITIALIZE, (request, extra) -> {
			InitializeRequest initializeRequest = MCPSchema.toRequest(request);
			clientCapabilities = initializeRequest.getParameters().getCapabilities();
			clientInfo = initializeRequest.getParameters().getClientInfo();
			
			InitializeResult result = new InitializeResult();
			result.setCapabilities(serverCapabilities);
			result.setInstructions(instructions);
			result.setServerInfo(serverInfo);
			result.setProtocolVersion(initializeRequest.getParameters().getProtocolVersion());
			
			return result;
		});
		
		addNotificationHandler(InitializedNotification.METHOD_NOTIFICATION_INITIALIZED, notification -> {});
	}
	
	/**
	 * @return the loggingLevel
	 */
	public LoggingLevel getLoggingLevel() {
		return loggingLevel;
	}

	/**
	 * @param loggingLevel the loggingLevel to set
	 */
	public void setLoggingLevel(LoggingLevel loggingLevel) {
		this.loggingLevel = loggingLevel;
	}

	/**
	 * @return the serverInfo
	 */
	public Implementation getServerInfo() {
		return serverInfo;
	}

	/**
	 * @return the serverCapabilities
	 */
	public ServerCapabilities getServerCapabilities() {
		return serverCapabilities;
	}

	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return instructions;
	}

	/**
	 * @return the clientInfo
	 */
	public Implementation getClientInfo() {
		return clientInfo;
	}

	/**
	 * @return the clientCapabilities
	 */
	public ClientCapabilities getClientCapabilities() {
		return clientCapabilities;
	}
}
