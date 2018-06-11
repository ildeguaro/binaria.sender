package com.jmc.binaria.sender.db;

import java.util.ArrayList;
import java.util.List;

import org.sql2o.Connection;

import com.jmc.binaria.sender.model.Customer;
import com.jmc.binaria.sender.model.Campaign;

public class CampaignDaoImpl implements CampaignDao {
	
	private static String TABLE_NAME = "sender_campaigns";

	public Campaign createCampaing(Customer customer, String nameCampaig, Long ordenImpresionId, String emailTemplate) {
		try {
			String uuid = getUUIDFromSQL();
			Connection conn = Sql2Connection.getSql2oConnetion().beginTransaction();
			conn.createQuery(
					"insert into "+TABLE_NAME+" (uuid,name,customer_id,orden_impresion_id,html_template,creation_date,status)"
							+ " VALUES (:uuid, :name, :customer_id, :orden_impresion_id, :html_template, now(), :status)")
					
					.addParameter("uuid", uuid)
					.addParameter("name", nameCampaig)
					.addParameter("customer_id", customer.getId())
					.addParameter("orden_impresion_id", ordenImpresionId)
					.addParameter("html_template", emailTemplate)
					.addParameter("status", "CREATED")
					.executeUpdate();
			conn.commit();
			return findCampaignByUiid(uuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getUUIDFromSQL() {
		String uuid = "";
		try {
			List<String> result = new ArrayList<String>();
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery("select uuid()").executeAndFetch(String.class);
			if (result != null && !result.isEmpty())
				uuid = result.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uuid;
	}

	public Campaign findCampaignByUiid(String uuid) {
		Campaign object = null;
		try {
			List<Campaign> result = new ArrayList<Campaign>();
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery("select * from "+TABLE_NAME+" where uuid = :uuid order by id desc")
					.addParameter("uuid", uuid)
					.addColumnMapping("customer_id", "customerId")
					.addColumnMapping("orden_impresion_id", "ordenImpresionId")
					.addColumnMapping("creation_date", "creationDate")
					.addColumnMapping("sending_begin","sendingBeginDate")
					.addColumnMapping("sending_end","sendingEndDate")
					.addColumnMapping("sending_duration","duration")
					.addColumnMapping("html_template", "emailTemplate")
					.executeAndFetch(Campaign.class);
			if (result != null && !result.isEmpty())
				object = result.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}

	public List<Campaign> allCampaign() {
		List<Campaign> result = new ArrayList<Campaign>();
		try {			
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery("select * from "+TABLE_NAME)					
					.addColumnMapping("customer_id", "customerId")
					.addColumnMapping("orden_impresion_id", "ordenImpresionId")
					.addColumnMapping("creation_date", "creationDate")
					.addColumnMapping("sending_begin","sendingBeginDate")
					.addColumnMapping("sending_end","sendingEndDate")
					.addColumnMapping("sending_duration","duration")
					.addColumnMapping("html_template", "emailTemplate")
					.executeAndFetch(Campaign.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
