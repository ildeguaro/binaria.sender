package com.jmc.binaria.sender.model.api;

import com.jmc.binaria.sender.model.FtpSettings;

public class SendEmailPayload {
	
	private int customerId;
	
	private FtpSettings ftpValues;
	
	private String[] packagesName;
	
	private String emailTemplateBase64;
	
	private String ordenImpresionId;
	
	private String emailDescription;

	public FtpSettings getFtpValues() {
		return ftpValues;
	}

	public void setFtpValues(FtpSettings ftpValues) {
		this.ftpValues = ftpValues;
	}

	public String[] getPackagesName() {
		return packagesName;
	}

	public void setPackagesName(String[] packagesName) {
		this.packagesName = packagesName;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	
	public String getOrdenImpresionId() {
		return ordenImpresionId;
	}

	public void setOrdenImpresionId(String ordenImpresionId) {
		this.ordenImpresionId = ordenImpresionId;
	}

	public String getEmailTemplateBase64() {
		return emailTemplateBase64;
	}

	public void setEmailTemplateBase64(String emailTemplateBase64) {
		this.emailTemplateBase64 = emailTemplateBase64;
	}

	public String getEmailDescription() {
		return emailDescription;
	}

	public void setEmailDescription(String emailDescription) {
		this.emailDescription = emailDescription;
	}

}
