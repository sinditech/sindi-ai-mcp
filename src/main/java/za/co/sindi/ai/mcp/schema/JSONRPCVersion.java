package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public enum JSONRPCVersion {
	VERSION_2_0(Schema.JSONRPC_VERSION)
	;
	private final String version;

	/**
	 * @param version
	 */
	private JSONRPCVersion(final String version) {
		this.version = version;
	}
	
	public static JSONRPCVersion getLatest() {
		return VERSION_2_0;
	}
	
	public static JSONRPCVersion of(final String value) {
		for (JSONRPCVersion version : values()) {
			if (version.version.equals(value)) return version;
		}
		
		throw new IllegalArgumentException("Invalid JSONRPCVersion version '" + value + "'.");
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return version;
	}
}
