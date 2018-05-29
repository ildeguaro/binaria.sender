package com.jmc.binaria.sender.model.api;

import com.jmc.binaria.sender.model.FtpSettings;
import com.jmc.binaria.sender.model.SmtpSettigs;

public class SendEmailPayload {
	
	private FtpSettings ftpValues;
	
	private SmtpSettigs smtpValues;
	
	private String[] packagesName;

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

}
