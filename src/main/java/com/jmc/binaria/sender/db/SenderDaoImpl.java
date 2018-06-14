package com.jmc.binaria.sender.db;

import java.util.ArrayList;
import java.util.List;

import org.sql2o.Connection;

import com.jmc.binaria.sender.model.Sender;

public class SenderDaoImpl implements SenderDao {

	private static String TABLE_NAME = "sender_senders";

	public Sender createSender(Sender sender) {
		Connection conn = null;
		try {
			if (this.findByName(sender.getName()) == null) {
				conn = Sql2Connection.getSql2oConnetion().beginTransaction();
				String sql = "insert into " + TABLE_NAME + " (name, uri, customer_id) VALUES (:name, :uri, :customer_id)";
				conn.createQuery(sql)
						.addParameter("name", sender.getName())
						.addParameter("uri", sender.getUriAccess())
						.addParameter("customer_id", sender.getCustomerId())
						.executeUpdate();
				conn.commit();
				System.out.println(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
		}
		return sender;
	}

	public Sender createSenderWithAssignation(Sender sender) {
		Connection conn = null;
		try {
			if (this.findByName(sender.getName()) == null) {
				conn = Sql2Connection.getSql2oConnetion().beginTransaction();
				String sql = "insert into " + TABLE_NAME
						+ " (name, uri, assignation_type) VALUES (:name, :uri, :assignation_type)";
				conn.createQuery(sql).addParameter("name", sender.getName()).addParameter("uri", sender.getUriAccess())
						.addParameter("assignation_type", sender.getAsignationType()).executeUpdate();
				conn.commit();
				System.out.println(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
		}
		return sender;
	}

	public List<Sender> findAll() {
		List<Sender> result = new ArrayList<Sender>();
		Connection conn = null;
		try {
			String sql = "select * from " + TABLE_NAME;
			conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery(sql)
					.addColumnMapping("uri", "uriAccess")
					.addColumnMapping("assignation_type", "asignationType")
					.executeAndFetch(Sender.class);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
		}
		return result;
	}

	public Sender findByName(String name) {
		Sender objeto = null;
		Connection conn = null;
		try {
			String sql = "select * from " + TABLE_NAME + " where name = :name ";
			List<Sender> result = new ArrayList<Sender>();
			conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery(sql).addParameter("name", name)
					.addColumnMapping("uri", "uriAccess")
					.addColumnMapping("assignation_type", "asignationType")
					.addColumnMapping("customer_id", "customerId")
					.executeAndFetch(Sender.class);
			if (result != null && !result.isEmpty())
				objeto = result.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
		}
		return objeto;
	}

	public int nextSenderId() {
		int next = 0;
		Connection conn = null;
		try {
			String sql = "select count(*) from " + TABLE_NAME;
			List<Integer> result = new ArrayList<Integer>();
			conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery(sql).executeAndFetch(Integer.class);
			next = result.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
		}
		return next;
	}

	public boolean delete(Sender sender) {
		boolean result = false;
		Connection conn = null;
		try {
			conn = Sql2Connection.getSql2oConnetion().beginTransaction();
			String sql = "delete from " + TABLE_NAME + " where name = :name";
			conn.createQuery(sql).addParameter("name", sender.getName()).executeUpdate();
			conn.commit();

			System.out.println(sql);
			result = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
		}
		return result;
	}

}
