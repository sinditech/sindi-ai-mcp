package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class RootsListChangedNotification extends BaseNotification implements ClientNotification {
	
	public static final String METHOD_NOTIFICATION_ROOTS_lIST_CHANGED = "notifications/roots/list_changed";

	/**
	 * 
	 */
	public RootsListChangedNotification() {
		super(METHOD_NOTIFICATION_ROOTS_lIST_CHANGED);
		//TODO Auto-generated constructor stub
	}
}
