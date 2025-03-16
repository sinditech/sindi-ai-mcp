package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;
import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public class ServerCapabilities implements Serializable {

	@JsonbProperty
	private Map<String, Object> experimental;
	
	@JsonbProperty
	public Logging logging;
	
	@JsonbProperty
	public Prompts prompts;
	
	@JsonbProperty
	private Resources resources;
	
	@JsonbProperty
	private Tools tools;
	
	/**
	 * 
	 */
	public ServerCapabilities() {
		super();
		//TODO Auto-generated constructor stub
	}

	/**
	 * @param experimental
	 * @param logging
	 * @param prompts
	 * @param resources
	 * @param tools
	 */
	public ServerCapabilities(Map<String, Object> experimental, Logging logging, Prompts prompts, Resources resources,
			Tools tools) {
		super();
		this.experimental = experimental;
		this.logging = logging;
		this.prompts = prompts;
		this.resources = resources;
		this.tools = tools;
	}
	
	private ServerCapabilities(final ServerCapabilities other) {
		super();
		this.experimental = other.experimental;
		this.logging = other.logging;
		this.prompts = other.prompts;
		this.resources = other.resources;
		this.tools = other.tools;
	}

	/**
	 * @return the experimental
	 */
	public Map<String, Object> getExperimental() {
		return experimental;
	}

	/**
	 * @param experimental the experimental to set
	 */
	public void setExperimental(Map<String, Object> experimental) {
		this.experimental = experimental;
	}

	/**
	 * @return the logging
	 */
	public Logging getLogging() {
		return logging;
	}

	/**
	 * @param logging the logging to set
	 */
	public void setLogging(Logging logging) {
		this.logging = logging;
	}

	/**
	 * @return the prompts
	 */
	public Prompts getPrompts() {
		return prompts;
	}

	/**
	 * @param prompts the prompts to set
	 */
	public void setPrompts(Prompts prompts) {
		this.prompts = prompts;
	}

	/**
	 * @return the resources
	 */
	public Resources getResources() {
		return resources;
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(Resources resources) {
		this.resources = resources;
	}

	/**
	 * @return the tools
	 */
	public Tools getTools() {
		return tools;
	}

	/**
	 * @param tools the tools to set
	 */
	public void setTools(Tools tools) {
		this.tools = tools;
	}

	public static class Logging implements Serializable {
		
	}
	
	public static class Prompts implements Serializable {
		
		@JsonbProperty
		private Boolean listChanged;

		/**
		 * @return the listChanged
		 */
		public Boolean getListChanged() {
			return listChanged;
		}

		/**
		 * @param listChanged the listChanged to set
		 */
		public void setListChanged(Boolean listChanged) {
			this.listChanged = listChanged;
		}
	}
	
	public static class Resources implements Serializable {
		
		@JsonbProperty
		private Boolean subscribe;
		
		@JsonbProperty
		private Boolean listChanged;

		/**
		 * @return the subscribe
		 */
		public Boolean getSubscribe() {
			return subscribe;
		}

		/**
		 * @param subscribe the subscribe to set
		 */
		public void setSubscribe(Boolean subscribe) {
			this.subscribe = subscribe;
		}

		/**
		 * @return the listChanged
		 */
		public Boolean getListChanged() {
			return listChanged;
		}

		/**
		 * @param listChanged the listChanged to set
		 */
		public void setListChanged(Boolean listChanged) {
			this.listChanged = listChanged;
		}
	}
	
	public static class Tools implements Serializable {
		
		@JsonbProperty
		private Boolean listChanged;

		/**
		 * @return the listChanged
		 */
		public Boolean getListChanged() {
			return listChanged;
		}

		/**
		 * @param listChanged the listChanged to set
		 */
		public void setListChanged(Boolean listChanged) {
			this.listChanged = listChanged;
		}
	}
	
	public static class Builder {
		
		private ServerCapabilities capabilities = new ServerCapabilities();
		
		public Builder experimental(Map<String, Object> experimental) {
			capabilities.setExperimental(experimental);
			return this;
		}
		
		public Builder logging() {
			capabilities.setLogging(new Logging());
			return this;
		}
		
		public Builder prompts(final boolean listChanged) {
			capabilities.setPrompts(new Prompts());
			capabilities.getPrompts().setListChanged(listChanged);
			return this;
		}
		
		public Builder resources(final boolean listChanged) {
			capabilities.setResources(new Resources());
			capabilities.getResources().setListChanged(listChanged);
			return this;
		}
		
		public Builder tools(final boolean listChanged) {
			capabilities.setTools(new Tools());
			capabilities.getTools().setListChanged(listChanged);
			return this;
		}
		
		public ServerCapabilities build() {
			return new ServerCapabilities(capabilities);
		}
	}
}
