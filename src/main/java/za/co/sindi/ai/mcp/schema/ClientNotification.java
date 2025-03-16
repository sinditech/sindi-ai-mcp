package za.co.sindi.ai.mcp.schema;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public sealed interface ClientNotification extends Notification permits CancelledNotification, ProgressNotification, InitializedNotification, RootsListChangedNotification {

}
