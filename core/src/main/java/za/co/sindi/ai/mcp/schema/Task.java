package za.co.sindi.ai.mcp.schema;

import java.io.Serializable;
import java.time.Instant;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 04 January 2026
 */
public class Task implements Serializable {
	
	@JsonbProperty
	private String taskId;
	
	@JsonbProperty
	private TaskStatus status;
	
	@JsonbProperty
	private String statusMessage;
	
	@JsonbProperty
	private Instant createdAt;
	
	@JsonbProperty
	private Instant lastUpdateAt;
	
	@JsonbProperty
	private Long ttl;
	
	@JsonbProperty
	private Long pollInterval;

	/**
	 * 
	 */
	public Task() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param taskId
	 * @param status
	 * @param createdAt
	 */
	public Task(String taskId, TaskStatus status, Instant createdAt) {
		super();
		this.taskId = taskId;
		this.status = status;
		this.createdAt = createdAt;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @return the status
	 */
	public TaskStatus getStatus() {
		return status;
	}

	/**
	 * @return the createdAt
	 */
	public Instant getCreatedAt() {
		return createdAt;
	}

	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * @param statusMessage the statusMessage to set
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	/**
	 * @return the lastUpdateAt
	 */
	public Instant getLastUpdateAt() {
		return lastUpdateAt;
	}

	/**
	 * @param lastUpdateAt the lastUpdateAt to set
	 */
	public void setLastUpdateAt(Instant lastUpdateAt) {
		this.lastUpdateAt = lastUpdateAt;
	}

	/**
	 * @return the ttl
	 */
	public Long getTtl() {
		return ttl;
	}

	/**
	 * @param ttl the ttl to set
	 */
	public void setTtl(Long ttl) {
		this.ttl = ttl;
	}

	/**
	 * @return the pollInterval
	 */
	public Long getPollInterval() {
		return pollInterval;
	}

	/**
	 * @param pollInterval the pollInterval to set
	 */
	public void setPollInterval(Long pollInterval) {
		this.pollInterval = pollInterval;
	}
}
