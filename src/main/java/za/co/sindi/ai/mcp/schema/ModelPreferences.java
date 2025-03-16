package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public class ModelPreferences implements Serializable {

	@JsonbProperty
	private ModelHint[] hints;
	
	@JsonbProperty
	private Double costPriority;
	
	@JsonbProperty
	private Double speedPriority;
	
	@JsonbProperty
	private Double intelligencePriority;

	/**
	 * @return the hints
	 */
	public ModelHint[] getHints() {
		return hints;
	}

	/**
	 * @param hints the hints to set
	 */
	public void setHints(ModelHint[] hints) {
		this.hints = hints;
	}

	/**
	 * @return the costPriority
	 */
	public Double getCostPriority() {
		return costPriority;
	}

	/**
	 * @param costPriority the costPriority to set
	 */
	public void setCostPriority(Double costPriority) {
		this.costPriority = costPriority;
	}

	/**
	 * @return the speedPriority
	 */
	public Double getSpeedPriority() {
		return speedPriority;
	}

	/**
	 * @param speedPriority the speedPriority to set
	 */
	public void setSpeedPriority(Double speedPriority) {
		this.speedPriority = speedPriority;
	}

	/**
	 * @return the intelligencePriority
	 */
	public Double getIntelligencePriority() {
		return intelligencePriority;
	}

	/**
	 * @param intelligencePriority the intelligencePriority to set
	 */
	public void setIntelligencePriority(Double intelligencePriority) {
		this.intelligencePriority = intelligencePriority;
	}
}
