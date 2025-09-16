package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class EmbeddedResource extends ContentBlock {

	@JsonbProperty
	private ResourceContents resource;

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
}
