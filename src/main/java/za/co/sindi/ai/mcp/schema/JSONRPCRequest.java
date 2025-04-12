package za.co.sindi.ai.mcp.schema;

import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.shared.RequestId;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class JSONRPCRequest extends JSONRPCMessage implements Request {

	@JsonbProperty
	private RequestId id;
	
	@JsonbProperty
	private String method;
	
	@JsonbProperty
	private Map<String, Object> params;

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

	/**
	 * @return the params
	 */
	public Map<String, Object> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
