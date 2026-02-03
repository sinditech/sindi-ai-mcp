/**
 * 
 */
package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 04 January 2026
 */
public class Icon implements Serializable {

	@JsonbProperty
	private String src;
	
	@JsonbProperty
	private String mimeType;
	
	@JsonbProperty
	private String[] sizes;
	
	@JsonbProperty
	private Theme theme;
	
	/**
	 * 
	 */
	public Icon() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param src
	 * @param mimeType
	 * @param sizes
	 * @param theme
	 */
	public Icon(String src, String mimeType, String[] sizes, Theme theme) {
		super();
		this.src = src;
		this.mimeType = mimeType;
		this.sizes = sizes;
		this.theme = theme;
	}

	/**
	 * @return the src
	 */
	public String getSrc() {
		return src;
	}

	/**
	 * @param src the src to set
	 */
	public void setSrc(String src) {
		this.src = src;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * @return the sizes
	 */
	public String[] getSizes() {
		return sizes;
	}

	/**
	 * @param sizes the sizes to set
	 */
	public void setSizes(String[] sizes) {
		this.sizes = sizes;
	}

	/**
	 * @return the theme
	 */
	public Theme getTheme() {
		return theme;
	}

	/**
	 * @param theme the theme to set
	 */
	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	public static enum Theme {
		LIGHT("light"),
		DARK("dark")
		;
		private final String theme;

		/**
		 * @param theme
		 */
		private Theme(final String theme) {
			this.theme = theme;
		}
		
		public static Theme of(final String value) {
			for (Theme theme : values()) {
				if (theme.theme.equals(value)) return theme;
			}
			
			throw new IllegalArgumentException("Invalid image theme '" + value + "'.");
		}

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return theme;
		}
	}
}
