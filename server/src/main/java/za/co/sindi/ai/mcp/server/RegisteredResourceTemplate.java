package za.co.sindi.ai.mcp.server;

import java.io.Serializable;
import java.util.Objects;

import za.co.sindi.ai.mcp.schema.ListResourceTemplatesResult;
import za.co.sindi.ai.mcp.schema.ResourceTemplate;
import za.co.sindi.ai.mcp.shared.RequestHandler;

/**
 * @author Buhake Sindi
 * @since 15 March 2025
 */
public class RegisteredResourceTemplate implements Serializable {

	private ResourceTemplate resourceTemplate;
	private RequestHandler<ListResourceTemplatesResult> readCallback;
	
	/**
	 * @param resourceTemplate
	 * @param readCallback
	 */
	public RegisteredResourceTemplate(ResourceTemplate resourceTemplate, RequestHandler<ListResourceTemplatesResult> readCallback) {
		super();
		this.resourceTemplate = Objects.requireNonNull(resourceTemplate, "A resource template is required.");
		this.readCallback = Objects.requireNonNull(readCallback, "A read callback handler is required.");
	}

	/**
	 * @return the resourceTemplate
	 */
	public ResourceTemplate getResourceTemplate() {
		return resourceTemplate;
	}

	/**
	 * @return the readCallback
	 */
	public RequestHandler<ListResourceTemplatesResult> getReadCallback() {
		return readCallback;
	}
}
