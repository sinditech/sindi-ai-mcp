package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
@JsonbTypeInfo(
	key = "method",
	value = {
	    @JsonbSubtype(alias=CallToolRequest.METHOD_TOOLS_CALL, type=CallToolRequest.class),
	    @JsonbSubtype(alias=CancelTaskRequest.METHOD_TASKS_RESULT, type=CancelTaskRequest.class),
	    @JsonbSubtype(alias=CompleteRequest.METHOD_COMPLETION_COMPLETE, type=CompleteRequest.class),
	    @JsonbSubtype(alias=CreateMessageRequest.METHOD_SAMPLING_CREATE_MESSAGE, type=CreateMessageRequest.class),
	    @JsonbSubtype(alias=ElicitRequest.METHOD_ELICITATION_CREATE, type=ElicitRequest.class),
	    @JsonbSubtype(alias=GetPromptRequest.METHOD_PROMPTS_GET, type=GetPromptRequest.class),
	    @JsonbSubtype(alias=GetTaskPayloadRequest.METHOD_TASKS_RESULT, type=GetTaskPayloadRequest.class),
	    @JsonbSubtype(alias=GetTaskRequest.METHOD_TASKS_GET, type=GetTaskRequest.class),
	    @JsonbSubtype(alias=InitializeRequest.METHOD_INITIALIZE, type=InitializeRequest.class),
	    @JsonbSubtype(alias=ListPromptsRequest.METHOD_LIST_PROMPTS, type=ListPromptsRequest.class),
	    @JsonbSubtype(alias=ListResourcesRequest.METHOD_LIST_RESOURCES, type=ListResourcesRequest.class),
	    @JsonbSubtype(alias=ListResourceTemplatesRequest.METHOD_LIST_RESOURCE_TEMPLATES, type=ListResourceTemplatesRequest.class),
	    @JsonbSubtype(alias=ListToolsRequest.METHOD_LIST_TOOLS, type=ListToolsRequest.class),
	    @JsonbSubtype(alias=PingRequest.METHOD_PING, type=PingRequest.class),
	    @JsonbSubtype(alias=ReadResourceRequest.METHOD_READ_RESOURCE, type=ReadResourceRequest.class),
	    @JsonbSubtype(alias=SetLevelRequest.METHOD_LOGGING_SETLEVEL, type=SetLevelRequest.class),
	    @JsonbSubtype(alias=SubscribeRequest.METHOD_SUBSCRIBE, type=SubscribeRequest.class),
	    @JsonbSubtype(alias=UnsubscribeRequest.METHOD_UNSUBSCRIBE, type=UnsubscribeRequest.class),
	}
)
public abstract class BaseRequest implements Request {

}
