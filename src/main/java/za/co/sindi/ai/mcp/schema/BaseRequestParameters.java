package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public abstract class BaseRequestParameters implements Serializable {

	@JsonbProperty("_meta")
	private RequestMeta meta;

	/**
	 * @return the meta
	 */
	public RequestMeta getMeta() {
		return meta;
	}

	/**
	 * @param meta the meta to set
	 */
	public void setMeta(RequestMeta meta) {
		this.meta = meta;
	}
}
