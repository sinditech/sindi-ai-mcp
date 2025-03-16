package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public sealed interface ServerResult extends Result permits EmptyResult, InitializeResult, CompleteResult, GetPromptResult, ListPromptsResult, ListResourcesResult, ListResourceTemplatesResult, ReadResourceResult, CallToolResult, ListToolsResult {

}
