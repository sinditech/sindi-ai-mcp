package za.co.sindi.ai.mcp.schema;

import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public class Prompt extends BaseMetadata implements BaseIcons {

	@JsonbProperty
	private String description;
	
	@JsonbProperty
	private PromptArgument[] arguments;
	
	@JsonbProperty("_meta")
	private Map<String, Object> meta;
	
	@JsonbProperty
	private Icon[] icons;

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the arguments
	 */
	public PromptArgument[] getArguments() {
		return arguments;
	}

	/**
	 * @param arguments the arguments to set
	 */
	public void setArguments(PromptArgument[] arguments) {
		this.arguments = arguments;
	}

	/**
	 * @return the meta
	 */
	public Map<String, Object> getMeta() {
		return meta;
	}

	/**
	 * @param meta the meta to set
	 */
	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}

	/**
	 * @return the icons
	 */
	public Icon[] getIcons() {
		return icons;
	}

	/**
	 * @param icons the icons to set
	 */
	public void setIcons(Icon[] icons) {
		this.icons = icons;
	}
}
