package za.co.sindi.ai.mcp.server;

import java.io.Serializable;
import java.util.Objects;

import za.co.sindi.ai.mcp.schema.GetPromptResult;
import za.co.sindi.ai.mcp.schema.Prompt;
import za.co.sindi.ai.mcp.shared.RequestHandler;

/**
 * @author Buhake Sindi
 * @since 15 March 2025
 */
public class RegisteredPrompt implements Serializable {

	private Prompt prompt;
	private RequestHandler<GetPromptResult> messageProvider;
	
	/**
	 * @param prompt
	 * @param messageProvider
	 */
	public RegisteredPrompt(Prompt prompt, RequestHandler<GetPromptResult> messageProvider) {
		super();
		this.prompt = Objects.requireNonNull(prompt, "A prompt is required.");
		this.messageProvider = Objects.requireNonNull(messageProvider, "A message provider is required.");
	}

	/**
	 * @return the prompt
	 */
	public Prompt getPrompt() {
		return prompt;
	}

	/**
	 * @return the messageProvider
	 */
	public RequestHandler<GetPromptResult> getMessageProvider() {
		return messageProvider;
	}
}
