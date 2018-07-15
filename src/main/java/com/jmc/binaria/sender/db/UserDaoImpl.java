package com.jmc.binaria.sender.db;

import java.util.ArrayList;
import java.util.List;

import org.sql2o.Connection;

import com.jmc.binaria.sender.model.User;

public class UserDaoImpl implements UserDao {
	
	private static String TABLE_NAME = "usuario";


	public User autenticate(String login, String password) {
		User objeto = null;
		Connection conn = null;
		try {
			String sql = "select id, login, password, nombre_persona, apellido from " 
					+ TABLE_NAME + " where login = '"+login+"' and password = MD5('"+password+"') ";
			List<User> result = new ArrayList<User>();
			conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery(sql)
					.addColumnMapping("nombre_persona", "firstname")
					.addColumnMapping("apellido", "lastname")					
					.executeAndFetch(User.class);
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

}
