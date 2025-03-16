package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public abstract class Annotated implements Serializable {
	
	@JsonbProperty
	private Annotations annotations;

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

	public static class Annotations implements Serializable {
		
		@JsonbProperty
		private Role[] audience;
		
		@JsonbProperty
		private Double priority;

		/**
		 * @return the audience
		 */
		public Role[] getAudience() {
			return audience;
		}

		/**
		 * @param audience the audience to set
		 */
		public void setAudience(Role[] audience) {
			this.audience = audience;
		}

		/**
		 * @return the priority
		 */
		public Double getPriority() {
			return priority;
		}

		/**
		 * @param priority the priority to set
		 */
		public void setPriority(Double priority) {
			this.priority = priority;
		}
	}
}
