package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 04 January 2026
 */
public class ToolExecution implements Serializable {
	
	@JsonbProperty
	private TaskSupport taskSupport;
	
	/**
	 * @return the taskSupport
	 */
	public TaskSupport getTaskSupport() {
		return taskSupport;
	}

	/**
	 * @param taskSupport the taskSupport to set
	 */
	public void setTaskSupport(TaskSupport taskSupport) {
		this.taskSupport = taskSupport;
	}

	public static enum TaskSupport {
		FORBIDDEN("forbidden"),
		OPTIONAL("optional"),
		REQUIRED("required")
		;
		private final String support;

		/**
		 * @param support
		 */
		private TaskSupport(final String support) {
			this.support = support;
		}
		
		public static TaskSupport of(final String value) {
			for (TaskSupport support : values()) {
				if (support.support.equals(value)) return support;
			}
			
			throw new IllegalArgumentException("Invalid task support '" + value + "'.");
		}

		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return support;
		}
	}
}
