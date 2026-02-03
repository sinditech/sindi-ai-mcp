package za.co.sindi.ai.mcp.schema;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.GetTaskPayloadRequest.GetTaskPayloadRequestParameters;

/**
 * @author Buhake Sindi
 * @since 05 January 2026
 */
public final class GetTaskPayloadRequest extends BaseRequest implements ParameterBasedRequest<GetTaskPayloadRequestParameters>, ClientRequest, ServerRequest {
	
	public static final String METHOD_TASKS_RESULT = "tasks/result";

	@JsonbProperty("params")
	private GetTaskPayloadRequestParameters parameters;

	/**
	 * @param parameters
	 */
	@JsonbCreator
	public GetTaskPayloadRequest(@JsonbProperty("params") GetTaskPayloadRequestParameters parameters) {
		super();
		this.parameters = Objects.requireNonNull(parameters, "A Get task payload request parameters is required.");
	}

	public GetTaskPayloadRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	public static final class GetTaskPayloadRequestParameters extends BaseRequestParameters {
		
		private String taskId;

		/**
		 * @param taskId
		 */
		@JsonbCreator
		public GetTaskPayloadRequestParameters(@JsonbProperty("taskId") String taskId) {
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
