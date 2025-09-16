package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
@JsonbTypeInfo(
	key = "type",
	value = {
	    @JsonbSubtype(alias="text", type=TextContent.class),
	    @JsonbSubtype(alias="image", type=ImageContent.class),
	    @JsonbSubtype(alias="audio", type=AudioContent.class),
	    @JsonbSubtype(alias="resource", type=EmbeddedResource.class)
	}
)
public sealed abstract class ContentBlock extends Annotated permits TextContent, ImageContent, AudioContent, EmbeddedResource {

}
