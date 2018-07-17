package com.jmc.binaria.sender.db;

import com.jmc.binaria.sender.model.Customer;

import java.util.List;

import com.jmc.binaria.sender.model.Campaign;

public interface CampaignDao {

	Campaign createCampaing(Customer customer, String nameCampaig, Long ordenImpresionId, String emailTemplate);
	
	String getUUIDFromSQL();
	
	Campaign findCampaignByUiid(String uuid);
	
	List<Campaign> allCampaign();
	
	List<Campaign> findCampaignTop10();

}
