package com.jmc.binaria.sender.db;

import java.util.List;
import java.util.Map;

import com.jmc.binaria.sender.model.EmailCampaign;
import com.jmc.binaria.sender.model.Sender;

public interface EmailCampaignDao {

	EmailCampaign createEmailCampaing(EmailCampaign emailCampaign);
	
	EmailCampaign updateSending(EmailCampaign emailCampaign);
	
	List<EmailCampaign> selectEmailToSend(int quantity, int senderId);
	
	EmailCampaign selectEmailToSendAssigned(int senderId);
	
	List<EmailCampaign> selectEmailToSendNoAssigned(int quantity, Sender sender);
	
	EmailCampaign findEmailCampaignByColumns(Map<String, String> columns);
	
	List<EmailCampaign> findEmailCampaignByOrdenId(long ordenId);
	
	List<EmailCampaign> findEmailCampaignByFields(String words);
	
	List<EmailCampaign> findEmailCampaignByAddress(String address);
	
	List<EmailCampaign> findEmailCampaignByName(String name);
	
	List<EmailCampaign> findEmailCampaignByOrdenIdAddressNamesOrFieldsSearch(long ordenId, String address, String names, String searchFields);

	boolean getEmailSent(EmailCampaign email);

}
