package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ListTasksResult extends PaginatedResult implements ClientResult, ServerResult {

	@JsonbProperty
	private Task[] tasks;

	/**
	 * @return the tasks
	 */
	public Task[] getTasks() {
		return tasks;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(Task[] tasks) {
		this.tasks = tasks;
	}
}
