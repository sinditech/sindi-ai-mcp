package za.co.sindi.ai.mcp.server;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

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
import za.co.sindi.ai.mcp.schema.InitializeRequest;
import za.co.sindi.ai.mcp.schema.InitializeResult;
import za.co.sindi.ai.mcp.schema.InitializedNotification;
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
import za.co.sindi.ai.mcp.schema.ProtocolVersion;
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
import za.co.sindi.ai.mcp.shared.Server;
import za.co.sindi.ai.mcp.shared.ServerTransport;
import za.co.sindi.commons.utils.Preconditions;
import za.co.sindi.commons.utils.Strings;

/**
 * @author Buhake Sindi
 * @since 14 March 2025
 */
public class DefaultMCPServer extends Server implements MCPServer, MCPAsyncServer {
	
	private final ConcurrentHashMap<String, RegisteredTool> tools = new ConcurrentHashMap<>();
	
	private final ConcurrentHashMap<String, RegisteredPrompt> prompts = new ConcurrentHashMap<>();
	
	private final ConcurrentHashMap<String, RegisteredResource> resources = new ConcurrentHashMap<>();
	
	private final ConcurrentHashMap<String, RegisteredResourceTemplate> resourceTemplates = new ConcurrentHashMap<>();
	
	private LoggingLevel loggingLevel;
	
	/**
	 * @param transport
	 * @param serverInfo
	 * @param serverCapabilities
	 * @param instructions
	 */
	public DefaultMCPServer(ServerTransport transport, Implementation serverInfo, ServerCapabilities serverCapabilities, String instructions) {
		super(transport, serverInfo, serverCapabilities, instructions);
		//TODO Auto-generated constructor stub
		addRequestHandler(InitializeRequest.METHOD_INITIALIZE, request -> {
			var initializeRequest = (InitializeRequest) request;
			clientCapabilities = initializeRequest.getParameters().getCapabilities();
			clientInfo = initializeRequest.getParameters().getClientInfo();
			
			InitializeResult result = new InitializeResult();
			result.setCapabilities(serverCapabilities);
			result.setInstructions(instructions);
			result.setServerInfo(serverInfo);
			result.setProtocolVersion(ProtocolVersion.getLatest());
			
			return result;
		});
		
		addNotificationHandler(InitializedNotification.METHOD_NOTIFICATION_INITIALIZED, notification -> {});
		
		if (serverCapabilities.getLogging() != null) {
			addRequestHandler(SetLevelRequest.METHOD_LOGGING_SETLEVEL, setLoggingLevelRequestHandler());
			addNotificationHandler(LoggingMessageNotification.METHOD_NOTIFICATION_LOGGING_MESSAGE, notification -> {});
		}
		
		if (serverCapabilities.getPrompts() != null) {
			addRequestHandler(ListPromptsRequest.METHOD_LIST_PROMPTS, listPromptsRequestHandler());
			addRequestHandler(GetPromptRequest.METHOD_PROMPTS_GET, getPromptRequestHandler());
		}
		
		if (serverCapabilities.getResources() != null) {
			addRequestHandler(ListResourcesRequest.METHOD_LIST_RESOURCES, listResourcesRequestHandler());
			addRequestHandler(ReadResourceRequest.METHOD_READ_RESOURCE, readResourcesRequestHandler());
			addRequestHandler(ListResourceTemplatesRequest.METHOD_LIST_RESOURCE_TEMPLATES, listResourceTemplatesRequestHandler());
		}
		
		if (serverCapabilities.getTools() != null) {
			addRequestHandler(ListToolsRequest.METHOD_LIST_TOOLS, listToolsRequestHandler());
			addRequestHandler(CallToolRequest.METHOD_TOOLS_CALL, callToolsRequestHandler());
		}
	}
	
