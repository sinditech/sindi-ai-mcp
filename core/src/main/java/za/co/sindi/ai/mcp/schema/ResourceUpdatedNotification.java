package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.ResourceUpdatedNotification.ResourceUpdatedNotificationParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ResourceUpdatedNotification extends BaseNotification implements ParameterBasedNotification<ResourceUpdatedNotificationParameters>, ServerNotification {
	
	public static final String METHOD_RESOURCES_UPDATED = "notifications/resources/updated";
	
	@JsonbProperty("params")
	private ResourceUpdatedNotificationParameters parameters;

	/**
	 * @return the parameters
	 */
	public ResourceUpdatedNotificationParameters getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(ResourceUpdatedNotificationParameters parameters) {
		this.parameters = parameters;
	}

	public static final class ResourceUpdatedNotificationParameters extends BaseNotificationParameters {
		
		@JsonbProperty
		private String uri;

		/**
		 * @return the uri
		 */
		public String getUri() {
			return uri;
		}

		/**
		 * @param uri the uri to set
		 */
		public void setUri(String uri) {
			this.uri = uri;
		}
	}
}
