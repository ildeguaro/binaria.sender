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
					+ " (sender_campaigns_id,addresses,names,package_id,document_id,attachment_path,content_email,fields_search)"
					+ " VALUES (:sender_campaigns_id, :addresses, :names, :package_id, :document_id, :attachment_path,"
					+ " :content_email, :fields_search)")

					.addParameter("sender_campaigns_id", ec.getCampaignId())
					.addParameter("addresses", ec.getAddresses()).addParameter("names", ec.getNames())
					.addParameter("package_id", ec.getPackageId()).addParameter("document_id", ec.getDocumentId())
					.addParameter("attachment_path", ec.getAttachmentPath())
					.addParameter("content_email", ec.getContentEmail())
					.addParameter("fields_search", ec.getFieldsSearch())
					.executeUpdate();
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
					+ " set sending_date = now(), sent = :sent, response = :response, error = :error, sender_id = :senderId")
					.addParameter("sent", ec.isWasSent()).addParameter("response", ec.getResponse())
					.addParameter("error", ec.getError())
					.addParameter("senderId", ec.getSenderId())
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

	public List<EmailCampaign> selectEmailToSend(int quantity, int senderId) {
		List<EmailCampaign> result = new ArrayList<EmailCampaign>();

		try {
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			String sql = "select * from " + TABLE_NAME + " where sender_id = "+senderId+" and sent = 0 ";
			System.out.println(sql);
			result = conn.createQuery(sql)					
					.addColumnMapping("sender_campaigns_id", "campaignId")
					.addColumnMapping("package_id", "packageId")
					.addColumnMapping("document_id", "documentId")
					.addColumnMapping("attachment_path", "attachmentPath")
					.addColumnMapping("content_email", "contentEmail")
					.addColumnMapping("sending_date", "sendingDate")
					.addColumnMapping("sent", "wasSent")
					.addColumnMapping("sender_id", "senderId")
					.addColumnMapping("fields_search", "fieldsSearch")
					.addColumnMapping("sender_assigned_id", "senderIdAssinged")
					.executeAndFetch(EmailCampaign.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<EmailCampaign> findEmailCampaignByFields(String words) {
		List<EmailCampaign> result = new ArrayList<EmailCampaign>();

		try {
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			String sql = "select sec.*, sc.uuid, sc.name, sc.creation_date from sender_email_campaign sec " + 
					"join sender_campaigns sc on sec.sender_campaigns_id = sc.id  " + 
					"where sec.fields_search like '%"+words+"%'";
			System.out.println(sql);
			result = conn.createQuery(sql)
					
					.addColumnMapping("sender_campaigns_id", "campaignId")
					.addColumnMapping("package_id", "packageId")
					.addColumnMapping("document_id", "documentId")
					.addColumnMapping("attachment_path", "attachmentPath")
					.addColumnMapping("content_email", "contentEmail")
					.addColumnMapping("sending_date", "sendingDate")
					.addColumnMapping("sent", "wasSent")
					.addColumnMapping("sender_id", "senderId")
					.addColumnMapping("fields_search", "fieldsSearch")
					.addColumnMapping("uuid", "campaignUuid")
					.addColumnMapping("name", "campaignName")
					.addColumnMapping("creation_date", "campaignDate")
					.addColumnMapping("sender_assigned_id", "senderIdAssinged")
					.executeAndFetch(EmailCampaign.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<EmailCampaign> findEmailCampaignByOrdenId(long ordenId) {
		List<EmailCampaign> result = new ArrayList<EmailCampaign>();

		try {
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			String sql = "select sec.*, sc.uuid, sc.name, sc.creation_date from sender_email_campaign sec " + 
					"join sender_campaigns sc on sec.sender_campaigns_id = sc.id  " + 
					"where sc.id =  :ordenId order by sc.id desc";
			System.out.println(sql);
			result = conn.createQuery(sql)
					.addParameter("ordenId", ordenId)
					.addColumnMapping("sender_campaigns_id", "campaignId")
					.addColumnMapping("package_id", "packageId")
					.addColumnMapping("document_id", "documentId")
					.addColumnMapping("attachment_path", "attachmentPath")
					.addColumnMapping("content_email", "contentEmail")
					.addColumnMapping("sending_date", "sendingDate")
					.addColumnMapping("sent", "wasSent")
					.addColumnMapping("sender_id", "senderId")
					.addColumnMapping("fields_search", "fieldsSearch")
					.addColumnMapping("uuid", "campaignUuid")
					.addColumnMapping("name", "campaignName")
					.addColumnMapping("creation_date", "campaignDate")
					.addColumnMapping("sender_assigned_id", "senderIdAssinged")
					.executeAndFetch(EmailCampaign.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<EmailCampaign> findEmailCampaignByAddress(String address) {
		List<EmailCampaign> result = new ArrayList<EmailCampaign>();

		try {
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			String sql = "select sec.*, sc.uuid, sc.name, sc.creation_date from sender_email_campaign sec " + 
					"join sender_campaigns sc on sec.sender_campaigns_id = sc.id  " + 
					"where sec.addresses = :address order by sc.id desc";
			System.out.println(sql);
			result = conn.createQuery(sql)
					.addParameter("ordenId", address)
					.addColumnMapping("sender_campaigns_id", "campaignId")
					.addColumnMapping("package_id", "packageId")
					.addColumnMapping("document_id", "documentId")
					.addColumnMapping("attachment_path", "attachmentPath")
					.addColumnMapping("content_email", "contentEmail")
					.addColumnMapping("sending_date", "sendingDate")
					.addColumnMapping("sent", "wasSent")
					.addColumnMapping("sender_id", "senderId")
					.addColumnMapping("fields_search", "fieldsSearch")
					.addColumnMapping("uuid", "campaignUuid")
					.addColumnMapping("name", "campaignName")
					.addColumnMapping("creation_date", "campaignDate")
					.addColumnMapping("sender_assigned_id", "senderIdAssinged")
					.executeAndFetch(EmailCampaign.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<EmailCampaign> findEmailCampaignByName(String name) {
		List<EmailCampaign> result = new ArrayList<EmailCampaign>();

		try {
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			String sql = "select sec.*, sc.uuid, sc.name, sc.creation_date from sender_email_campaign sec " + 
					"join sender_campaigns sc on sec.sender_campaigns_id = sc.id  " + 
					"where sec.name = :name order by sc.id desc";
			System.out.println(sql);
			result = conn.createQuery(sql)
					.addParameter("name", name)
					.addColumnMapping("sender_campaigns_id", "campaignId")
					.addColumnMapping("package_id", "packageId")
					.addColumnMapping("document_id", "documentId")
					.addColumnMapping("attachment_path", "attachmentPath")
					.addColumnMapping("content_email", "contentEmail")
					.addColumnMapping("sending_date", "sendingDate")
					.addColumnMapping("sent", "wasSent")
					.addColumnMapping("sender_id", "senderId")
					.addColumnMapping("fields_search", "fieldsSearch")
					.addColumnMapping("uuid", "campaignUuid")
					.addColumnMapping("name", "campaignName")
					.addColumnMapping("creation_date", "campaignDate")
					.addColumnMapping("sender_assigned_id", "senderIdAssinged")
					.executeAndFetch(EmailCampaign.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<EmailCampaign> findEmailCampaignByOrdenIdAddressNamesOrFieldsSearch(long ordenId, String address,
			String names, String searchFields) {
		List<EmailCampaign> result = new ArrayList<EmailCampaign>();

		try {
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			StringBuilder sql = new StringBuilder("select sec.*, sc.id, sc.uuid, sc.name, sc.creation_date \n");
					
			sql.append("from sender_email_campaign sec \n");
			sql.append("join sender_campaigns sc on sec.sender_campaigns_id = sc.id  \n" );
			sql.append("where \n");
			if (ordenId > 0) {
				sql.append("sc.id = ");
				sql.append(ordenId);
				sql.append(" ");
			}
			if (!address.isEmpty()) {
				if (ordenId>0)
					sql.append("AND ");
				sql.append("sec.addresses = '");
				sql.append(address);
				sql.append("' ");
				
			}
			if (!names.isEmpty()) {	
				if (!address.isEmpty() || ordenId > 0)
					sql.append("AND ");
				sql.append("sec.names = '");
				sql.append(names);
				sql.append("' ");
			}
			if (!searchFields.isEmpty()) {
				if (!names.isEmpty() || !address.isEmpty() || ordenId > 0)
					sql.append("AND ");
				sql.append("fields_search like '%");
				sql.append(searchFields);
				sql.append("%'");
			}
			
			sql.append(" order by sc.id desc");
			
			System.out.println(sql.toString());
			result = conn.createQuery(sql.toString())
									
					.addColumnMapping("sender_campaigns_id", "campaignId")
					.addColumnMapping("package_id", "packageId")
					.addColumnMapping("document_id", "documentId")
					.addColumnMapping("attachment_path", "attachmentPath")
					.addColumnMapping("content_email", "contentEmail")
					.addColumnMapping("sending_date", "sendingDate")
					.addColumnMapping("sent", "wasSent")
					.addColumnMapping("sender_id", "senderId")
					.addColumnMapping("fields_search", "fieldsSearch")
					.addColumnMapping("uuid", "campaignUuid")
					.addColumnMapping("name", "campaignName")
					.addColumnMapping("creation_date", "campaignDate")
					.addColumnMapping("sender_assigned_id", "senderIdAssinged")
					.executeAndFetch(EmailCampaign.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public EmailCampaign selectEmailToSendAssigned(int senderId) {
		EmailCampaign result = null;

		try {
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			String sql = "select * from " + TABLE_NAME + " where sender_assigned_id = "+senderId+" and sent = 0 ";
			System.out.println(sql);
			result = conn.createQuery(sql)					
					.addColumnMapping("sender_campaigns_id", "campaignId")
					.addColumnMapping("package_id", "packageId")
					.addColumnMapping("document_id", "documentId")
					.addColumnMapping("attachment_path", "attachmentPath")
					.addColumnMapping("content_email", "contentEmail")
					.addColumnMapping("sending_date", "sendingDate")
					.addColumnMapping("sent", "wasSent")
					.addColumnMapping("sender_id", "senderId")
					.addColumnMapping("fields_search", "fieldsSearch")
					.addColumnMapping("sender_assigned_id", "senderIdAssinged")
					.executeScalar(EmailCampaign.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public EmailCampaign selectEmailToSendNoAssigned() {
		List<EmailCampaign> result = new ArrayList<EmailCampaign>();

		try {
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			String sql = "select * from " + TABLE_NAME + " where sent = 0 and error is null ";
			System.out.println(sql);
			result = conn.createQuery(sql)					
					.addColumnMapping("sender_campaigns_id", "campaignId")
					.addColumnMapping("package_id", "packageId")
					.addColumnMapping("document_id", "documentId")
					.addColumnMapping("attachment_path", "attachmentPath")
					.addColumnMapping("content_email", "contentEmail")
					.addColumnMapping("sending_date", "sendingDate")
					.addColumnMapping("sent", "wasSent")
					.addColumnMapping("sender_id", "senderId")
					.addColumnMapping("fields_search", "fieldsSearch")
					.addColumnMapping("sender_assigned_id", "senderIdAssinged")
					.executeAndFetch(EmailCampaign.class);
			
			if (result != null && !result.isEmpty())
				return result.get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
