package za.co.sindi.ai.mcp.schema;

import java.util.Objects;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public abstract class BaseNotification extends JSONRPCNotification {

	/**
	 * @param method
	 */
	protected BaseNotification(String method) {
		super();
		setMethod(Objects.requireNonNull(method, "A notification method is required."));
	}
}
