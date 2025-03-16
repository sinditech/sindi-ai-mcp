package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ReadResourceResult implements ServerResult {
	
	@JsonbProperty
	private ResourceContents[] contents;

	/**
	 * @return the contents
	 */
	public ResourceContents[] getContents() {
		return contents;
	}

	/**
	 * @param contents the contents to set
	 */
	public void setContents(ResourceContents[] contents) {
		this.contents = contents;
	}
}
