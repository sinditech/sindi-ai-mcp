package za.co.sindi.ai.mcp.schema;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.TaskStatusNotification.TaskStatusNotificationParameters;

/**
 * @author Buhake Sindi
 * @since 05 January 2026
 */
public final class TaskStatusNotification extends BaseNotification implements ParameterBasedNotification<TaskStatusNotificationParameters>, ClientNotification, ServerNotification {
	
	public static final String METHOD_NOTIFICATION_TASK_STATUS = "notifications/tasks/status";
	
	@JsonbProperty("params")
	private TaskStatusNotificationParameters parameters;

	/**
	 * @param parameters
	 */
	@JsonbCreator
	public TaskStatusNotification(@JsonbProperty("params") TaskStatusNotificationParameters parameters) {
		super();
		this.parameters = Objects.requireNonNull(parameters, "A cancellated notitification parameters are required.");
	}

	/**
	 * @return the parameters
	 */
	public TaskStatusNotificationParameters getParameters() {
		return parameters;
	}

	public static final class TaskStatusNotificationParameters extends BaseNotificationParameters {
		
		@JsonbProperty
		private RequestId requestId;
		
		@JsonbProperty
		private String reason;

		/**
		 * @return the requestId
		 */
		public RequestId getRequestId() {
			return requestId;
		}

		/**
		 * @param requestId the requestId to set
		 */
		public void setRequestId(RequestId requestId) {
			this.requestId = requestId;
		}

		/**
		 * @return the reason
		 */
		public String getReason() {
			return reason;
		}

		/**
		 * @param reason the reason to set
		 */
		public void setReason(String reason) {
			this.reason = reason;
		}
	}
}
