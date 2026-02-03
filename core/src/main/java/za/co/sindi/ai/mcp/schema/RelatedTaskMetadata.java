package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 04 January 2026
 */
public class RelatedTaskMetadata implements Serializable {
	
	@JsonbProperty
	private String taskId;

	/**
	 * @param taskId
	 */
	public RelatedTaskMetadata(String taskId) {
		super();
		this.taskId = Objects.requireNonNull(taskId, "A task ID is required.");
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}
}
