package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public sealed abstract class JSONRPCMessage implements Serializable permits JSONRPCRequest, JSONRPCNotification, JSONRPCResponse {

	@JsonbProperty
	private JSONRPCVersion jsonrpc;

	/**
	 * @return the jsonrpc
	 */
	public JSONRPCVersion getJsonrpc() {
		return jsonrpc;
	}

	/**
	 * @param jsonrpc the jsonrpc to set
	 */
	public void setJsonrpc(JSONRPCVersion jsonrpc) {
		this.jsonrpc = jsonrpc;
	}
}
