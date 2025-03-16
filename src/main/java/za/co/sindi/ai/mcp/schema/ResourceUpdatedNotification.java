package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.ResourceUpdatedNotification.ResourceUpdatedNotificationParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ResourceUpdatedNotification extends BaseNotification implements ParameterBasedNotification<ResourceUpdatedNotificationParameters>, ServerNotification {
	
	public static final String METHOD_RESOURCES_UPDATED = "notifications/resources/updated";
	
	/**
	 * 
	 */
	public ResourceUpdatedNotification() {
		super(METHOD_RESOURCES_UPDATED);
		//TODO Auto-generated constructor stub
	}
	
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

	public static final class ResourceUpdatedNotificationParameters implements Serializable {
		
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
