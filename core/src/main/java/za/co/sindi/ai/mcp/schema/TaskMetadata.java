package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 04 January 2026
 */
public class TaskMetadata implements Serializable {
	
	@JsonbProperty
	private Long ttl;

	/**
	 * @return the ttl
	 */
	public Long getTtl() {
		return ttl;
	}

	/**
	 * @param ttl the ttl to set
	 */
	public void setTtl(Long ttl) {
		this.ttl = ttl;
	}
}
