package com.jmc.binaria.sender.db;

import java.util.ArrayList;
import java.util.List;

import org.sql2o.Connection;

import com.jmc.binaria.sender.model.EmailEvent;

public class EmailEventDaoImpl implements EmailEventDao {

	private static String TABLE_NAME = "sender_email_events";

	@Override
	public List<EmailEvent> findByEsmptpId(String esmptId) {
		List<EmailEvent> result = new ArrayList<EmailEvent>();
		Connection conn = null;
		try {
			conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery("select * from " + TABLE_NAME+ " where esmtp_id = "+esmptId)
					.addColumnMapping("sender_email_campaign_id", "emailCampaingId")
					.addColumnMapping("esmtp_id", "esmtpId")
					.addColumnMapping("event_type", "eventType")
					.addColumnMapping("event_date", "eventDate")
					.executeAndFetch(EmailEvent.class);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

}
