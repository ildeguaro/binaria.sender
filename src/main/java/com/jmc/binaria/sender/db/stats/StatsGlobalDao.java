package com.jmc.binaria.sender.db.stats;

import java.util.List;

import com.jmc.binaria.sender.model.stats.StatsGlobal;

public interface StatsGlobalDao {
	
	List<StatsGlobal> finbByCampaignId(long campaignId);
	
	int getSentCount(long campaignId);

}
