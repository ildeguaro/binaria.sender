package com.jmc.binaria.sender.db;

import java.util.ArrayList;
import java.util.List;

import org.sql2o.Connection;

import com.jmc.binaria.sender.model.Sender;

public class SenderDaoImpl implements SenderDao {

	private static String TABLE_NAME = "sender_senders";

	public Sender createSender(Sender sender) {
		try {
			
			if (this.findByName(sender.getName()) == null) {
				
				Connection conn = Sql2Connection.getSql2oConnetion().beginTransaction();
				String sql = "insert into " + TABLE_NAME + " (name, uri) VALUES (:name, :uri)";
				conn.createQuery(sql).addParameter("name", sender.getName()).addParameter("uri", sender.getUriAccess())
						.executeUpdate();
				conn.commit();
				System.out.println(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sender;
	}

	public List<Sender> findAll() {
		List<Sender> result = new ArrayList<Sender>();
		try {
			String sql = "select * from " + TABLE_NAME;
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery(sql).executeAndFetch(Sender.class);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Sender findByName(String name) {
		Sender objeto = null;
		try {
			String sql = "select * from " + TABLE_NAME + " where name = :name ";
			List<Sender> result = new ArrayList<Sender>();
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery(sql).addParameter("name", name).addColumnMapping("uri", "uriAccess")
					.executeAndFetch(Sender.class);
			System.out.println(sql);
			if (result != null && !result.isEmpty())
				objeto = result.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objeto;
	}

	public int nextSenderId() {
		int next = 0;
		try {
			String sql = "select count(*) from " + TABLE_NAME;
			List<Integer> result = new ArrayList<Integer>();
			Connection conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery(sql).executeAndFetch(Integer.class);
			System.out.println(sql);
			next = result.get(0);
			System.out.println("next id : " + next);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return next;
	}
}
