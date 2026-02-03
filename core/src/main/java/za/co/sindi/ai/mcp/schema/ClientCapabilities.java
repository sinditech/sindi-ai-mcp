package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;
import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public class ClientCapabilities implements Serializable {

	@JsonbProperty
	private Map<String, Object> experimental;
	
	@JsonbProperty
	public Roots roots;
	
	@JsonbProperty
	public Object sampling;
	
	@JsonbProperty
	private Elicitation elicitation;
	
	@JsonbProperty
	private Tasks tasks;
	
	/**
	 * 
	 */
	public ClientCapabilities() {
		super();
		//TODO Auto-generated constructor stub
	}

	/**
	 * @param experimental
	 * @param roots
	 * @param sampling
	 * @param elicitation
	 * @param tasks
	 */
	public ClientCapabilities(Map<String, Object> experimental, Roots roots, Object sampling, Elicitation elicitation, Tasks tasks) {
		super();
		this.experimental = experimental;
		this.roots = roots;
		this.sampling = sampling;
		this.elicitation = elicitation;
		this.tasks = tasks;
	}

	/**
	 * 
	 * @param other
	 */
	private ClientCapabilities(final ClientCapabilities other) {
		this(other.experimental, other.roots, other.sampling, other.elicitation, other.tasks);
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
	 * @return the roots
	 */
	public Roots getRoots() {
		return roots;
	}

	/**
	 * @param roots the roots to set
	 */
	public void setRoots(Roots roots) {
		this.roots = roots;
	}

	/**
	 * @return the sampling
	 */
	public Object getSampling() {
		return sampling;
	}

	/**
	 * @param sampling the sampling to set
	 */
	public void setSampling(Object sampling) {
		this.sampling = sampling;
	}

	/**
	 * @return the elicitation
	 */
	public Elicitation getElicitation() {
		return elicitation;
	}

	/**
	 * @param elicitation the elicitation to set
	 */
	public void setElicitation(Elicitation elicitation) {
		this.elicitation = elicitation;
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

	public static final class Roots implements Serializable {
		
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
	
	public static final class Sampling implements Serializable {
		
		@JsonbProperty
		private Object context;
		
		@JsonbProperty
		private Object tools;

		/**
		 * @return the context
		 */
		public Object getContext() {
			return context;
		}

		/**
		 * @param context the context to set
		 */
		public void setContext(Object context) {
			this.context = context;
		}

		/**
		 * @return the tools
		 */
		public Object getTools() {
			return tools;
		}

		/**
		 * @param tools the tools to set
		 */
		public void setTools(Object tools) {
			this.tools = tools;
		}
	}
	
	public static final class Elicitation implements Serializable {
		
		@JsonbProperty
		private Object form;
		
		@JsonbProperty
		private Object url;

		/**
		 * @return the form
		 */
		public Object getForm() {
			return form;
		}

		/**
		 * @param form the form to set
		 */
		public void setForm(Object form) {
			this.form = form;
		}

		/**
		 * @return the url
		 */
		public Object getUrl() {
			return url;
		}

		/**
		 * @param url the url to set
		 */
		public void setUrl(Object url) {
			this.url = url;
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
			private Sampling sampling;
			
			@JsonbProperty
			private Elicitation elicitation;
			
			/**
			 * @return the sampling
			 */
			public Sampling getSampling() {
				return sampling;
			}

			/**
			 * @param sampling the sampling to set
			 */
			public void setSampling(Sampling sampling) {
				this.sampling = sampling;
			}

			/**
			 * @return the elicitation
			 */
			public Elicitation getElicitation() {
				return elicitation;
			}

			/**
			 * @param elicitation the elicitation to set
			 */
			public void setElicitation(Elicitation elicitation) {
				this.elicitation = elicitation;
			}

			public static final class Sampling implements Serializable {
				
				@JsonbProperty
				private Object createMessage;

				/**
				 * @return the createMessage
				 */
				public Object getCreateMessage() {
					return createMessage;
				}

				/**
				 * @param createMessage the createMessage to set
				 */
				public void setCreateMessage(Object createMessage) {
					this.createMessage = createMessage;
				}
			}
			
			public static final class Elicitation implements Serializable {
				
				@JsonbProperty
				private Object create;

				/**
				 * @return the create
				 */
				public Object getCreate() {
					return create;
				}

				/**
				 * @param create the create to set
				 */
				public void setCreate(Object create) {
					this.create = create;
				}
			}
		}
	}
	
	public static final class Builder {
		
		private ClientCapabilities capabilities = new ClientCapabilities();
		
		public Builder experimental(Map<String, Object> experimental) {
			capabilities.setExperimental(experimental);
			return this;
		}
		
		public Builder roots(final boolean listChanged) {
			capabilities.setRoots(new Roots());
			capabilities.getRoots().setListChanged(listChanged);
			return this;
		}
		
		public Builder sampling() {
			capabilities.setSampling(new Sampling());
			return this;
		}
		
		public Builder elicitation() {
			if (capabilities.getElicitation() == null)
				capabilities.setElicitation(new Elicitation());
			return this;
		}
		
		public Builder formElicitation() {
			elicitation();
			capabilities.getElicitation().setForm(new Object());
			return this;
		}
		
		public Builder urlElicitation() {
			elicitation();
			capabilities.getElicitation().setUrl(new Object());
			return this;
		}
		
		public Builder tasks(final Tasks tasks) {
			if (tasks != null) capabilities.setTasks(tasks);
			return this;
		}
		
		public ClientCapabilities build() {
			return new ClientCapabilities(capabilities);
		}
	}
}
