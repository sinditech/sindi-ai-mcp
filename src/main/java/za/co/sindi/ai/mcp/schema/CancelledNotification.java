package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.CancelledNotification.CancelledNotificationParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class CancelledNotification extends BaseNotification implements ParameterBasedNotification<CancelledNotificationParameters>, ClientNotification, ServerNotification {
	
	public static final String METHOD_NOTIFICATION_CANCELLED = "notifications/cancelled";
	
	@JsonbProperty("params")
	private CancelledNotificationParameters parameters;

	/**
	 * @return the parameters
	 */
	public CancelledNotificationParameters getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(CancelledNotificationParameters parameters) {
		this.parameters = parameters;
	}

	public static final class CancelledNotificationParameters extends BaseNotificationParameters {
		
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
