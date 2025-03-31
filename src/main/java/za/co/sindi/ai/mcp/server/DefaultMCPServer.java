package za.co.sindi.ai.mcp.server;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import za.co.sindi.ai.mcp.schema.CallToolRequest;
import za.co.sindi.ai.mcp.schema.CallToolResult;
import za.co.sindi.ai.mcp.schema.CreateMessageRequest;
import za.co.sindi.ai.mcp.schema.CreateMessageRequest.CreateMessageRequestParameters;
import za.co.sindi.ai.mcp.schema.CreateMessageResult;
import za.co.sindi.ai.mcp.schema.EmptyResult;
import za.co.sindi.ai.mcp.schema.ErrorCodes;
import za.co.sindi.ai.mcp.schema.GetPromptRequest;
import za.co.sindi.ai.mcp.schema.GetPromptResult;
import za.co.sindi.ai.mcp.schema.Implementation;
import za.co.sindi.ai.mcp.schema.ListPromptsRequest;
import za.co.sindi.ai.mcp.schema.ListPromptsResult;
import za.co.sindi.ai.mcp.schema.ListResourceTemplatesRequest;
import za.co.sindi.ai.mcp.schema.ListResourceTemplatesResult;
import za.co.sindi.ai.mcp.schema.ListResourcesRequest;
import za.co.sindi.ai.mcp.schema.ListResourcesResult;
import za.co.sindi.ai.mcp.schema.ListRootsRequest;
import za.co.sindi.ai.mcp.schema.ListRootsResult;
import za.co.sindi.ai.mcp.schema.ListToolsRequest;
import za.co.sindi.ai.mcp.schema.ListToolsResult;
import za.co.sindi.ai.mcp.schema.LoggingLevel;
import za.co.sindi.ai.mcp.schema.LoggingMessageNotification;
import za.co.sindi.ai.mcp.schema.LoggingMessageNotification.LoggingMessageNotificationParameters;
import za.co.sindi.ai.mcp.schema.PingRequest;
import za.co.sindi.ai.mcp.schema.Prompt;
import za.co.sindi.ai.mcp.schema.PromptListChangedNotification;
import za.co.sindi.ai.mcp.schema.ReadResourceRequest;
import za.co.sindi.ai.mcp.schema.ReadResourceResult;
import za.co.sindi.ai.mcp.schema.Resource;
import za.co.sindi.ai.mcp.schema.ResourceListChangedNotification;
import za.co.sindi.ai.mcp.schema.ResourceTemplate;
import za.co.sindi.ai.mcp.schema.ResourceUpdatedNotification;
import za.co.sindi.ai.mcp.schema.ResourceUpdatedNotification.ResourceUpdatedNotificationParameters;
import za.co.sindi.ai.mcp.schema.Root;
import za.co.sindi.ai.mcp.schema.Schema;
import za.co.sindi.ai.mcp.schema.ServerCapabilities;
import za.co.sindi.ai.mcp.schema.SetLevelRequest;
import za.co.sindi.ai.mcp.schema.Tool;
import za.co.sindi.ai.mcp.schema.ToolListChangedNotification;
import za.co.sindi.ai.mcp.shared.MCPError;
import za.co.sindi.ai.mcp.shared.RequestHandler;
import za.co.sindi.ai.mcp.shared.ServerTransport;
import za.co.sindi.commons.utils.Preconditions;
import za.co.sindi.commons.utils.Strings;

/**
 * @author Buhake Sindi
 * @since 14 March 2025
 */
public class DefaultMCPServer implements MCPServer, MCPAsyncServer {
	
	private static final Logger LOGGER = Logger.getLogger(DefaultMCPServer.class.getName());
	
	private final ConcurrentHashMap<String, RegisteredTool> tools = new ConcurrentHashMap<>();
	
	private final ConcurrentHashMap<String, RegisteredPrompt> prompts = new ConcurrentHashMap<>();
	
	private final ConcurrentHashMap<String, RegisteredResource> resources = new ConcurrentHashMap<>();
	
	private final ConcurrentHashMap<String, RegisteredResourceTemplate> resourceTemplates = new ConcurrentHashMap<>();
	
	private final Server server;
	
	private LoggingLevel loggingLevel = LoggingLevel.DEBUG;
	
	/**
	 * @param transport
	 * @param serverInfo
	 * @param serverCapabilities
	 * @param instructions
	 */
	public DefaultMCPServer(ServerTransport transport, Implementation serverInfo, ServerCapabilities serverCapabilities, String instructions) {
		this(new DefaultServer(transport, serverInfo, serverCapabilities, instructions));
		//TODO Auto-generated constructor stub
	}
	
