package za.co.sindi.ai.mcp.server;

import java.io.Serializable;
import java.util.Objects;

import za.co.sindi.ai.mcp.schema.ReadResourceResult;
import za.co.sindi.ai.mcp.schema.Resource;
import za.co.sindi.ai.mcp.shared.RequestHandler;

/**
 * @author Buhake Sindi
 * @since 15 March 2025
 */
public class RegisteredResource implements Serializable {

	private Resource resource;
	private RequestHandler<ReadResourceResult> readHandler;
	
	/**
	 * @param resource
	 * @param readHandler
	 */
	public RegisteredResource(Resource resource, RequestHandler<ReadResourceResult> readHandler) {
		super();
		this.resource = Objects.requireNonNull(resource, "A resource is required.");
		this.readHandler = Objects.requireNonNull(readHandler, "A read handler is required.");
	}

	/**
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * @return the readHandler
	 */
	public RequestHandler<ReadResourceResult> getReadHandler() {
		return readHandler;
	}
}
