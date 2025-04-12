package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class JSONRPCResult extends JSONRPCMessage {

	@JsonbProperty
	private RequestId id;
	
	@JsonbProperty
	private Object result;

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

	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}
}
