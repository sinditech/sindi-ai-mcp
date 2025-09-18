package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public abstract class PaginatedResult implements Result {
	
	@JsonbProperty
	private Cursor nextCursor;

	/**
	 * @return the nextCursor
	 */
	public Cursor getNextCursor() {
		return nextCursor;
	}

	/**
	 * @param nextCursor the nextCursor to set
	 */
	public void setNextCursor(Cursor nextCursor) {
		this.nextCursor = nextCursor;
	}
}
