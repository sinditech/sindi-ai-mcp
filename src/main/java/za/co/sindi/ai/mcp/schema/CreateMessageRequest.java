package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.mcp.schema.CreateMessageRequest.CreateMessageRequestParameters;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class CreateMessageRequest extends BaseRequest implements ParameterBasedRequest<CreateMessageRequestParameters>, ServerRequest {
	
	public static final String METHOD_SAMPLING_CREATE_MESSAGE = "sampling/createMessage";
	
	/**
	 * 
	 */
	public CreateMessageRequest() {
		super(METHOD_SAMPLING_CREATE_MESSAGE);
		//TODO Auto-generated constructor stub
	}

	@JsonbProperty("params")
	private CreateMessageRequestParameters parameters;

	public CreateMessageRequestParameters getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(CreateMessageRequestParameters parameters) {
		this.parameters = parameters;
	}

	public static final class CreateMessageRequestParameters implements Serializable {
		
		@JsonbProperty
		private SamplingMessage[] messages;
		
		@JsonbProperty
		private ModelPreferences modelPreferences;
		
		@JsonbProperty
		private String systemPrompt;
		
		@JsonbProperty
		private IncludeContext includeContext;

		@JsonbProperty
		private Double temperature;
		
		@JsonbProperty
		private Long maxTokens;
		
		@JsonbProperty
		private String[] stopSequences;
		
		@JsonbProperty
		private Object metadata;

		/**
		 * @return the messages
		 */
		public SamplingMessage[] getMessages() {
			return messages;
		}

		/**
		 * @param messages the messages to set
		 */
		public void setMessages(SamplingMessage[] messages) {
			this.messages = messages;
		}

		/**
		 * @return the modelPreferences
		 */
		public ModelPreferences getModelPreferences() {
			return modelPreferences;
		}

		/**
		 * @param modelPreferences the modelPreferences to set
		 */
		public void setModelPreferences(ModelPreferences modelPreferences) {
			this.modelPreferences = modelPreferences;
		}

		/**
		 * @return the systemPrompt
		 */
		public String getSystemPrompt() {
			return systemPrompt;
		}

		/**
		 * @param systemPrompt the systemPrompt to set
		 */
		public void setSystemPrompt(String systemPrompt) {
			this.systemPrompt = systemPrompt;
		}

		/**
		 * @return the includeContext
		 */
		public IncludeContext getIncludeContext() {
			return includeContext;
		}

		/**
		 * @param includeContext the includeContext to set
		 */
		public void setIncludeContext(IncludeContext includeContext) {
			this.includeContext = includeContext;
		}

		/**
		 * @return the temperature
		 */
		public Double getTemperature() {
			return temperature;
		}

		/**
		 * @param temperature the temperature to set
		 */
		public void setTemperature(Double temperature) {
			this.temperature = temperature;
		}

		/**
		 * @return the maxTokens
		 */
		public Long getMaxTokens() {
			return maxTokens;
		}

		/**
		 * @param maxTokens the maxTokens to set
		 */
		public void setMaxTokens(Long maxTokens) {
			this.maxTokens = maxTokens;
		}

		/**
		 * @return the stopSequences
		 */
		public String[] getStopSequences() {
			return stopSequences;
		}

		/**
		 * @param stopSequences the stopSequences to set
		 */
		public void setStopSequences(String[] stopSequences) {
			this.stopSequences = stopSequences;
		}

		/**
		 * @return the metadata
		 */
		public Object getMetadata() {
			return metadata;
		}

		/**
		 * @param metadata the metadata to set
		 */
		public void setMetadata(Object metadata) {
			this.metadata = metadata;
		}
	}
	
	public static enum IncludeContext {
		NONE("none")
		,THIS_SERVER("thisServer")
		,ALL_SERVERS("allServers")
		;
		private final String context;

		/**
		 * @param context
		 */
		private IncludeContext(final String context) {
			this.context = context;
		}
		
		public static IncludeContext of(final String value) {
			for (IncludeContext icontext : values()) {
				if (icontext.context.equals(value)) return icontext;
			}
			
			throw new IllegalArgumentException("Unrecognised include context '" + value + "'.");
		}

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return context;
		}
	}
}
