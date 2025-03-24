package za.co.sindi.ai.mcp.server;

import java.util.Objects;

import za.co.sindi.ai.mcp.schema.ClientCapabilities;
import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.InitializeRequest;
import za.co.sindi.ai.mcp.schema.InitializeResult;
import za.co.sindi.ai.mcp.schema.InitializedNotification;
import za.co.sindi.ai.mcp.schema.ProtocolVersion;
import za.co.sindi.ai.mcp.schema.ServerCapabilities;
import za.co.sindi.ai.mcp.schema.ServerNotification;
import za.co.sindi.ai.mcp.schema.ServerRequest;
import za.co.sindi.ai.mcp.schema.ServerResult;
import za.co.sindi.ai.mcp.shared.Protocol;
import za.co.sindi.ai.mcp.shared.ServerTransport;

/**
 * @author Buhake Sindi
 * @since 15 February 2025
 */
public abstract class Server extends Protocol<ServerTransport, ServerRequest, ServerNotification, ServerResult> {

	protected Implementation serverInfo;
	protected ServerCapabilities serverCapabilities;
	protected String instructions;
	protected Implementation clientInfo;
	protected ClientCapabilities clientCapabilities;
	
	/**
	 * @param transport
	 * @param serverInfo
	 * @param serverCapabilities
	 */
	protected Server(ServerTransport transport, Implementation serverInfo) {
		this(transport, serverInfo, null, null);
	}

	/**
	 * @param transport
	 * @param serverInfo
	 * @param serverCapabilities
	 * @param instructions
	 */
	protected Server(ServerTransport transport, Implementation serverInfo, ServerCapabilities serverCapabilities) {
		this(transport, serverInfo, serverCapabilities, null);
	}
	
	/**
	 * @param transport
	 * @param serverInfo
	 * @param serverCapabilities
	 * @param instructions
	 */
	protected Server(ServerTransport transport, Implementation serverInfo, ServerCapabilities serverCapabilities, String instructions) {
		super(transport);
		this.serverInfo = Objects.requireNonNull(serverInfo, "A server info is required.");
		this.serverCapabilities = Objects.requireNonNull(serverCapabilities, "A server capabilities is required.");
		this.instructions = instructions;
		
		addRequestHandler(InitializeRequest.METHOD_INITIALIZE, request -> {
			var initializeRequest = (InitializeRequest) request;
			clientCapabilities = initializeRequest.getParameters().getCapabilities();
			clientInfo = initializeRequest.getParameters().getClientInfo();
			
			InitializeResult result = new InitializeResult();
			result.setCapabilities(serverCapabilities);
			result.setInstructions(instructions);
			result.setServerInfo(serverInfo);
			result.setProtocolVersion(ProtocolVersion.getLatest());
			
			return result;
		});
		
		addNotificationHandler(InitializedNotification.METHOD_NOTIFICATION_INITIALIZED, notification -> {});
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
