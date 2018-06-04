package com.jmc.binaria.sender.model.api;

import com.jmc.binaria.sender.model.FtpSettings;
import com.jmc.binaria.sender.model.SmtpSettigs;

public class SendEmailPayload {
	
	private FtpSettings ftpValues;
	
	private SmtpSettigs smtpValues;
	
	private String[] packagesName;
	
	private int customerId;
	
	private String ordenImpresionId;
	
	private String emailDescription;

	public FtpSettings getFtpValues() {
		return ftpValues;
	}

	public void setFtpValues(FtpSettings ftpValues) {
		this.ftpValues = ftpValues;
	}

	public SmtpSettigs getSmtpValues() {
		return smtpValues;
	}

	public void setSmtpValues(SmtpSettigs smtpValues) {
		this.smtpValues = smtpValues;
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

	public String getEmailDescription() {
		return emailDescription;
	}

	public void setEmailDescription(String emailDescription) {
		this.emailDescription = emailDescription;
	}

}
