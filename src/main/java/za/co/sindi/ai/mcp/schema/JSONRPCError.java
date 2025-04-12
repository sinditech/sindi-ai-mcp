package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class JSONRPCError extends JSONRPCMessage {
	
	@JsonbProperty
	private RequestId id;
	
	@JsonbProperty
	private Error error;

	/**
	 * @return the id
	 */
	public RequestId getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(RequestId id) {
		this.id = id;
	}

	/**
	 * @return the error
	 */
	public Error getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(Error error) {
		this.error = error;
	}

	public static class Error implements Serializable {

		@JsonbProperty
		private int code;
		
		@JsonbProperty
		private String message;
		
		@JsonbProperty
		private Object data;

		/**
		 * @return the code
		 */
		public int getCode() {
			return code;
		}

		/**
		 * @param code the code to set
		 */
		public void setCode(int code) {
			this.code = code;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @param message the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}

		/**
		 * @return the data
		 */
		public Object getData() {
			return data;
		}

		/**
		 * @param data the data to set
		 */
		public void setData(Object data) {
			this.data = data;
		}
	}
}
