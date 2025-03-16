package za.co.sindi.ai.mcp.shared;

import java.util.Objects;

import za.co.sindi.ai.mcp.schema.ClientCapabilities;
import za.co.sindi.ai.mcp.schema.ClientNotification;
import za.co.sindi.ai.mcp.schema.ClientRequest;
import za.co.sindi.ai.mcp.schema.ClientResult;
import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.ServerCapabilities;

/**
 * @author Buhake Sindi
 * @since 15 February 2025
 */
public abstract class Client extends Protocol<ClientTransport, ClientRequest, ClientNotification, ClientResult> {

	protected ClientCapabilities clientCapabilities;
	protected Implementation clientInfo;
	protected ServerCapabilities serverCapabilities;
	protected Implementation serverInfo;
	protected String instructions;

	/**
	 * @param transport
	 * @param clientCapabilities
	 * @param clientInfo
	 */
	protected Client(ClientTransport transport, ClientCapabilities clientCapabilities, Implementation clientInfo) {
		super(transport);
		this.clientCapabilities = Objects.requireNonNull(clientCapabilities, "A client capabilities is required.");
		this.clientInfo = Objects.requireNonNull(clientInfo, "A client info is required.");
	}

	/**
	 * @return the clientCapabilities
	 */
	public ClientCapabilities getClientCapabilities() {
		return clientCapabilities;
	}

	/**
	 * @return the clientInfo
	 */
	public Implementation getClientInfo() {
		return clientInfo;
	}

	/**
	 * @return the serverCapabilities
	 */
	public ServerCapabilities getServerCapabilities() {
		return serverCapabilities;
	}

	/**
	 * @return the serverInfo
	 */
	public Implementation getServerInfo() {
		return serverInfo;
	}

	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return instructions;
	}
}
