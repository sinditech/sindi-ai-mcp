package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

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
	 * 
	 */
	public CancelledNotification() {
		super(METHOD_NOTIFICATION_CANCELLED);
		//TODO Auto-generated constructor stub
	}
	
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

	public static final class CancelledNotificationParameters implements Serializable {
		
		@JsonbProperty
		private long requestId;
		
		@JsonbProperty
		private String reason;

		/**
		 * @return the requestId
		 */
		public long getRequestId() {
			return requestId;
		}

		/**
		 * @param requestId the requestId to set
		 */
		public void setRequestId(long requestId) {
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