	/**
	 * @param server
	 */
	public DefaultMCPServer(Server server) {
		super();
		this.server = server;
		if (server.getServerCapabilities().getLogging() != null) {
			server.addRequestHandler(SetLevelRequest.METHOD_LOGGING_SETLEVEL, setLoggingLevelRequestHandler());
			server.addNotificationHandler(LoggingMessageNotification.METHOD_NOTIFICATION_LOGGING_MESSAGE, notification -> {});
		}
		
		if (server.getServerCapabilities().getPrompts() != null) {
			server.addRequestHandler(ListPromptsRequest.METHOD_LIST_PROMPTS, listPromptsRequestHandler());
			server.addRequestHandler(GetPromptRequest.METHOD_PROMPTS_GET, getPromptRequestHandler());
		}
		
		if (server.getServerCapabilities().getResources() != null) {
			server.addRequestHandler(ListResourcesRequest.METHOD_LIST_RESOURCES, listResourcesRequestHandler());
			server.addRequestHandler(ReadResourceRequest.METHOD_READ_RESOURCE, readResourcesRequestHandler());
			server.addRequestHandler(ListResourceTemplatesRequest.METHOD_LIST_RESOURCE_TEMPLATES, listResourceTemplatesRequestHandler());
		}
		
		if (server.getServerCapabilities().getTools() != null) {
			server.addRequestHandler(ListToolsRequest.METHOD_LIST_TOOLS, listToolsRequestHandler());
			server.addRequestHandler(CallToolRequest.METHOD_TOOLS_CALL, callToolsRequestHandler());
		}
	}

	private RequestHandler<EmptyResult> setLoggingLevelRequestHandler() {
		
		return request -> {
			loggingLevel = LoggingLevel.of(String.valueOf(request.getParams().get("level")));
			return Schema.EMPTY_RESULT;
		};
	}
	
	private RequestHandler<ListPromptsResult> listPromptsRequestHandler() {
		
		return request -> {
			var promptList = prompts.values().stream().map(prompt -> prompt.getPrompt()).toList();
			ListPromptsResult result = new ListPromptsResult();
			result.setPrompts(promptList.toArray(new Prompt[promptList.size()]));
			return result;
		};
	}
	
	private RequestHandler<GetPromptResult> getPromptRequestHandler() {
		
		return request -> {
			String promptName = String.valueOf(request.getParams().get("name"));
			RegisteredPrompt registeredPrompt = prompts.get(promptName);
			if (registeredPrompt == null) {
				throw new MCPError(ErrorCodes.INVALID_PARAMS ,"Prompt not found: " + promptName);
			}
			
			return registeredPrompt.getMessageProvider().handle(request);
		};
	}
	
	private RequestHandler<ListResourcesResult> listResourcesRequestHandler() {
		
		return request -> {
			var resourceList = resources.values().stream().map(resource -> resource.getResource()).toList();
			ListResourcesResult result = new ListResourcesResult();
			result.setResources(resourceList.toArray(new Resource[resourceList.size()]));
			return result;
		};
	}
	
	private RequestHandler<ReadResourceResult> readResourcesRequestHandler() {
		
		return request -> {
			String resourceUri = String.valueOf(request.getParams().get("uri"));
			RegisteredResource registeredResource = resources.get(resourceUri);
			if (registeredResource == null) {
				throw new MCPError(ErrorCodes.INVALID_PARAMS ,"Resource with uri '" + resourceUri + "' not found.");
			}
			
			return registeredResource.getReadHandler().handle(request);
		};
	}
	
	private RequestHandler<ListResourceTemplatesResult> listResourceTemplatesRequestHandler() {
		return request -> {
			var resourceTemplateList = resourceTemplates.values().stream().map(resourceTemplate -> resourceTemplate.getResourceTemplate()).toList();
			ListResourceTemplatesResult result = new ListResourceTemplatesResult();
			result.setResourceTemplates(resourceTemplateList.toArray(new ResourceTemplate[resourceTemplateList.size()]));
			return result;
		};
	}
	
	private RequestHandler<ListToolsResult> listToolsRequestHandler() {
		
		return request -> {
			var toolsList = tools.values().stream().map(tool -> tool.getTool()).toList();
			ListToolsResult result = new ListToolsResult();
			result.setTools(toolsList.toArray(new Tool[toolsList.size()]));
			return result;
		};
		
	}
	
