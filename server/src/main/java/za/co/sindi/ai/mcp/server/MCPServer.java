package za.co.sindi.ai.mcp.server;

import za.co.sindi.ai.mcp.schema.CallToolResult;
import za.co.sindi.ai.mcp.schema.GetPromptResult;
import za.co.sindi.ai.mcp.schema.ListResourceTemplatesResult;
import za.co.sindi.ai.mcp.schema.Prompt;
import za.co.sindi.ai.mcp.schema.PromptArgument;
import za.co.sindi.ai.mcp.schema.ReadResourceResult;
import za.co.sindi.ai.mcp.schema.Resource;
import za.co.sindi.ai.mcp.schema.ResourceTemplate;
import za.co.sindi.ai.mcp.schema.Tool;
import za.co.sindi.ai.mcp.schema.Tool.InputSchema;
import za.co.sindi.ai.mcp.shared.RequestHandler;

/**
 * @author Buhake Sindi
 * @since 14 March 2025
 */
public interface MCPServer extends AutoCloseable {
	
	public void connect();

//	public void ping();
//	
//	public CreateMessageResult createMessage(final CreateMessageRequestParameters parameters);
//	
//	public Root[] listRoots();
	
	default void addTool(final String name, final String description, final InputSchema inputSchema, final RequestHandler<CallToolResult> handler) {
		Tool tool = new Tool();
		tool.setDescription(description);
		tool.setInputSchema(inputSchema);
		tool.setName(name);
		
		addTool(tool, handler);
	}
	
	public void addTool(final Tool tool, final RequestHandler<CallToolResult> handler);
	public void removeTool(final String toolName);
	
	default void addPrompt(final String name, final String description, final PromptArgument[] arguments, final RequestHandler<GetPromptResult> promptProvider) {
		Prompt prompt = new Prompt();
		prompt.setDescription(description);
		prompt.setName(name);
		prompt.setArguments(arguments);
		
		addPrompt(prompt, promptProvider);
	}
	
	public void addPrompt(final Prompt prompt, final RequestHandler<GetPromptResult> promptProvider);
	public void removePrompt(final String promptName);
	
	default void addResource(final String uri, final String name, final String description, final RequestHandler<ReadResourceResult> readHandler) {
		addResource(uri, name, description, "text/html", readHandler);
	}
	
	default void addResource(final String uri, final String name, final String description, final String mimeType, final RequestHandler<ReadResourceResult> readHandler) {
		Resource resource = new Resource();
		resource.setDescription(description);
		resource.setMimeType(mimeType);
		resource.setName(name);
		resource.setUri(uri);
		
		addResource(resource, readHandler);
	}
	
	public void addResource(final Resource resource, final RequestHandler<ReadResourceResult> readHandler);
	public void removeResource(final String uri);
	
	default void addResourceTemplate(final String uriTemplate, final String name, final String description, final String mimeType, final RequestHandler<ListResourceTemplatesResult> readCallback) {
		ResourceTemplate resourceTemplate = new ResourceTemplate();
		resourceTemplate.setDescription(description);
		resourceTemplate.setMimeType(mimeType);
		resourceTemplate.setName(name);
		resourceTemplate.setUriTemplate(uriTemplate);
		
		addResourceTemplate(resourceTemplate, readCallback);
	}
	
	public void addResourceTemplate(final ResourceTemplate resourceTemplate, final RequestHandler<ListResourceTemplatesResult> readCallback);
	public void removeResourceTemplate(final String uriTemplate);
}
