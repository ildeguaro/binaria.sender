package com.jmc.binaria.sender.model.api.client;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailEventResponse {
	
	@JsonProperty(value="_id", index=2)
	private String id;
	
	private String email;
	
	private long timestamp;
	
	@JsonProperty("sg_message_id")
	private String messageId;
	
	@JsonProperty("sg_event_id")
	private String eventId;
	
	private String event;
	
	private String smtpId;
	
	@JsonIgnore
	private String date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Date getDate() {
		return new Date(this.timestamp);
	}

	public void setDate(String date) {
		this.date = date;
	}
}
