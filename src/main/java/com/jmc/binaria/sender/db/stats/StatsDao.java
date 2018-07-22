package com.jmc.binaria.sender.db.stats;

import java.util.List;

import com.jmc.binaria.sender.model.stats.StatsByCategory;
import com.jmc.binaria.sender.model.stats.StatsGlobal;
import com.jmc.binaria.sender.model.stats.StatsSent;

public interface StatsDao {
	
	List<StatsGlobal> finbByCampaignId(long campaignId);
	
	int getSentCount(long campaignId);
	
	List<StatsByCategory> findByCategory(long campaignId, String category);
	
	List<StatsSent> findAllSents(long campaignId);

}
