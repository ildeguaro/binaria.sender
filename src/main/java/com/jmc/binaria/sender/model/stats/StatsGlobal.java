package com.jmc.binaria.sender.model.stats;

public class StatsGlobal {
	
	private long campaignId;
	
	private String event;
	
	private String descriptionEvent;
	
	private String iconEvent;
	
	private int count;

	public long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getDescriptionEvent() {
		return descriptionEvent;
	}

	public void setDescriptionEvent(String descriptionEvent) {
		this.descriptionEvent = descriptionEvent;
	}

	public String getIconEvent() {
		return iconEvent;
	}

	public void setIconEvent(String iconEvent) {
		this.iconEvent = iconEvent;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
