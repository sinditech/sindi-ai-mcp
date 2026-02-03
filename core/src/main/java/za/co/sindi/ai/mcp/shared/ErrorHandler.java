/**
 * 
 */
package za.co.sindi.ai.mcp.shared;

/**
 * @author Buhake Sindi
 * @since 02 April 2025
 */
@FunctionalInterface
public interface ErrorHandler {

	public void onError(Throwable exception);
}
