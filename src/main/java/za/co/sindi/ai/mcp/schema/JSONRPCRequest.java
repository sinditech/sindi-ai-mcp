package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public class JSONRPCRequest extends JSONRPCMessage implements Request {

	@JsonbProperty
	private long id;
	
	@JsonbProperty
	private Object params;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the params
	 */
	public Object getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Object params) {
		this.params = params;
	}
}
