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
	private Object elicitation;
	
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
	 */
	public ClientCapabilities(Map<String, Object> experimental, Roots roots, Object sampling, Object elicitation) {
		super();
		this.experimental = experimental;
		this.roots = roots;
		this.sampling = sampling;
		this.elicitation = elicitation;
	}

	private ClientCapabilities(final ClientCapabilities other) {
		super();
		this.experimental = other.experimental;
		this.roots = other.roots;
		this.sampling = other.sampling;
		this.elicitation = other.elicitation;
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
	public Object getElicitation() {
		return elicitation;
	}

	/**
	 * @param elicitation the elicitation to set
	 */
	public void setElicitation(Object elicitation) {
		this.elicitation = elicitation;
	}

	public static class Roots implements Serializable {
		
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
	
	public static class Sampling implements Serializable {
		
	}
	
	public static class Elicitation implements Serializable {
		
	}
	
	public static class Builder {
		
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
			capabilities.setElicitation(new Elicitation());
			return this;
		}
		
		public ClientCapabilities build() {
			return new ClientCapabilities(capabilities);
		}
	}
}