	private RequestHandler<EmptyResult> setLoggingLevelRequestHandler() {
		
		return request -> {
			SetLevelRequest setLevelRequest = (SetLevelRequest)request;
			loggingLevel = setLevelRequest.getParameters().getLevel();
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
			var promptRequest = (GetPromptRequest) request;
			String promptName = promptRequest.getParameters().getName();
			RegisteredPrompt registeredPrompt = prompts.get(promptName);
			if (registeredPrompt == null) {
				throw new MCPError(ErrorCodes.INVALID_PARAMS ,"Prompt not found: " + promptName);
			}
			
			return registeredPrompt.getMessageProvider().handle(promptRequest);
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
			var readResourceRequest = (ReadResourceRequest) request;
			String resourceUri = readResourceRequest.getParameters().getUri();
			RegisteredResource registeredResource = resources.get(resourceUri);
			if (registeredResource == null) {
				throw new MCPError(ErrorCodes.INVALID_PARAMS ,"Resource with uri '" + resourceUri + "' not found.");
			}
			
			return registeredResource.getReadHandler().handle(readResourceRequest);
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
			var callToolRequest = (CallToolRequest) request;
			String toolName = callToolRequest.getParameters().getName();
			RegisteredTool registeredTool = tools.get(toolName);
			if (registeredTool == null) {
				throw new MCPError(ErrorCodes.INVALID_PARAMS ,"Tool not found: " + toolName);
			}
			
			return registeredTool.getHandler().handle(callToolRequest);
		};
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPServer#ping()
	 */
	@Override
	public EmptyResult ping() {
		// TODO Auto-generated method stub
		try {
			return pingAsync().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			onError(e);
			return null;
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
			onError(e);
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
			onError(e);
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
			onError(e);
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
			onError(e);
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
			onError(e);
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
			onError(e);
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
			onError(e);
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
			onError(e);
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
			onError(e);
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
			onError(e);
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
			onError(e);
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
			onError(e);
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
			onError(e);
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
			onError(e);
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
			onError(e);
		}
	}
	
	//------------------------------- Async -------------------------------

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#pingAsync()
	 */
	@Override
	public CompletableFuture<EmptyResult> pingAsync() {
		// TODO Auto-generated method stub
		CompletableFuture<EmptyResult> cf = new CompletableFuture<>();
		cf.complete(sendRequest(new PingRequest()));
		return cf;
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#createMessageAsync(za.co.sindi.ai.mcp.schema.CreateMessageRequest.CreateMessageRequestParameters)
	 */
	@Override
	public CompletableFuture<CreateMessageResult> createMessageAsync(CreateMessageRequestParameters parameters) {
		// TODO Auto-generated method stub
		return CompletableFuture.supplyAsync(() -> {
			CreateMessageRequest request = new CreateMessageRequest();
			request.setParameters(parameters);
			return sendRequest(request);
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#listRootsAsync()
	 */
	@Override
	public CompletableFuture<Root[]> listRootsAsync() {
		// TODO Auto-generated method stub
		return CompletableFuture.supplyAsync(() -> {
			ListRootsResult result = sendRequest(new ListRootsRequest());
			return result.getRoots();
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#sendLoggingMessageAsync(za.co.sindi.ai.mcp.schema.LoggingMessageNotification.LoggingMessageNotificationParameters)
	 */
	@Override
	public CompletableFuture<Void> sendLoggingMessageAsync(LoggingMessageNotificationParameters parameters) {
		// TODO Auto-generated method stub
		return CompletableFuture.runAsync(() -> {
			LoggingMessageNotification notification = new LoggingMessageNotification();
			notification.setParameters(parameters);
			sendNotification(notification);
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#sendResourceUpdatedAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> sendResourceUpdatedAsync(String uri) {
		// TODO Auto-generated method stub
		return CompletableFuture.runAsync(() -> {
			ResourceUpdatedNotification notification = new ResourceUpdatedNotification();
			ResourceUpdatedNotificationParameters parameters = new ResourceUpdatedNotificationParameters();
			parameters.setUri(uri);
			notification.setParameters(parameters);
			sendNotification(notification);
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#sendResourceListChangedAsync()
	 */
	@Override
	public CompletableFuture<Void> sendResourceListChangedAsync() {
		// TODO Auto-generated method stub
		return CompletableFuture.runAsync(() -> {
			sendNotification(new ResourceListChangedNotification());
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#sendToolListChangedAsync()
	 */
	@Override
	public CompletableFuture<Void> sendToolListChangedAsync() {
		// TODO Auto-generated method stub
		return CompletableFuture.runAsync(() -> {
			sendNotification(new ToolListChangedNotification());
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#sendPromptListChangedAsync()
	 */
	@Override
	public CompletableFuture<Void> sendPromptListChangedAsync() {
		// TODO Auto-generated method stub
		return CompletableFuture.runAsync(() -> {
			sendNotification(new PromptListChangedNotification());
		});
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#addToolAsync(za.co.sindi.ai.mcp.schema.Tool, za.co.sindi.ai.mcp.shared.RequestHandler)
	 */
	@Override
	public CompletableFuture<Void> addToolAsync(Tool tool, RequestHandler<CallToolResult> handler) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(tool != null, "Tool must not be null.");
		Preconditions.checkArgument(handler != null, "Tool call handler must not be null.");
		Preconditions.checkState(serverCapabilities.getTools() != null, "Server does not support tools capability.");
		
		return CompletableFuture.runAsync(() -> {
			LOGGER.info("Registering tool: " + tool.getName());
			tools.put(tool.getName(), new RegisteredTool(tool, handler));
		}).thenCompose(result -> serverCapabilities.getTools().getListChanged() ? sendToolListChangedAsync() : CompletableFuture.completedFuture(null));
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#removeToolAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> removeToolAsync(String toolName) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(!Strings.isNullOrEmpty(toolName), "Tool name must not be null or empty.");
		Preconditions.checkState(serverCapabilities.getTools() != null, "Server does not support tools capability.");
		
		return CompletableFuture.supplyAsync(() -> {
			RegisteredTool removed = tools.remove(toolName);
			if (removed != null) {
				LOGGER.info("Removed tool: " + toolName);
			}
			return removed;
		}).thenCompose(removed -> removed != null && serverCapabilities.getTools().getListChanged() ? sendToolListChangedAsync() : CompletableFuture.completedFuture(null));
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#addPromptAsync(za.co.sindi.ai.mcp.schema.Prompt, za.co.sindi.ai.mcp.shared.RequestHandler)
	 */
	@Override
	public CompletableFuture<Void> addPromptAsync(Prompt prompt, RequestHandler<GetPromptResult> promptProvider) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(prompt != null, "Prompt must not be null.");
		Preconditions.checkArgument(promptProvider != null, "Prompt handler must not be null.");
		Preconditions.checkState(serverCapabilities.getPrompts() != null, "Server does not support prompts capability.");
		
		return CompletableFuture.runAsync(() -> {
			LOGGER.info("Registering Prompt: " + prompt.getName());
			prompts.put(prompt.getName(), new RegisteredPrompt(prompt, promptProvider));
		}).thenCompose(result -> serverCapabilities.getPrompts().getListChanged() ? sendPromptListChangedAsync() : CompletableFuture.completedFuture(null));
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#removePromptAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> removePromptAsync(String promptName) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(!Strings.isNullOrEmpty(promptName), "Prompt name must not be null or empty.");
		Preconditions.checkState(serverCapabilities.getPrompts() != null, "Server does not support prompts capability.");
		
		return CompletableFuture.supplyAsync(() -> {
			RegisteredPrompt removed = prompts.remove(promptName);
			if (removed != null) {
				LOGGER.info("Removed prompt: " + promptName);
			}
			return removed;
		}).thenCompose(removed -> removed != null && serverCapabilities.getPrompts().getListChanged() ? sendPromptListChangedAsync() : CompletableFuture.completedFuture(null));
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#addResourceAsync(za.co.sindi.ai.mcp.schema.Resource, za.co.sindi.ai.mcp.shared.RequestHandler)
	 */
	@Override
	public CompletableFuture<Void> addResourceAsync(Resource resource, RequestHandler<ReadResourceResult> readHandler) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(resource != null, "Resource must not be null.");
		Preconditions.checkArgument(readHandler != null, "Resource read handler must not be null.");
		Preconditions.checkState(serverCapabilities.getResources() != null, "Server does not support resources capability.");
		
		return CompletableFuture.runAsync(() -> {
			LOGGER.info("Registering Resource: " + resource.getUri());
			resources.put(resource.getUri(), new RegisteredResource(resource, readHandler));
		}).thenCompose(result -> serverCapabilities.getResources().getListChanged() ? sendResourceListChangedAsync() : CompletableFuture.completedFuture(null));
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#removeResourceAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> removeResourceAsync(String uri) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(!Strings.isNullOrEmpty(uri), "Resource uri must not be null or empty.");
		Preconditions.checkState(serverCapabilities.getResources() != null, "Server does not support resources capability.");
		
		return CompletableFuture.supplyAsync(() -> {
			RegisteredResource removed = resources.remove(uri);
			if (removed != null) {
				LOGGER.info("Removed resource: " + uri);
			}
			return removed;
		}).thenCompose(removed -> removed != null && serverCapabilities.getResources().getListChanged() ? sendResourceListChangedAsync() : CompletableFuture.completedFuture(null));
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
		Preconditions.checkState(serverCapabilities.getResources() != null, "Server does not support resources capability.");
		
		return CompletableFuture.runAsync(() -> {
			LOGGER.info("Registering Resource template: " + resourceTemplate.getUriTemplate());
			resourceTemplates.put(resourceTemplate.getUriTemplate(), new RegisteredResourceTemplate(resourceTemplate, readCallback));
		}).thenCompose(result -> serverCapabilities.getResources().getListChanged() ? sendResourceListChangedAsync() : CompletableFuture.completedFuture(null));
	}

	/* (non-Javadoc)
	 * @see za.co.sindi.ai.mcp.server.MCPAsyncServer#removeResourceTemplateAsync(java.lang.String)
	 */
	@Override
	public CompletableFuture<Void> removeResourceTemplateAsync(String uriTemplate) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(!Strings.isNullOrEmpty(uriTemplate), "Resource template uri must not be null or empty.");
		Preconditions.checkState(serverCapabilities.getResources() != null, "Server does not support resources capability.");
		
		return CompletableFuture.supplyAsync(() -> {
			RegisteredResourceTemplate removed = resourceTemplates.remove(uriTemplate);
			if (removed != null) {
				LOGGER.info("Removed resource template: " + uriTemplate);
			}
			return removed;
		}).thenCompose(removed -> removed != null && serverCapabilities.getResources().getListChanged() ? sendResourceListChangedAsync() : CompletableFuture.completedFuture(null));
	}
}
