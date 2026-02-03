package za.co.sindi.ai.mcp.schema;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.CancelTaskRequest.CancelTaskequestParameters;

/**
 * @author Buhake Sindi
 * @since 05 January 2026
 */
public final class CancelTaskRequest extends BaseRequest implements ParameterBasedRequest<CancelTaskequestParameters>, ClientRequest, ServerRequest {
	
	public static final String METHOD_TASKS_RESULT = "tasks/cancel";

	@JsonbProperty("params")
	private CancelTaskequestParameters parameters;

	/**
	 * @param parameters
	 */
	@JsonbCreator
	public CancelTaskRequest(@JsonbProperty("params") CancelTaskequestParameters parameters) {
		super();
		this.parameters = Objects.requireNonNull(parameters, "A cancel task request parameters is required.");
	}

	public CancelTaskequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	public static final class CancelTaskequestParameters extends BaseRequestParameters {
		
		private String taskId;

		/**
		 * @param taskId
		 */
		@JsonbCreator
		public CancelTaskequestParameters(@JsonbProperty("taskId") String taskId) {
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
