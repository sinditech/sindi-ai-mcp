package za.co.sindi.ai.mcp.schema;

import java.util.Objects;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public abstract class BaseRequest extends JSONRPCRequest {

	/**
	 * @param method
	 */
	protected BaseRequest(String method) {
		super();
		setMethod(Objects.requireNonNull(method, "A request method is required."));
	}
}
