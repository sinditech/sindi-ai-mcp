package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public enum ProtocolVersion {
	LATEST_PROTOCOL_VERSION(Schema.LATEST_PROTOCOL_VERSION)
	,VERSION_2025_03_26("2025-03-26")
	,VERSION_2024_11_05("2024-11-05")
	,VERSION_2024_10_07("2024-10-07")
	;
	private final String version;

	/**
	 * @param version
	 */
	private ProtocolVersion(final String version) {
		this.version = version;
	}
	
	public static ProtocolVersion getLatest() {
		return LATEST_PROTOCOL_VERSION;
	}
	
	public static ProtocolVersion of(final String value) {
		for (ProtocolVersion version : values()) {
			if (version.version.equals(value)) return version;
		}
		
		throw new IllegalArgumentException("Invalid protocol version '" + value + "'.");
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
