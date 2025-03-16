package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class InitializeResult implements ServerResult {
	
	@JsonbProperty
	private ProtocolVersion protocolVersion;
	
	@JsonbProperty
	private ServerCapabilities capabilities;
	
	@JsonbProperty
	private Implementation serverInfo;
	
	@JsonbProperty
	private String instructions;

	/**
	 * @return the protocolVersion
	 */
	public ProtocolVersion getProtocolVersion() {
		return protocolVersion;
	}

	/**
	 * @param protocolVersion the protocolVersion to set
	 */
	public void setProtocolVersion(ProtocolVersion protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	/**
	 * @return the capabilities
	 */
	public ServerCapabilities getCapabilities() {
		return capabilities;
	}

	/**
	 * @param capabilities the capabilities to set
	 */
	public void setCapabilities(ServerCapabilities capabilities) {
		this.capabilities = capabilities;
	}

	/**
	 * @return the serverInfo
	 */
	public Implementation getServerInfo() {
		return serverInfo;
	}

	/**
	 * @param serverInfo the serverInfo to set
	 */
	public void setServerInfo(Implementation serverInfo) {
		this.serverInfo = serverInfo;
	}

	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return instructions;
	}

	/**
	 * @param instructions the instructions to set
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
}
