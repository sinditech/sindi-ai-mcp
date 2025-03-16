package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public abstract class JSONRPCMessage implements Serializable {

	@JsonbProperty
	private JSONRPCVersion jsonrpc;
	
	@JsonbProperty
	private String method;

	/**
	 * 
	 */
	protected JSONRPCMessage() {
		super();
		//TODO Auto-generated constructor stub
		jsonrpc = JSONRPCVersion.VERSION_2_0;
	}

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

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
}
