package com.jmc.binaria.sender.model.stats;

import java.util.List;

public class StatsSent {
	
	private long campaignId;
	
	private String category;
	
	private int count;
	
	private List<StatsByCategory> detail;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<StatsByCategory> getDetail() {
		return detail;
	}

	public void setDetail(List<StatsByCategory> detail) {
		this.detail = detail;
	}

}
