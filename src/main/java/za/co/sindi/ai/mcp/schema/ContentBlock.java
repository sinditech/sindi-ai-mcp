package za.co.sindi.ai.mcp.schema;

import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;
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
	    @JsonbSubtype(alias="resource", type=EmbeddedResource.class),
	    @JsonbSubtype(alias="resource_link", type=ResourceLink.class)
	}
)
public sealed abstract class ContentBlock permits TextContent, ImageContent, AudioContent, ResourceLink, EmbeddedResource {
	
	@JsonbProperty
	private Annotations annotations;
	
	@JsonbProperty("_meta")
	private Map<String, Object> meta;

	/**
	 * @return the annotations
	 */
	public Annotations getAnnotations() {
		return annotations;
	}

	/**
	 * @param annotations the annotations to set
	 */
	public void setAnnotations(Annotations annotations) {
		this.annotations = annotations;
	}

	/**
	 * @return the meta
	 */
	public Map<String, Object> getMeta() {
		return meta;
	}

	/**
	 * @param meta the meta to set
	 */
	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}
}
