package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ResourceListChangedNotification extends BaseNotification implements ServerNotification {
	
	public static final String METHOD_NOTIFICATION_RESOURCES_lIST_CHANGED = "notifications/resources/list_changed";

	/**
	 * 
	 */
	public ResourceListChangedNotification() {
		super(METHOD_NOTIFICATION_RESOURCES_lIST_CHANGED);
		//TODO Auto-generated constructor stub
	}
}
