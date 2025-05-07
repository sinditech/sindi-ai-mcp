package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class TextContent extends Content {

	@JsonbProperty
	private String text;

	/**
	 * 
	 */
	public TextContent() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 */
	public TextContent(String text) {
		super();
		this.text = text;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
}
