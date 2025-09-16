package za.co.sindi.ai.mcp.client;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import za.co.sindi.ai.mcp.schema.ClientCapabilities;
import za.co.sindi.ai.mcp.schema.ClientNotification;
import za.co.sindi.ai.mcp.schema.ClientRequest;
import za.co.sindi.ai.mcp.schema.ClientResult;
import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.RootsListChangedNotification;
import za.co.sindi.ai.mcp.schema.ServerCapabilities;
import za.co.sindi.ai.mcp.shared.ClientTransport;
import za.co.sindi.ai.mcp.shared.Protocol;

/**
 * @author Buhake Sindi
 * @since 15 February 2025
 */
public abstract class Client extends Protocol<ClientTransport, ClientRequest, ClientNotification, ClientResult> {

	protected ClientCapabilities capabilities;
	protected Implementation clientInfo;
	protected ServerCapabilities serverCapabilities;
	protected Implementation serverInfo;
	protected String instructions;

	/**
	 * @param capabilities
	 * @param clientInfo
	 */
	protected Client(ClientCapabilities capabilities, Implementation clientInfo) {
		super();
		this.capabilities = Objects.requireNonNull(capabilities, "A client capabilities is required.");
		this.clientInfo = Objects.requireNonNull(clientInfo, "A client info is required.");
	}

	/**
	 * @return the capabilities
	 */
	public ClientCapabilities getCapabilities() {
		return capabilities;
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
	
	protected CompletableFuture<Void> sendRootListChangedAsync() {
		if (Boolean.TRUE.equals(getCapabilities().getRoots().getListChanged())) {
			return sendNotification(new RootsListChangedNotification());
		}
		
		return CompletableFuture.completedFuture(null);
	}
}
