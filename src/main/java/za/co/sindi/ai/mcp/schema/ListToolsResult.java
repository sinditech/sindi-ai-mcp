package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ListToolsResult extends PaginatedResult implements ServerResult {

	@JsonbProperty
	private Tool[] tools;

	/**
	 * @return the tools
	 */
	public Tool[] getTools() {
		return tools;
	}

	/**
	 * @param tools the tools to set
	 */
	public void setTools(Tool[] tools) {
		this.tools = tools;
	}
}
