package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public abstract sealed class JSONRPCResponse extends JSONRPCMessage permits JSONRPCError, JSONRPCResult {

	@JsonbProperty
	private RequestId id;
	
	/**
	 * @return the id
	 */
	public RequestId getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(RequestId id) {
		this.id = id;
	}
}
