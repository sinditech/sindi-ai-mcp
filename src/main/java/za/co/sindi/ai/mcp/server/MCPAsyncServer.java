package za.co.sindi.ai.mcp.server;

import java.util.concurrent.CompletableFuture;

import za.co.sindi.ai.mcp.schema.CallToolResult;
import za.co.sindi.ai.mcp.schema.CreateMessageRequest.CreateMessageRequestParameters;
import za.co.sindi.ai.mcp.schema.CreateMessageResult;
import za.co.sindi.ai.mcp.schema.GetPromptResult;
import za.co.sindi.ai.mcp.schema.ListResourceTemplatesResult;
import za.co.sindi.ai.mcp.schema.Prompt;
import za.co.sindi.ai.mcp.schema.PromptArgument;
import za.co.sindi.ai.mcp.schema.ReadResourceResult;
import za.co.sindi.ai.mcp.schema.Resource;
import za.co.sindi.ai.mcp.schema.ResourceTemplate;
import za.co.sindi.ai.mcp.schema.Root;
import za.co.sindi.ai.mcp.schema.Tool;
import za.co.sindi.ai.mcp.schema.Tool.InputSchema;
import za.co.sindi.ai.mcp.shared.RequestHandler;

/**
 * @author Buhake Sindi
 * @since 14 March 2025
 */
public interface MCPAsyncServer {
	
	public CompletableFuture<Void> connectAsync();

	public CompletableFuture<Void> pingAsync();
	
	public CompletableFuture<CreateMessageResult> createMessageAsync(final CreateMessageRequestParameters parameters);
	
	public CompletableFuture<Root[]> listRootsAsync();
	
	default CompletableFuture<Void> addToolAsync(final String name, final String description, final InputSchema inputSchema, final RequestHandler<CallToolResult> handler) {
		Tool tool = new Tool();
		tool.setDescription(description);
		tool.setInputSchema(inputSchema);
		tool.setName(name);
		
		return addToolAsync(tool, handler);
	}
	
	public CompletableFuture<Void> addToolAsync(final Tool tool, final RequestHandler<CallToolResult> handler);
	public CompletableFuture<Void> removeToolAsync(final String toolName);
	
	default CompletableFuture<Void> addPromptAsync(final String name, final String description, final PromptArgument[] arguments, final RequestHandler<GetPromptResult> promptProvider) {
		Prompt prompt = new Prompt();
		prompt.setDescription(description);
		prompt.setName(name);
		prompt.setArguments(arguments);
		
		return addPromptAsync(prompt, promptProvider);
	}
	
	public CompletableFuture<Void> addPromptAsync(final Prompt prompt, final RequestHandler<GetPromptResult> promptProvider);
	public CompletableFuture<Void> removePromptAsync(final String promptName);
	
	default CompletableFuture<Void> addResourceAsync(final String uri, final String name, final String description, final RequestHandler<ReadResourceResult> readHandler) {
		return addResourceAsync(uri, name, description, "text/html", readHandler);
	}
	
	default CompletableFuture<Void> addResourceAsync(final String uri, final String name, final String description, final String mimeType, final RequestHandler<ReadResourceResult> readHandler) {
		Resource resource = new Resource();
		resource.setDescription(description);
		resource.setMimeType(mimeType);
		resource.setName(name);
		resource.setUri(uri);
		
		return addResourceAsync(resource, readHandler);
	}
	
	public CompletableFuture<Void> addResourceAsync(final Resource resource, final RequestHandler<ReadResourceResult> readHandler);
	public CompletableFuture<Void> removeResourceAsync(final String uri);
	
	default CompletableFuture<Void> addResourceTemplateAsync(final String uriTemplate, final String name, final String description, final String mimeType, final RequestHandler<ListResourceTemplatesResult> readCallback) {
		ResourceTemplate resourceTemplate = new ResourceTemplate();
		resourceTemplate.setDescription(description);
		resourceTemplate.setMimeType(mimeType);
		resourceTemplate.setName(name);
		resourceTemplate.setUriTemplate(uriTemplate);
		
		return addResourceTemplateAsync(resourceTemplate, readCallback);
	}
	
	public CompletableFuture<Void> addResourceTemplateAsync(final ResourceTemplate resourceTemplate, final RequestHandler<ListResourceTemplatesResult> readCallback);
	public CompletableFuture<Void> removeResourceTemplateAsync(final String uriTemplate);
}
