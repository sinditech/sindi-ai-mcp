package za.co.sindi.ai.mcp.schema;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.ElicitationCompleteNotification.ElicitationCompleteNotificationParameters;

/**
 * @author Buhake Sindi
 * @since 06 January 2026
 */
public final class ElicitationCompleteNotification extends BaseNotification implements ParameterBasedNotification<ElicitationCompleteNotificationParameters>, ServerNotification {
	
	public static final String METHOD_NOTIFICATION_LOGGING_MESSAGE = "notifications/message";
	
	@JsonbProperty("params")
	private ElicitationCompleteNotificationParameters parameters;
	
	/**
	 * @param parameters
	 */
	@JsonbCreator
	public ElicitationCompleteNotification(@JsonbProperty("params") ElicitationCompleteNotificationParameters parameters) {
		super();
		this.parameters = Objects.requireNonNull(parameters, "An elicitation notification parameters are reuired.");
	}

	/**
	 * @return the parameters
	 */
	public ElicitationCompleteNotificationParameters getParameters() {
		return parameters;
	}

	public static final class ElicitationCompleteNotificationParameters extends BaseNotificationParameters {
		
		private String elicitationId;

		/**
		 * @param elicitationId
		 */
		public ElicitationCompleteNotificationParameters(@JsonbProperty("elicitationId") String elicitationId) {
			super();
			this.elicitationId = Objects.requireNonNull(elicitationId, "An elicitation ID is required.");
		}

		/**
		 * @return the elicitationId
		 */
		public String getElicitationId() {
			return elicitationId;
		}
	}
}
