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
	    @JsonbSubtype(alias="ref/resource", type=ResourceTemplateReference.class),
	    @JsonbSubtype(alias="ref/prompt", type=PromptReference.class),
	}
)
public sealed abstract class Reference extends Annotated permits ResourceTemplateReference, PromptReference {

}
