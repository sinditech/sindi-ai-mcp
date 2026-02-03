package za.co.sindi.ai.mcp.schema;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
@JsonbTypeInfo(
	key = "method",
	value = {
	    @JsonbSubtype(alias=CancelledNotification.METHOD_NOTIFICATION_CANCELLED, type=CancelledNotification.class),
	    @JsonbSubtype(alias=ElicitationCompleteNotification.METHOD_NOTIFICATION_LOGGING_MESSAGE, type=ElicitationCompleteNotification.class),
	    @JsonbSubtype(alias=InitializedNotification.METHOD_NOTIFICATION_INITIALIZED, type=InitializedNotification.class),
	    @JsonbSubtype(alias=LoggingMessageNotification.METHOD_NOTIFICATION_LOGGING_MESSAGE, type=LoggingMessageNotification.class),
	    @JsonbSubtype(alias=ProgressNotification.METHOD_NOTIFICATION_PROGRESS, type=ProgressNotification.class),
	    @JsonbSubtype(alias=PromptListChangedNotification.METHOD_NOTIFICATION_PROMPTS_lIST_CHANGED, type=PromptListChangedNotification.class),
	    @JsonbSubtype(alias=ResourceListChangedNotification.METHOD_NOTIFICATION_RESOURCES_lIST_CHANGED, type=ResourceListChangedNotification.class),
	    @JsonbSubtype(alias=ResourceUpdatedNotification.METHOD_RESOURCES_UPDATED, type=ResourceUpdatedNotification.class),
	    @JsonbSubtype(alias=RootsListChangedNotification.METHOD_NOTIFICATION_ROOTS_lIST_CHANGED, type=RootsListChangedNotification.class),
	    @JsonbSubtype(alias=TaskStatusNotification.METHOD_NOTIFICATION_TASK_STATUS, type=TaskStatusNotification.class),
	    @JsonbSubtype(alias=ToolListChangedNotification.METHOD_NOTIFICATION_TOOLS_lIST_CHANGED, type=ToolListChangedNotification.class),
	}
)
public abstract class BaseNotification implements Notification {

}
