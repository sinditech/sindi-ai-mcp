package za.co.sindi.ai.mcp.shared;

import za.co.sindi.ai.mcp.schema.Request;
import za.co.sindi.ai.mcp.schema.Result;

/**
 * @author Buhake Sindi
 * @since 13 March 2025
 */
@FunctionalInterface
public interface RequestHandler<T extends Result> {

	public T handle(Request request);
}
