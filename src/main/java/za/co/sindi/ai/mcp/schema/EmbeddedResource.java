package za.co.sindi.ai.mcp.schema;

import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class EmbeddedResource extends ContentBlock {

	@JsonbProperty
	private ResourceContents resource;
	
	@JsonbProperty("_meta")
	private Map<String, Object> meta;

	/**
	 * @return the resource
	 */
	public ResourceContents getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(ResourceContents resource) {
		this.resource = resource;
	}

	/**
	 * @return the meta
	 */
	public Map<String, Object> getMeta() {
		return meta;
	}

	/**
	 * @param meta the meta to set
	 */
	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}
}
