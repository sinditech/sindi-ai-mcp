package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.ProgressNotification.ProgressNotificationParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ProgressNotification extends BaseNotification implements ParameterBasedNotification<ProgressNotificationParameters>, ClientNotification, ServerNotification {
	
	public static final String METHOD_NOTIFICATION_PROGRESS = "notifications/progress";
	
	@JsonbProperty("params")
	private ProgressNotificationParameters parameters;
	
	/**
	 * @return the parameters
	 */
	public ProgressNotificationParameters getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(ProgressNotificationParameters parameters) {
		this.parameters = parameters;
	}

	public static final class ProgressNotificationParameters extends BaseNotificationParameters {
		
		@JsonbProperty
		private ProgressToken progressToken;
		
		@JsonbProperty
		private long progress;
		
		@JsonbProperty
		private Long total;
		
		@JsonbProperty
		private String message;

		/**
		 * @return the progressToken
		 */
		public ProgressToken getProgressToken() {
			return progressToken;
		}

		/**
		 * @param progressToken the progressToken to set
		 */
		public void setProgressToken(ProgressToken progressToken) {
			this.progressToken = progressToken;
		}

		/**
		 * @return the progress
		 */
		public long getProgress() {
			return progress;
		}

		/**
		 * @param progress the progress to set
		 */
		public void setProgress(long progress) {
			this.progress = progress;
		}

		/**
		 * @return the total
		 */
		public Long getTotal() {
			return total;
		}

		/**
		 * @param total the total to set
		 */
		public void setTotal(Long total) {
			this.total = total;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @param message the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}
	}
}
