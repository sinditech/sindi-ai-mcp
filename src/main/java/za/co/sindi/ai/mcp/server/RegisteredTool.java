package za.co.sindi.ai.mcp.server;

import java.io.Serializable;
import java.util.Objects;

import za.co.sindi.ai.mcp.schema.CallToolResult;
import za.co.sindi.ai.mcp.schema.Tool;
import za.co.sindi.ai.mcp.shared.RequestHandler;

/**
 * @author Buhake Sindi
 * @since 15 March 2025
 */
public class RegisteredTool implements Serializable {

	private Tool tool;
	private RequestHandler<CallToolResult> handler;
	/**
	 * @param tool
	 * @param handler
	 */
	public RegisteredTool(Tool tool, RequestHandler<CallToolResult> handler) {
		super();
		this.tool = Objects.requireNonNull(tool, "A tool is required.");
		this.handler = Objects.requireNonNull(handler, "A client request handler is required.");
	}
	/**
	 * @return the tool
	 */
	public Tool getTool() {
		return tool;
	}
	
	/**
	 * @return the handler
	 */
	public RequestHandler<CallToolResult> getHandler() {
		return handler;
	}
}
