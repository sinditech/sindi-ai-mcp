package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ListResourcesResult extends PaginatedResult implements ServerResult {

	@JsonbProperty
	private Resource[] resources;

	/**
	 * @return the resources
	 */
	public Resource[] getResources() {
		return resources;
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(Resource[] resources) {
		this.resources = resources;
	}
}
