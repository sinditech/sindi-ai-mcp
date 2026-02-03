package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public sealed interface ClientRequest extends Request permits PingRequest, InitializeRequest, CompleteRequest, SetLevelRequest, GetPromptRequest, ListPromptsRequest, ListResourcesRequest, ListResourceTemplatesRequest, ReadResourceRequest, SubscribeRequest, UnsubscribeRequest, CallToolRequest, ListToolsRequest, GetTaskRequest, GetTaskPayloadRequest, CancelTaskRequest, ListTasksRequest {

}
