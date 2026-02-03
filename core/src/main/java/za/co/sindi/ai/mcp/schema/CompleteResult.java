package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class CompleteResult extends SamplingMessage implements ServerResult {
	
	@JsonbProperty
	private Completion completion;
	
	/**
	 * @return the completion
	 */
	public Completion getCompletion() {
		return completion;
	}

	/**
	 * @param completion the completion to set
	 */
	public void setCompletion(Completion completion) {
		this.completion = completion;
	}

	public static final class Completion implements Serializable {
		
		@JsonbProperty
		private String[] values;
		
		@JsonbProperty
		private Long total;
		
		@JsonbProperty("hasMore")
		private Boolean hasMore;

		/**
		 * @return the values
		 */
		public String[] getValues() {
			return values;
		}

		/**
		 * @param values the values to set
		 */
		public void setValues(String[] values) {
			this.values = values;
		}

		/**
		 * @return the total
		 */
		public Long getTotal() {
			return total;
		}

		/**
		 * @param total the total to set
		 */
		public void setTotal(Long total) {
			this.total = total;
		}

		/**
		 * @return the hasMore
		 */
		public Boolean getHasMore() {
			return hasMore;
		}

		/**
		 * @param hasMore the hasMore to set
		 */
		public void setHasMore(Boolean hasMore) {
			this.hasMore = hasMore;
		}
	}
}
