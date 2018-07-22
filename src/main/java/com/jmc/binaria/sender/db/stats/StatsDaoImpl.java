package com.jmc.binaria.sender.db.stats;

import java.util.ArrayList;
import java.util.List;

import org.sql2o.Connection;

import com.jmc.binaria.sender.db.Sql2Connection;
import com.jmc.binaria.sender.model.stats.StatsByCategory;
import com.jmc.binaria.sender.model.stats.StatsGlobal;
import com.jmc.binaria.sender.model.stats.StatsSent;

public class StatsDaoImpl implements StatsDao {

	private static String VIEW_GLOBAL_STATS_NAME = "sender_view_stats_global";
	
	private static String VIEW_BY_CATEGORY_STATS_NAME = "sender_view_stats_by_category";
	
	private static String VIEW_GLOBAL_BY_CATEGORY_STATS_NAME = "sender_view_stats_global_category";

	@Override
	public List<StatsGlobal> finbByCampaignId(long campaignId) {
		List<StatsGlobal> result = new ArrayList<StatsGlobal>();
		Connection conn = null;
		try {
			conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery("select * from " + VIEW_GLOBAL_STATS_NAME + " where campaigns_id = " + campaignId)
					.addColumnMapping("campaigns_id", "campaignId").addColumnMapping("description", "descriptionEvent")
					.addColumnMapping("icon_path", "iconEvent").executeAndFetch(StatsGlobal.class);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

	@Override
	public int getSentCount(long campaignId) {
		Integer result= new Integer(0);
		Connection conn = null;
		try {
			conn = Sql2Connection.getSql2oConnetion().open();
			result = conn
					.createQuery("SELECT count(email.id) AS sent_count "
							+ "FROM sender_email_campaign email "
							+ "WHERE email.sent "
							+ "AND email.sender_campaigns_id =" + campaignId)
					.executeScalar(Integer.class);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

	@Override
	public List<StatsByCategory> findByCategory(long campaignId, String category) {
		List<StatsByCategory> result = new ArrayList<StatsByCategory>();
		Connection conn = null;
		try {
			conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery("select * from " + VIEW_BY_CATEGORY_STATS_NAME + " "
					+ "where campaigns_id = " + campaignId + " and category = '"+category+"'")
					.addColumnMapping("campaigns_id", "campaignId")
					.addColumnMapping("description", "descriptionEvent")
					.addColumnMapping("icon_path", "iconEvent")
					.executeAndFetch(StatsByCategory.class);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

	@Override
	public List<StatsSent> findAllSents(long campaignId) {
		List<StatsSent> result = new ArrayList<StatsSent>();
		Connection conn = null;
		try {
			conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery("select * from " + VIEW_GLOBAL_BY_CATEGORY_STATS_NAME + " where campaigns_id = " + campaignId)
					.addColumnMapping("campaigns_id", "campaignId")
					.executeAndFetch(StatsSent.class);

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
