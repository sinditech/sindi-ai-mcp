package za.co.sindi.ai.mcp.schema;

import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 05 January 2026
 */
public final class ToolUseContent implements SamplingMessageContentBlock {

	@JsonbProperty
	private String id;
	
	@JsonbProperty
	private String name;

	@JsonbProperty
	private Map<String, Object> input;
	
	@JsonbProperty("_meta")
	private Map<String, Object> meta;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the input
	 */
	public Map<String, Object> getInput() {
		return input;
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(Map<String, Object> input) {
		this.input = input;
	}
}
