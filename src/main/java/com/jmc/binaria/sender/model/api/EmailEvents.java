package com.jmc.binaria.sender.model.api;

import java.util.Date;

public class EmailEvents {
	
	public static String PROCESSED_TYPE = "processed";
	
	public static String OPEN_TYPE = "open"; 
	
	public static String DELIVERED_TYPE = "delivered";
	
	private String id;
	
	private String email;
	
	private String recipent;
	
	private Date sent;
	
	private boolean processed;
	
	private Date processedDate;
	
	private boolean delivered;
	
	private Date deliveredDate;
	
	private boolean open;
	
	private Date openDate;
	
	private int clicks;

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

	public String getRecipent() {
		return recipent;
	}

	public void setRecipent(String recipent) {
		this.recipent = recipent;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	public Date getDeliveredDate() {
		return deliveredDate;
	}

	public void setDeliveredDate(Date deliveredDate) {
		this.deliveredDate = deliveredDate;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean opened) {
		this.open = opened;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public int getClicks() {
		return clicks;
	}

	public void setClicks(int clicks) {
		this.clicks = clicks;
	}
	
	public Date getSent() {
		return sent;
	}

	public void setSent(Date sent) {
		this.sent = sent;
	}
	

}
