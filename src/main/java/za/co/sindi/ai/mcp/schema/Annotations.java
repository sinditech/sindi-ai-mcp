package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;
import java.time.OffsetDateTime;

import jakarta.json.bind.annotation.JsonbProperty;

public class Annotations implements Serializable {
	
	@JsonbProperty
	private Role[] audience;
	
	@JsonbProperty
	private Double priority;
	
	@JsonbProperty
	private OffsetDateTime lastModified;

	/**
	 * @return the audience
	 */
	public Role[] getAudience() {
		return audience;
	}

	/**
	 * @param audience the audience to set
	 */
	public void setAudience(Role[] audience) {
		this.audience = audience;
	}

	/**
	 * @return the priority
	 */
	public Double getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Double priority) {
		this.priority = priority;
	}

	/**
	 * @return the lastModified
	 */
	public OffsetDateTime getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(OffsetDateTime lastModified) {
		this.lastModified = lastModified;
	}
}