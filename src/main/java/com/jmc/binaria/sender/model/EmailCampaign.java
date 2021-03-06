package com.jmc.binaria.sender.model;

import java.util.Date;

public class EmailCampaign {

	private long id;
	
	private long campaignId;
	
	private String addresses;
	
	private String names;
	
	private String packageId; 
	
	private String documentId;
	
	private String attachmentPath;
	
	private String contentEmail;
	
	private Date sendingDate;
	
	private boolean wasSent;
	
	private String esmtpId;
	
	private String response;
	
	private String error; 
	
	private String fieldsSearch;
	
	private int senderId;
	
	private String campaignUuid;
	
	private String campaignName;
	
	private Date campaignDate;
	
	private int senderIdAssinged;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public String getContentEmail() {
		return contentEmail;
	}

	public void setContentEmail(String contentEmail) {
		this.contentEmail = contentEmail;
	}

	public Date getSendingDate() {
		return sendingDate;
	}

	public void setSendingDate(Date sendingDate) {
		this.sendingDate = sendingDate;
	}

	public boolean isWasSent() {
		return wasSent;
	}

	public void setWasSent(boolean wasSent) {
		this.wasSent = wasSent;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getError() {
		return error;
	}

	public String getEsmtpId() {
		return esmtpId;
	}

	public void setEsmtpId(String esmtpId) {
		this.esmtpId = esmtpId;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getFieldsSearch() {
		return fieldsSearch;
	}

	public void setFieldsSearch(String fieldsSearch) {
		this.fieldsSearch = fieldsSearch;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public String getCampaignUuid() {
		return campaignUuid;
	}

	public void setCampaignUuid(String campaignUuid) {
		this.campaignUuid = campaignUuid;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public Date getCampaignDate() {
		return campaignDate;
	}

	public void setCampaignDate(Date campaignDate) {
		this.campaignDate = campaignDate;
	}

		
	
}
