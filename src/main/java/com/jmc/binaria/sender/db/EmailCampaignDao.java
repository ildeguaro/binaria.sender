package com.jmc.binaria.sender.db;

import java.util.List;
import java.util.Map;

import com.jmc.binaria.sender.model.EmailCampaign;

public interface EmailCampaignDao {

	EmailCampaign createEmailCampaing(EmailCampaign emailCampaign);
	
	EmailCampaign updateSending(EmailCampaign emailCampaign);
	
	List<EmailCampaign> selectEmailToSend(long ordenId, int quantity, int senderId);
	
	EmailCampaign findEmailCampaignByColumns(Map<String, String> columns);

}