	private RequestHandler<CallToolResult> callToolsRequestHandler() {
		return request -> {
			String toolName = String.valueOf(request.getParams().get("name"));
			RegisteredTool registeredTool = tools.get(toolName);
			if (registeredTool == null) {
				throw new MCPError(ErrorCodes.INVALID_PARAMS ,"Tool not found: " + toolName);
			}
			
			return registeredTool.getHandler().handle(request);
		};
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		server.connect();
	}

	@Override
	public CompletableFuture<Void> connectAsync() {
		// TODO Auto-generated method stub
		return server.connectAsync();
	}
	
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		server.close();
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#ping()
	 */
	@Override
	public void ping() {
		// TODO Auto-generated method stub
		try {
			pingAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#createMessage(za.co.sindi.ai.mcp.schema.CreateMessageRequest.CreateMessageRequestParameters)
	 */
	@Override
	public CreateMessageResult createMessage(CreateMessageRequestParameters parameters) {
		// TODO Auto-generated method stub
		try {
			return createMessageAsync(parameters).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#listRoots()
	 */
	@Override
	public Root[] listRoots() {
		// TODO Auto-generated method stub
		try {
			return listRootsAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#sendLoggingMessage(za.co.sindi.ai.mcp.schema.LoggingMessageNotification.LoggingMessageNotificationParameters)
	 */
	@Override
	public void sendLoggingMessage(LoggingMessageNotificationParameters parameters) {
		// TODO Auto-generated method stub
		try {
			sendLoggingMessageAsync(parameters).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#sendResourceUpdated(java.lang.String)
	 */
	@Override
	public void sendResourceUpdated(String uri) {
		// TODO Auto-generated method stub
		try {
			sendResourceUpdatedAsync(uri).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#sendResourceListChanged()
	 */
	@Override
	public void sendResourceListChanged() {
		// TODO Auto-generated method stub
		try {
			sendResourceListChangedAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#sendToolListChanged()
	 */
	@Override
	public void sendToolListChanged() {
		// TODO Auto-generated method stub
		try {
			sendToolListChangedAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#sendPromptListChanged()
	 */
	@Override
	public void sendPromptListChanged() {
		// TODO Auto-generated method stub
		try {
			sendPromptListChangedAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#addTool(za.co.sindi.ai.mcp.schema.Tool, za.co.sindi.ai.mcp.shared.RequestHandler)
	 */
	@Override
	public void addTool(Tool tool, RequestHandler<CallToolResult> handler) {
		// TODO Auto-generated method stub
		try {
			addToolAsync(tool, handler).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#removeTool(java.lang.String)
	 */
	@Override
	public void removeTool(String toolName) {
		// TODO Auto-generated method stub
		try {
			removeToolAsync(toolName).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#addPrompt(za.co.sindi.ai.mcp.schema.Prompt, za.co.sindi.ai.mcp.shared.RequestHandler)
	 */
	@Override
	public void addPrompt(Prompt prompt, RequestHandler<GetPromptResult> promptProvider) {
		// TODO Auto-generated method stub
		try {
			addPromptAsync(prompt, promptProvider).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#removePrompt(java.lang.String)
	 */
	@Override
	public void removePrompt(String promptName) {
		// TODO Auto-generated method stub
		try {
			removePromptAsync(promptName).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#addResource(za.co.sindi.ai.mcp.schema.Resource, za.co.sindi.ai.mcp.shared.RequestHandler)
	 */
	@Override
	public void addResource(Resource resource, RequestHandler<ReadResourceResult> readHandler) {
		// TODO Auto-generated method stub
		try {
			addResourceAsync(resource, readHandler).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#removeResource(java.lang.String)
	 */
	@Override
	public void removeResource(String uri) {
		// TODO Auto-generated method stub
		try {
			removeResourceAsync(uri).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#addResourceTemplate(za.co.sindi.ai.mcp.schema.ResourceTemplate, za.co.sindi.ai.mcp.shared.RequestHandler)
	 */
	@Override
	public void addResourceTemplate(ResourceTemplate resourceTemplate,
			RequestHandler<ListResourceTemplatesResult> readCallback) {
		// TODO Auto-generated method stub
		try {
			addResourceTemplateAsync(resourceTemplate, readCallback).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
		}
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#removeResourceTemplate(java.lang.String)
	 */
	@Override
	public void removeResourceTemplate(String uriTemplate) {
		// TODO Auto-generated method stub
		try {
			removeResourceTemplateAsync(uriTemplate).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			server.onError(e);
		}
	}
	
	//------------------------------- Async -------------------------------

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#pingAsync()
	 */
	@Override
	public CompletableFuture<Void> pingAsync() {
		// TODO Auto-generated method stub
		return server.sendRequest(new PingRequest(), EmptyResult.class).thenAccept(result -> {});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#createMessageAsync(za.co.sindi.ai.mcp.schema.CreateMessageRequest.CreateMessageRequestParameters)
	 */
	@Override
	public CompletableFuture<CreateMessageResult> createMessageAsync(CreateMessageRequestParameters parameters) {
		// TODO Auto-generated method stub
		CreateMessageRequest request = new CreateMessageRequest();
		request.setParameters(parameters);
		return server.sendRequest(request, CreateMessageResult.class);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#listRootsAsync()
	 */
	@Override
	public CompletableFuture<Root[]> listRootsAsync() {
		// TODO Auto-generated method stub
		return server.sendRequest(new ListRootsRequest(), ListRootsResult.class).thenApply(result -> result.getRoots());
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#sendLoggingMessageAsync(za.co.sindi.ai.mcp.schema.LoggingMessageNotification.LoggingMessageNotificationParameters)
	 */
	@Override
	public CompletableFuture<Void> sendLoggingMessageAsync(LoggingMessageNotificationParameters parameters) {
		// TODO Auto-generated method stub
		LoggingMessageNotification notification = new LoggingMessageNotification();
		notification.setParameters(parameters);
		return server.sendNotification(notification);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#sendResourceUpdatedAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> sendResourceUpdatedAsync(String uri) {
		// TODO Auto-generated method stub
		ResourceUpdatedNotification notification = new ResourceUpdatedNotification();
		ResourceUpdatedNotificationParameters parameters = new ResourceUpdatedNotificationParameters();
		parameters.setUri(uri);
		notification.setParameters(parameters);
		return server.sendNotification(notification);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#sendResourceListChangedAsync()
	 */
	@Override
	public CompletableFuture<Void> sendResourceListChangedAsync() {
		// TODO Auto-generated method stub
		return server.sendNotification(new ResourceListChangedNotification());
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#sendToolListChangedAsync()
	 */
	@Override
	public CompletableFuture<Void> sendToolListChangedAsync() {
		// TODO Auto-generated method stub
		return server.sendNotification(new ToolListChangedNotification());
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#sendPromptListChangedAsync()
	 */
	@Override
	public CompletableFuture<Void> sendPromptListChangedAsync() {
		// TODO Auto-generated method stub
		return server.sendNotification(new PromptListChangedNotification());
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#addToolAsync(za.co.sindi.ai.mcp.schema.Tool, za.co.sindi.ai.mcp.shared.RequestHandler)
	 */
	@Override
	public CompletableFuture<Void> addToolAsync(Tool tool, RequestHandler<CallToolResult> handler) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(tool != null, "Tool must not be null.");
		Preconditions.checkArgument(handler != null, "Tool call handler must not be null.");
		Preconditions.checkState(server.getServerCapabilities().getTools() != null, "Server does not support tools capability.");
		
		if (server.getServerCapabilities().getTools() != null) {
			LOGGER.info("Registering tool: " + tool.getName());
			tools.put(tool.getName(), new RegisteredTool(tool, handler));
			
			if (server.getServerCapabilities().getTools().getListChanged()) return sendToolListChangedAsync();
		}
		
		return CompletableFuture.completedFuture(null);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#removeToolAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> removeToolAsync(String toolName) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(!Strings.isNullOrEmpty(toolName), "Tool name must not be null or empty.");
		Preconditions.checkState(server.getServerCapabilities().getTools() != null, "Server does not support tools capability.");
		
		if (server.getServerCapabilities().getTools() != null) {
			RegisteredTool removed = tools.remove(toolName);
			if (removed != null) {
				LOGGER.info("Removed tool: " + toolName);
				
				if (server.getServerCapabilities().getTools().getListChanged()) return sendToolListChangedAsync();
			}
		}
		
		return CompletableFuture.completedFuture(null);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#addPromptAsync(za.co.sindi.ai.mcp.schema.Prompt, za.co.sindi.ai.mcp.shared.RequestHandler)
	 */
	@Override
	public CompletableFuture<Void> addPromptAsync(Prompt prompt, RequestHandler<GetPromptResult> promptProvider) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(prompt != null, "Prompt must not be null.");
		Preconditions.checkArgument(promptProvider != null, "Prompt handler must not be null.");
		Preconditions.checkState(server.getServerCapabilities().getPrompts() != null, "Server does not support prompts capability.");
		
		if (server.getServerCapabilities().getPrompts() != null) {
			LOGGER.info("Registering Prompt: " + prompt.getName());
			prompts.put(prompt.getName(), new RegisteredPrompt(prompt, promptProvider));
			
			if (server.getServerCapabilities().getPrompts().getListChanged()) return sendPromptListChangedAsync();
		}
		
		return CompletableFuture.completedFuture(null);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#removePromptAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> removePromptAsync(String promptName) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(!Strings.isNullOrEmpty(promptName), "Prompt name must not be null or empty.");
		Preconditions.checkState(server.getServerCapabilities().getPrompts() != null, "Server does not support prompts capability.");
		
		if (server.getServerCapabilities().getPrompts() != null) {
			RegisteredPrompt removed = prompts.remove(promptName);
			if (removed != null) {
				LOGGER.info("Removed prompt: " + promptName);
				
				if (server.getServerCapabilities().getPrompts().getListChanged()) return sendPromptListChangedAsync();
			}
		}
		
		return CompletableFuture.completedFuture(null);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#addResourceAsync(za.co.sindi.ai.mcp.schema.Resource, za.co.sindi.ai.mcp.shared.RequestHandler)
	 */
	@Override
	public CompletableFuture<Void> addResourceAsync(Resource resource, RequestHandler<ReadResourceResult> readHandler) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(resource != null, "Resource must not be null.");
		Preconditions.checkArgument(readHandler != null, "Resource read handler must not be null.");
		Preconditions.checkState(server.getServerCapabilities().getResources() != null, "Server does not support resources capability.");
		
		if (server.getServerCapabilities().getResources() != null) {
			LOGGER.info("Registering Resource: " + resource.getUri());
			resources.put(resource.getUri(), new RegisteredResource(resource, readHandler));
			
			if (server.getServerCapabilities().getResources().getListChanged()) return sendResourceListChangedAsync();
		}
		
		return CompletableFuture.completedFuture(null);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#removeResourceAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> removeResourceAsync(String uri) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(!Strings.isNullOrEmpty(uri), "Resource uri must not be null or empty.");
		Preconditions.checkState(server.getServerCapabilities().getResources() != null, "Server does not support resources capability.");
		
		if (server.getServerCapabilities().getResources() != null) {
			RegisteredResource removed = resources.remove(uri);
			if (removed != null) {
				LOGGER.info("Removed resource: " + uri);
				
				if (server.getServerCapabilities().getResources().getListChanged()) return sendResourceListChangedAsync();
			}
		}
		
		return CompletableFuture.completedFuture(null);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#addResourceTemplateAsync(za.co.sindi.ai.mcp.schema.ResourceTemplate, za.co.sindi.ai.mcp.shared.RequestHandler)
	 */
	@Override
	public CompletableFuture<Void> addResourceTemplateAsync(ResourceTemplate resourceTemplate,
			RequestHandler<ListResourceTemplatesResult> readCallback) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(resourceTemplate != null, "Resource template must not be null.");
		Preconditions.checkArgument(readCallback != null, "Resource read callback handler must not be null.");
		Preconditions.checkState(server.getServerCapabilities().getResources() != null, "Server does not support resources capability.");
		
		if (server.getServerCapabilities().getResources() != null) {
			LOGGER.info("Registering Resource template: " + resourceTemplate.getUriTemplate());
			resourceTemplates.put(resourceTemplate.getUriTemplate(), new RegisteredResourceTemplate(resourceTemplate, readCallback));
			
			if (server.getServerCapabilities().getResources().getListChanged()) return sendResourceListChangedAsync();
		}
		
		return CompletableFuture.completedFuture(null);
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#removeResourceTemplateAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> removeResourceTemplateAsync(String uriTemplate) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(!Strings.isNullOrEmpty(uriTemplate), "Resource template uri must not be null or empty.");
		Preconditions.checkState(server.getServerCapabilities().getResources() != null, "Server does not support resources capability.");
		
		if (server.getServerCapabilities().getResources() != null) {
			RegisteredResourceTemplate removed = resourceTemplates.remove(uriTemplate);
			if (removed != null) {
				LOGGER.info("Removed resource template: " + uriTemplate);
				
				if (server.getServerCapabilities().getResources().getListChanged()) return sendResourceListChangedAsync();
			}
		}
		
		return CompletableFuture.completedFuture(null);	
	}
}
