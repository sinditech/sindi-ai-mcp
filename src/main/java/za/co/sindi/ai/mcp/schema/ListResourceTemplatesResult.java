package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ListResourceTemplatesResult extends PaginatedResult implements ServerResult {

	@JsonbProperty
	private ResourceTemplate[] resourceTemplates;

	/**
	 * @return the resourceTemplates
	 */
	public ResourceTemplate[] getResourceTemplates() {
		return resourceTemplates;
	}

	/**
	 * @param resourceTemplates the resourceTemplates to set
	 */
	public void setResourceTemplates(ResourceTemplate[] resourceTemplates) {
		this.resourceTemplates = resourceTemplates;
	}
}
