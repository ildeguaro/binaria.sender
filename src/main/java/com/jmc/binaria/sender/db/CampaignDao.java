package com.jmc.binaria.sender.db;

import com.jmc.binaria.sender.model.Customer;
import com.jmc.binaria.sender.model.Campaign;

public interface CampaignDao {

	Campaign createCampaing(Customer customer, String nameCampaig, Long ordenImpresionId);
	
	String getUUIDFromSQL();
	
	Campaign findCampaignByUiid(String uuid);

}
