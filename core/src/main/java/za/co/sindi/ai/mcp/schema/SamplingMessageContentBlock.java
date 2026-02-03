package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

/**
 * @author Buhake Sindi
 * @since 04 January 2026
 */
@JsonbTypeInfo(
	key = "type",
	value = {
	    @JsonbSubtype(alias="text", type=TextContent.class),
		@JsonbSubtype(alias="image", type=ImageContent.class),
		@JsonbSubtype(alias="audio", type=AudioContent.class),
		@JsonbSubtype(alias="resource", type=EmbeddedResource.class),
		@JsonbSubtype(alias="resource_link", type=ResourceLink.class),
	    @JsonbSubtype(alias="tool_use", type=ToolUseContent.class),
	    @JsonbSubtype(alias="tool_result", type=ToolResultContent.class)
	}
)
public sealed interface SamplingMessageContentBlock permits ContentBlock, ToolUseContent, ToolResultContent {
	
}
