package za.co.sindi.ai.mcp.schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import za.co.sindi.ai.mcp.mapper.JSONObjectMapper;
import za.co.sindi.ai.mcp.mapper.ObjectMapper;

/**
 * @author Buhake Sindi
 * @since 14 March 2025
 */
public final class MCPSchema {
	
	private static final Map<String, Class<? extends Request>> JSONRPC_REQUEST_METHODS;
	private static final Map<Class<? extends Request>, String> REQUEST_METHODS;
	private static final Map<String, Class<? extends Notification>> JSONRPC_NOTIFICATION_METHODS;
	private static final Map<Class<? extends Notification>, String> NOTIFICATION_METHODS;
	
	private static final ObjectMapper MAPPER = JSONObjectMapper.newInstance();
	
	static {
		Map<String, Class<? extends Request>> requestMethods = new HashMap<>();
		requestMethods.put(CallToolRequest.METHOD_TOOLS_CALL, CallToolRequest.class);
	    requestMethods.put(CompleteRequest.METHOD_COMPLETION_COMPLETE, CompleteRequest.class);
	    requestMethods.put(CreateMessageRequest.METHOD_SAMPLING_CREATE_MESSAGE, CreateMessageRequest.class);
	    requestMethods.put(GetPromptRequest.METHOD_PROMPTS_GET, GetPromptRequest.class);
	    requestMethods.put(InitializeRequest.METHOD_INITIALIZE, InitializeRequest.class);
	    requestMethods.put(ListPromptsRequest.METHOD_LIST_PROMPTS, ListPromptsRequest.class);
	    requestMethods.put(ListResourcesRequest.METHOD_LIST_RESOURCES, ListResourcesRequest.class);
	    requestMethods.put(ListResourceTemplatesRequest.METHOD_LIST_RESOURCE_TEMPLATES, ListResourceTemplatesRequest.class);
	    requestMethods.put(ListToolsRequest.METHOD_LIST_TOOLS, ListToolsRequest.class);
	    requestMethods.put(PingRequest.METHOD_PING, PingRequest.class);
	    requestMethods.put(ReadResourceRequest.METHOD_READ_RESOURCE, ReadResourceRequest.class);
	    requestMethods.put(SetLevelRequest.METHOD_LOGGING_SETLEVEL, SetLevelRequest.class);
	    requestMethods.put(SubscribeRequest.METHOD_SUBSCRIBE, SubscribeRequest.class);
	    requestMethods.put(UnsubscribeRequest.METHOD_UNSUBSCRIBE, UnsubscribeRequest.class);
	    JSONRPC_REQUEST_METHODS = Collections.unmodifiableMap(requestMethods);
	    REQUEST_METHODS = Collections.unmodifiableMap(requestMethods.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)));
	    
	    Map<String, Class<? extends Notification>> notificationMethods = new HashMap<>();
	    notificationMethods.put(CancelledNotification.METHOD_NOTIFICATION_CANCELLED, CancelledNotification.class);
	    notificationMethods.put(InitializedNotification.METHOD_NOTIFICATION_INITIALIZED, InitializedNotification.class);
	    notificationMethods.put(LoggingMessageNotification.METHOD_NOTIFICATION_LOGGING_MESSAGE, LoggingMessageNotification.class);
	    notificationMethods.put(ProgressNotification.METHOD_NOTIFICATION_PROGRESS, ProgressNotification.class);
	    notificationMethods.put(PromptListChangedNotification.METHOD_NOTIFICATION_PROMPTS_lIST_CHANGED, PromptListChangedNotification.class);
	    notificationMethods.put(ResourceListChangedNotification.METHOD_NOTIFICATION_RESOURCES_lIST_CHANGED, ResourceListChangedNotification.class);
	    notificationMethods.put(ResourceUpdatedNotification.METHOD_RESOURCES_UPDATED, ResourceUpdatedNotification.class);
	    notificationMethods.put(RootsListChangedNotification.METHOD_NOTIFICATION_ROOTS_lIST_CHANGED, RootsListChangedNotification.class);
	    notificationMethods.put(ToolListChangedNotification.METHOD_NOTIFICATION_TOOLS_lIST_CHANGED, ToolListChangedNotification.class);
	    JSONRPC_NOTIFICATION_METHODS = Collections.unmodifiableMap(notificationMethods);
	    NOTIFICATION_METHODS = Collections.unmodifiableMap(notificationMethods.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)));
	}

	private MCPSchema() {
		throw new AssertionError("Private constructor.");
	}
	
	public static String serializeJSONRPCMessage(JSONRPCMessage... messages) {
		return serializeJSONRPCMessage(MAPPER, messages);
	}
	
	public static String serializeJSONRPCMessage(ObjectMapper objectMapper, JSONRPCMessage... messages) {
		if (messages.length == 1) {
			return objectMapper.map(messages[0]);
		}
		
		return objectMapper.map(messages);
	}
	
	public static JSONRPCMessage deserializeJSONRPCMessage(String jsonText) {
		return deserializeJSONRPCMessage(MAPPER, jsonText);
	}
	
	public static JSONRPCMessage deserializeJSONRPCMessage(ObjectMapper objectMapper, String jsonText) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = objectMapper.map(jsonText, Map.class);
		
		// Determine message type based on specific JSON structure
		if (map.containsKey("method") && map.containsKey("id")) {
			return objectMapper.map(jsonText, JSONRPCRequest.class);
		}
		else if (map.containsKey("method") && !map.containsKey("id")) {
			return objectMapper.map(jsonText, JSONRPCNotification.class);
		}
		else if (map.containsKey("result") && map.containsKey("id")) {
			return objectMapper.map(jsonText, JSONRPCResult.class);
		} 
		else if (map.containsKey("error") && map.containsKey("id")) {
			return objectMapper.map(jsonText, JSONRPCError.class);
		}

		throw new IllegalArgumentException("Cannot deserialize JSONRPCMessage: " + jsonText);
	}
	
	public static JSONRPCMessage[] deserializeJSONRPCMessages(String jsonText) {
		return deserializeJSONRPCMessages(MAPPER, jsonText);
	}
	
	public static JSONRPCMessage[] deserializeJSONRPCMessages(ObjectMapper objectMapper, String jsonText) {
		JSONRPCMessage[] result = null;
		if (jsonText.trim().startsWith("[") && jsonText.trim().endsWith("]")) {
			List<?> objectList = objectMapper.map(jsonText, List.class);
			
			// Create array to hold the individual JSON strings
			result = new JSONRPCMessage[objectList.size()];
	        
			// Convert each object back to its JSON string representation
	        for (int i = 0; i < objectList.size(); i++) {
	            result[i] = deserializeJSONRPCMessage(objectMapper, objectMapper.map(objectList.get(i)));
	        }
		} else {		
			result = new JSONRPCMessage[1];
			result[0] = deserializeJSONRPCMessage(objectMapper, jsonText);
		}
		
		return result;
	}
	
	public static <T extends Request> T toRequest(JSONRPCRequest request) {
		return toRequest(MAPPER, request);
	}
	
	public static <T extends Request> T toRequest(ObjectMapper objectMapper, JSONRPCRequest request) {
		@SuppressWarnings("unchecked")
		Class<T> requestClass = (Class<T>) JSONRPC_REQUEST_METHODS.get(request.getMethod());
		return objectMapper.map(objectMapper.map(request), requestClass);
	}
	
	public static <T extends Notification> T toNotification(JSONRPCNotification notification) {
		return toNotification(MAPPER, notification);
	}
	
	public static <T extends Notification> T toNotification(ObjectMapper objectMapper, JSONRPCNotification notification) {
		@SuppressWarnings("unchecked")
		Class<T> requestClass = (Class<T>) JSONRPC_NOTIFICATION_METHODS.get(notification.getMethod());
		return objectMapper.map(objectMapper.map(notification), requestClass);
	}
	
	public static <T extends Result> T toResult(JSONRPCResult result, final Class<T> resultType) {
		return toResult(MAPPER, result, resultType);
	}
	
	public static <T extends Result> T toResult(ObjectMapper objectMapper, JSONRPCResult result, final Class<T> resultType) {
		return objectMapper.map(objectMapper.map(result.getResult()), resultType);
	}
	
	
	public static <T extends Request> JSONRPCRequest toJSONRPCRequest(T request) {
		return toJSONRPCRequest(MAPPER, request);
	}
	
	public static <T extends Request> JSONRPCRequest toJSONRPCRequest(ObjectMapper objectMapper, T request) {
		return toJSONRPCRequest(objectMapper, JSONRPCVersion.getLatest(), request);
	}
	
	public static <T extends Request> JSONRPCRequest toJSONRPCRequest(ObjectMapper objectMapper, JSONRPCVersion jsonRPCVersion, T request) {
		JSONRPCRequest jsonRPCRequest = objectMapper.map(objectMapper.map(request), JSONRPCRequest.class);
		jsonRPCRequest.setJsonrpc(jsonRPCVersion);
		jsonRPCRequest.setMethod(REQUEST_METHODS.get(request.getClass()));
		return jsonRPCRequest;
		
	}
	
	public static <T extends Notification> JSONRPCNotification toJSONRPCNotification(T notification) {
		return toJSONRPCNotification(MAPPER, notification);
	}
	
	public static <T extends Notification> JSONRPCNotification toJSONRPCNotification(ObjectMapper objectMapper, T notification) {
		return toJSONRPCNotification(objectMapper, JSONRPCVersion.getLatest(), notification);
	}
	
	public static <T extends Notification> JSONRPCNotification toJSONRPCNotification(ObjectMapper objectMapper, JSONRPCVersion jsonRPCVersion, T notification) {
		JSONRPCNotification jsonRPCNotification = objectMapper.map(objectMapper.map(notification), JSONRPCNotification.class);
		jsonRPCNotification.setJsonrpc(jsonRPCVersion);
		jsonRPCNotification.setMethod(NOTIFICATION_METHODS.get(notification.getClass()));
		return jsonRPCNotification;
	}
	
	public static <T> T toObject(final Map<String, Object> map, final Class<T> clazz) {
		return toObject(MAPPER, map, clazz);
	}
	
	public static <T> T toObject(final ObjectMapper objectMapper, final Map<String, Object> map, final Class<T> clazz) {
		String jsonText = objectMapper.map(map);
		return objectMapper.map(jsonText, clazz);
	}
}
