/**
 * 
 */
package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 06 September 2025
 */
public final class StringSchema extends PrimitiveSchemaDefinition {

	@JsonbProperty
	private Integer minLength;
	
	@JsonbProperty
	private Integer maxLength;
	
	@JsonbProperty
	private Format format;
	
	/**
	 * 
	 */
	public StringSchema() {
		super();
		// TODO Auto-generated constructor stub
		setType("string");
	}

	/**
	 * @return the minLength
	 */
	public Integer getMinLength() {
		return minLength;
	}

	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	/**
	 * @return the maxLength
	 */
	public Integer getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return the format
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(Format format) {
		this.format = format;
	}

	public static enum Format {
		EMAIL("email")
		,URI("uri")
		,DATE("date")
		,DATE_TIME("date-time")
		;
		private final String format;

		/**
		 * @param role
		 */
		private Format(final String format) {
			this.format = format;
		}
		
		public static Format of(final String value) {
			for (Format format : values()) {
				if (format.format.equals(value)) return format;
			}
			
			throw new IllegalArgumentException("Unrecognised String format '" + value + "'.");
		}

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return format;
		}
	}
}
