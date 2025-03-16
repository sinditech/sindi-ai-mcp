package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public abstract class PaginatedResult implements Result {
	
	@JsonbProperty
	private String nextCursor;

	/**
	 * @return the nextCursor
	 */
	public String getNextCursor() {
		return nextCursor;
	}

	/**
	 * @param nextCursor the nextCursor to set
	 */
	public void setNextCursor(String nextCursor) {
		this.nextCursor = nextCursor;
	}
}
