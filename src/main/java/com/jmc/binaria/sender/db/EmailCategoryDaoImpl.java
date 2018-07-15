package com.jmc.binaria.sender.db;

import java.util.List;

import org.sql2o.Connection;
import org.sql2o.Query;

import com.jmc.binaria.sender.model.EmailCategory;

public class EmailCategoryDaoImpl implements EmailCategoryDao {

	private static String TABLE_NAME = "sender_email_category";

	@Override
	public void insert(EmailCategory emailCategory) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean insertAll(List<EmailCategory> emailCategoryList) {
		boolean ok = false;
		Connection conn = null;
		try {
			final String sql = "INSERT INTO " + TABLE_NAME + "(sender_email_campaign_id,category) "
					+ "VALUES (:sender_email_campaign_id, :category)";

			conn = Sql2Connection.getSql2oConnetion().beginTransaction();
			Query query = conn.createQuery(sql);
			emailCategoryList.forEach( i -> {
				query
				.addParameter("sender_email_campaign_id", i.getEmailCampaingId())
				.addParameter("category",i.getCategoryName()).addToBatch();
			});
			query.executeBatch();
			conn.commit();
			ok = true;
		} catch (Exception e) {
			e.printStackTrace();
			ok = false;
		} finally {
			if (conn != null)
				conn.close();
		}
		return ok;

	}

}
