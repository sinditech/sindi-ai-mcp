/**
 * 
 */
package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 04 January 2026
 */
public enum TaskStatus {

	;
	private final String status;

	/**
	 * @param status
	 */
	private TaskStatus(String status) {
		this.status = status;
	}
	
	public static TaskStatus of(final String value) {
		for (TaskStatus status : values()) {
			if (status.status.equals(value)) return status;
		}
		
		throw new IllegalArgumentException("Unrecognised task status '" + value + "'.");
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return status;
	}
}
