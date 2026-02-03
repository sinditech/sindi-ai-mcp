package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class CreateMessageResult extends SamplingMessage implements ClientResult {
	
	@JsonbProperty
	private String model;
	
	@JsonbProperty
	private String stopReason;

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the stopReason
	 */
	public String getStopReason() {
		return stopReason;
	}

	/**
	 * @param stopReason the stopReason to set
	 */
	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}
}
