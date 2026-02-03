package za.co.sindi.ai.mcp.schema;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.GetTaskRequest.GetTaskRequestParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class GetTaskRequest extends BaseRequest implements ParameterBasedRequest<GetTaskRequestParameters>, ClientRequest, ServerRequest {
	
	public static final String METHOD_TASKS_GET = "tasks/get";

	@JsonbProperty("params")
	private GetTaskRequestParameters parameters;

	/**
	 * @param parameters
	 */
	@JsonbCreator
	public GetTaskRequest(@JsonbProperty("params") GetTaskRequestParameters parameters) {
		super();
		this.parameters = Objects.requireNonNull(parameters, "A Get task request parameters is required.");
	}

	public GetTaskRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	public static final class GetTaskRequestParameters extends BaseRequestParameters {
		
		private String taskId;

		/**
		 * @param taskId
		 */
		public GetTaskRequestParameters(@JsonbProperty("taskId") String taskId) {
			super();
			this.taskId = Objects.requireNonNull(taskId, "A Task ID is required.");
		}

		/**
		 * @return the taskId
		 */
		public String getTaskId() {
			return taskId;
		}
	}
}
