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
	private Completions completions;
	
	@JsonbProperty
	public Prompts prompts;
	
	@JsonbProperty
	private Resources resources;
	
	@JsonbProperty
	private Tools tools;
	
	@JsonbProperty
	private Tasks tasks;
	
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
	 * @param completions
	 * @param prompts
	 * @param resources
	 * @param tools
	 * @param tasks
	 */
	public ServerCapabilities(Map<String, Object> experimental, Logging logging, Completions completions,
			Prompts prompts, Resources resources, Tools tools, Tasks tasks) {
		super();
		this.experimental = experimental;
		this.logging = logging;
		this.completions = completions;
		this.prompts = prompts;
		this.resources = resources;
		this.tools = tools;
		this.tasks = tasks;
	}

	/**
	 * 
	 * @param other
	 */
	private ServerCapabilities(final ServerCapabilities other) {
		this(other.experimental, other.logging, other.completions, other.prompts, other.resources, other.tools, other.tasks);
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
	 * @return the completions
	 */
	public Completions getCompletions() {
		return completions;
	}

	/**
	 * @param completions the completions to set
	 */
	public void setCompletions(Completions completions) {
		this.completions = completions;
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

	/**
	 * @return the tasks
	 */
	public Tasks getTasks() {
		return tasks;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(Tasks tasks) {
		this.tasks = tasks;
	}

	public static class Logging implements Serializable {
		
	}
	
	public static class Completions implements Serializable {
		
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
	
	public static final class Tasks implements Serializable {
		
		@JsonbProperty
		private Object lists;
		
		@JsonbProperty
		private Object cancel;
		
		@JsonbProperty
		private Requests requests;
		
		/**
		 * @return the lists
		 */
		public Object getLists() {
			return lists;
		}

		/**
		 * @param lists the lists to set
		 */
		public void setLists(Object lists) {
			this.lists = lists;
		}

		/**
		 * @return the cancel
		 */
		public Object getCancel() {
			return cancel;
		}

		/**
		 * @param cancel the cancel to set
		 */
		public void setCancel(Object cancel) {
			this.cancel = cancel;
		}

		/**
		 * @return the requests
		 */
		public Requests getRequests() {
			return requests;
		}

		/**
		 * @param requests the requests to set
		 */
		public void setRequests(Requests requests) {
			this.requests = requests;
		}

		public static final class Requests implements Serializable {
			
			@JsonbProperty
			private Tools tools;
			
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

			public static final class Tools implements Serializable {
				
				@JsonbProperty
				private Object call;

				/**
				 * @return the call
				 */
				public Object getCall() {
					return call;
				}

				/**
				 * @param call the call to set
				 */
				public void setCall(Object call) {
					this.call = call;
				}
			}
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
		
		public Builder completions() {
			capabilities.setCompletions(new Completions());
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
		
		public Builder tasks(final Tasks tasks) {
			if (tasks != null) capabilities.setTasks(tasks);
			return this;
		}
		
		public Builder enableAll() {
			return logging().completions().prompts(true).resources(true).tools(true).tasks(new Tasks());
		}
		
		public ServerCapabilities build() {
			return new ServerCapabilities(capabilities);
		}
	}
}
