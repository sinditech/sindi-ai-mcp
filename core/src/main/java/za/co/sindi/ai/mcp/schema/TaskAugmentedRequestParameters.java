/**
 * 
 */
package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 05 January 2026
 */
public abstract class TaskAugmentedRequestParameters extends BaseRequestParameters {

	@JsonbProperty
	private TaskMetadata task;

	/**
	 * @return the task
	 */
	public TaskMetadata getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(TaskMetadata task) {
		this.task = task;
	}
}
