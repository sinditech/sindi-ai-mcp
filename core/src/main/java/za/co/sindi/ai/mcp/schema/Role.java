package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public enum Role {
	USER("user")
	,ASSISTANT("assistant")
	;
	private final String role;

	/**
	 * @param role
	 */
	private Role(final String role) {
		this.role = role;
	}
	
	public static Role of(final String value) {
		for (Role role : values()) {
			if (role.role.equals(value)) return role;
		}
		
		throw new IllegalArgumentException("Unrecognised role '" + value + "'.");
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return role;
	}
}
