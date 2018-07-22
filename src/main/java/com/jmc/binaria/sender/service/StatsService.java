package com.jmc.binaria.sender.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jmc.binaria.sender.db.stats.StatsDao;
import com.jmc.binaria.sender.db.stats.StatsDaoImpl;
import com.jmc.binaria.sender.model.stats.StatsByCategory;
import com.jmc.binaria.sender.model.stats.StatsSent;

public class StatsService {

	static Logger logger = LoggerFactory.getLogger(StatsService.class);

	private StatsDao statsDao;

	public StatsService() {
	    statsDao = new StatsDaoImpl();
	}
	
	public List<StatsByCategory> getByCategory(long campaignId, String category){
		return statsDao.findByCategory(campaignId, category);
	}
	
	public List<StatsSent> getStatsSent(long campaignId){
		List<StatsSent> stats = statsDao.findAllSents(campaignId);
		stats.forEach( i -> {
			i.setDetail(this.getByCategory(i.getCampaignId(), i.getCategory()));
		});
		return stats;
	}

}
