package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class ToolListChangedNotification extends BaseNotification implements ServerNotification {
	
	public static final String METHOD_NOTIFICATION_TOOLS_lIST_CHANGED = "notifications/tools/list_changed";

	/**
	 * 
	 */
	public ToolListChangedNotification() {
		super(METHOD_NOTIFICATION_TOOLS_lIST_CHANGED);
		//TODO Auto-generated constructor stub
	}
}
