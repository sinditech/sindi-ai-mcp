package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public interface ParameterBasedNotification<PARAM> extends Notification {

	public PARAM getParameters();
//	public Map<String, Object> toParametersMap();
}
