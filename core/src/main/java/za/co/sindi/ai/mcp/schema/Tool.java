package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;
import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public class Tool extends BaseMetadata implements BaseIcons {
	
	@JsonbProperty
	private String description;
	
	@JsonbProperty
	private InputSchema inputSchema;
	
	@JsonbProperty
	private ToolExecution execution;
	
	@JsonbProperty
	private OutputSchema outputSchema;
	
	@JsonbProperty
	private ToolAnnotations annotations;
	
	@JsonbProperty("_meta")
	private Map<String, Object> meta;
	
	@JsonbProperty
	private Icon[] icons;
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the inputSchema
	 */
	public InputSchema getInputSchema() {
		return inputSchema;
	}

	/**
	 * @param inputSchema the inputSchema to set
	 */
	public void setInputSchema(InputSchema inputSchema) {
		this.inputSchema = inputSchema;
	}

	/**
	 * @return the execution
	 */
	public ToolExecution getExecution() {
		return execution;
	}

	/**
	 * @param execution the execution to set
	 */
	public void setExecution(ToolExecution execution) {
		this.execution = execution;
	}

	/**
	 * @return the outputSchema
	 */
	public OutputSchema getOutputSchema() {
		return outputSchema;
	}

	/**
	 * @param outputSchema the outputSchema to set
	 */
	public void setOutputSchema(OutputSchema outputSchema) {
		this.outputSchema = outputSchema;
	}

	/**
	 * @return the annotations
	 */
	public ToolAnnotations getAnnotations() {
		return annotations;
	}

	/**
	 * @param annotations the annotations to set
	 */
	public void setAnnotations(ToolAnnotations annotations) {
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

	/**
	 * @return the icons
	 */
	public Icon[] getIcons() {
		return icons;
	}

	/**
	 * @param icons the icons to set
	 */
	public void setIcons(Icon[] icons) {
		this.icons = icons;
	}

	public static class InputSchema implements Serializable {
		
		@JsonbProperty
		private String type;
		
		@JsonbProperty
		private Boolean additionalProperties;
		
		@JsonbProperty
		private Map<String, PropertySchema> properties;
		
		@JsonbProperty
		private String[] required;

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @return the additionalProperties
		 */
		public Boolean getAdditionalProperties() {
			return additionalProperties;
		}

		/**
		 * @param additionalProperties the additionalProperties to set
		 */
		public void setAdditionalProperties(Boolean additionalProperties) {
			this.additionalProperties = additionalProperties;
		}

		/**
		 * @return the properties
		 */
		public Map<String, PropertySchema> getProperties() {
			return properties;
		}

		/**
		 * @param properties the properties to set
		 */
		public void setProperties(Map<String, PropertySchema> properties) {
			this.properties = properties;
		}

		/**
		 * @return the required
		 */
		public String[] getRequired() {
			return required;
		}

		/**
		 * @param required the required to set
		 */
		public void setRequired(String[] required) {
			this.required = required;
		}
	}
	
	public static class OutputSchema implements Serializable {
		
		@JsonbProperty
		private String type;
		
		@JsonbProperty
		private Map<String, PropertySchema> properties;
		
		@JsonbProperty
		private String[] required;

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @return the properties
		 */
		public Map<String, PropertySchema> getProperties() {
			return properties;
		}

		/**
		 * @param properties the properties to set
		 */
		public void setProperties(Map<String, PropertySchema> properties) {
			this.properties = properties;
		}

		/**
		 * @return the required
		 */
		public String[] getRequired() {
			return required;
		}

		/**
		 * @param required the required to set
		 */
		public void setRequired(String[] required) {
			this.required = required;
		}
	}
	
	public static class PropertySchema implements Serializable {
		
		@JsonbProperty
		private String type;
		
		@JsonbProperty
		private String description;
		
		@JsonbProperty
		private PropertySchema items;

		/**
		 * 
		 */
		public PropertySchema() {
			super();
			// TODO Auto-generated constructor stub
		}

		/**
		 * @param type
		 */
		public PropertySchema(String type) {
			super();
			this.type = type;
		}

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @return the items
		 */
		public PropertySchema getItems() {
			return items;
		}

		/**
		 * @param items the items to set
		 */
		public void setItems(PropertySchema items) {
			this.items = items;
		}
	}
	
	public static class ToolAnnotations implements Serializable {
		
		@JsonbProperty
		private String title;
		
		@JsonbProperty
		private Boolean readOnlyHint;
		
		@JsonbProperty
		private Boolean destructiveHint;
		
		@JsonbProperty
		private Boolean idempotentHint;
		
		@JsonbProperty
		private Boolean openWorldHint;
		
		/**
		 * 
		 */
		public ToolAnnotations() {
			super();
			// TODO Auto-generated constructor stub
		}

		/**
		 * @param title
		 * @param readOnlyHint
		 * @param destructiveHint
		 * @param idempotentHint
		 * @param openWorldHint
		 */
		public ToolAnnotations(String title, Boolean readOnlyHint, Boolean destructiveHint, Boolean idempotentHint,
				Boolean openWorldHint) {
			super();
			this.title = title;
			this.readOnlyHint = readOnlyHint;
			this.destructiveHint = destructiveHint;
			this.idempotentHint = idempotentHint;
			this.openWorldHint = openWorldHint;
		}

		/**
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}
		
		/**
		 * @param title the title to set
		 */
		public void setTitle(String title) {
			this.title = title;
		}
		
		/**
		 * @return the readOnlyHint
		 */
		public Boolean getReadOnlyHint() {
			return readOnlyHint;
		}
		
		/**
		 * @param readOnlyHint the readOnlyHint to set
		 */
		public void setReadOnlyHint(Boolean readOnlyHint) {
			this.readOnlyHint = readOnlyHint;
		}
		
		/**
		 * @return the destructiveHint
		 */
		public Boolean getDestructiveHint() {
			return destructiveHint;
		}
		
		/**
		 * @param destructiveHint the destructiveHint to set
		 */
		public void setDestructiveHint(Boolean destructiveHint) {
			this.destructiveHint = destructiveHint;
		}
		
		/**
		 * @return the idempotentHint
		 */
		public Boolean getIdempotentHint() {
			return idempotentHint;
		}
		
		/**
		 * @param idempotentHint the idempotentHint to set
		 */
		public void setIdempotentHint(Boolean idempotentHint) {
			this.idempotentHint = idempotentHint;
		}
		
		/**
		 * @return the openWorldHint
		 */
		public Boolean getOpenWorldHint() {
			return openWorldHint;
		}
		
		/**
		 * @param openWorldHint the openWorldHint to set
		 */
		public void setOpenWorldHint(Boolean openWorldHint) {
			this.openWorldHint = openWorldHint;
		}
	}
}
