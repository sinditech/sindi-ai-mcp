package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public enum LoggingLevel {
	DEBUG("debug")
	,INFO("info")
	,NOTICE("notice")
	,WARNING("warning")
	,ERROR("error")
	,CRITICAL("critical")
	,ALERT("alert")
	,EMERGENCY("emergency")
	;
	private final String level;

	/**
	 * @param role
	 */
	private LoggingLevel(final String level) {
		this.level = level;
	}
	
	public static LoggingLevel of(final String value) {
		for (LoggingLevel level : values()) {
			if (level.level.equals(value)) return level;
		}
		
		throw new IllegalArgumentException("Unrecognised Logging level '" + value + "'.");
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return level;
	}
}
