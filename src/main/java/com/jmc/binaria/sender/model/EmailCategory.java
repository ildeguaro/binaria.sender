package com.jmc.binaria.sender.model;

public class EmailCategory {
	
	private long id;	

	private long emailCampaingId;
	
	private String categoryName;
	
	public EmailCategory(long emailCampaingId, String categoryName) {
		this.emailCampaingId = emailCampaingId;
		this.categoryName = categoryName;
	}

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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
