package com.jmc.binaria.sender.model;

import java.util.Date;

public class EmailEvent {
	
	private long id;
	
	private long emailCampaingId;
	
	private String esmtpId;
	
	private String eventType;
	
	private Date eventDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEmailCampaingId() {
		return emailCampaingId;
	}

	public void setEmailCampaingId(long emailCampaingId) {
		this.emailCampaingId = emailCampaingId;
	}

	public String getEsmtpId() {
		return esmtpId;
	}

	public void setEsmtpId(String esmtpId) {
		this.esmtpId = esmtpId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

}
