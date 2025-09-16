package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.LoggingMessageNotification.LoggingMessageNotificationParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class LoggingMessageNotification extends BaseNotification implements ParameterBasedNotification<LoggingMessageNotificationParameters>, ServerNotification {
	
	public static final String METHOD_NOTIFICATION_LOGGING_MESSAGE = "notifications/message";
	
	@JsonbProperty("params")
	private LoggingMessageNotificationParameters parameters;
	
	/**
	 * @return the parameters
	 */
	public LoggingMessageNotificationParameters getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(LoggingMessageNotificationParameters parameters) {
		this.parameters = parameters;
	}

	public static final class LoggingMessageNotificationParameters extends BaseNotificationParameters {
		
		@JsonbProperty
		private LoggingLevel level;
		
		@JsonbProperty
		private String logger;
		
		@JsonbProperty
		private Object data;

		/**
		 * @return the level
		 */
		public LoggingLevel getLevel() {
			return level;
		}

		/**
		 * @param level the level to set
		 */
		public void setLevel(LoggingLevel level) {
			this.level = level;
		}

		/**
		 * @return the logger
		 */
		public String getLogger() {
			return logger;
		}

		/**
		 * @param logger the logger to set
		 */
		public void setLogger(String logger) {
			this.logger = logger;
		}

		/**
		 * @return the data
		 */
		public Object getData() {
			return data;
		}

		/**
		 * @param data the data to set
		 */
		public void setData(Object data) {
			this.data = data;
		}
	}
}
