package za.co.sindi.ai.mcp.schema;

import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 07 September 2025
 */
public final class ElicitResult implements ClientResult {
	
	@JsonbProperty
	private Action action;
	
	@JsonbProperty
	private Map<String, Object> content;

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * @return the content
	 */
	public Map<String, Object> getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(Map<String, Object> content) {
		this.content = content;
	}

	public static enum Action {
		ACCEPT("accept")
		,DECLINE("decline")
		,CANCEL("cancel")
		;
		private final String action;

		/**
		 * @param action
		 */
		private Action(final String action) {
			this.action = action;
		}
		
		public static Action of(final String value) {
			for (Action action : values()) {
				if (action.action.equals(value)) return action;
			}
			
			throw new IllegalArgumentException("Invalid Action '" + value + "'.");
		}

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return action;
		}
	}
}
