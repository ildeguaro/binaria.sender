package com.jmc.binaria.sender.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sql2o.Connection;

import com.jmc.binaria.sender.model.EmailCampaign;

public class EmailCampaignDaoImpl implements EmailCampaignDao {

	private static String TABLE_NAME = "sender_email_campaign";

	public EmailCampaign createEmailCampaing(EmailCampaign ec) {
		try {
			Connection conn = Sql2Connection.getSql2oConnetion().beginTransaction();
			conn.createQuery("insert into " + TABLE_NAME
					+ " (sender_campaigns_id,addresses,names,package_id,document_id,fields_search)"
					+ " VALUES (:sender_campaigns_id, :addresses, :names, :package_id, :document_id, :fields_search)")

					.addParameter("sender_campaigns_id", ec.getCampaignId())
					.addParameter("addresses", ec.getAddresses()).addParameter("names", ec.getNames())
					.addParameter("package_id", ec.getPackageId()).addParameter("document_id", ec.getDocumentId())
					.addParameter("fields_search", ec.getFieldsSearch()).executeUpdate();
			conn.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ec;
	}

	public EmailCampaign updateSending(EmailCampaign ec) {
		try {
			Connection conn = Sql2Connection.getSql2oConnetion().beginTransaction();
			conn.createQuery("update " + TABLE_NAME
					+ " set sending_date = now(), sent = :sent, response = :response, error = :error")
					.addParameter("sent", ec.isWasSent()).addParameter("response", ec.getResponse())
					.addParameter("error", ec.getError())

					.executeUpdate();
			conn.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ec;
	}

	public EmailCampaign findEmailCampaignByColumns(Map<String, String> columns) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<EmailCampaign> selectEmailToSend(long ordenId, int quantity) {
		List<EmailCampaign> result = new ArrayList<EmailCampaign>();

		try {
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery("select * from " + TABLE_NAME + " where sent = 0 ")
					.addColumnMapping("campaign_id", "campaignId")
					.addColumnMapping("package_id", "packageId")
					.addColumnMapping("document_id", "documentId")
					.addColumnMapping("sending_date", "sendingDate")
					.addColumnMapping("sent", "wasSent")
					.executeAndFetch(EmailCampaign.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
