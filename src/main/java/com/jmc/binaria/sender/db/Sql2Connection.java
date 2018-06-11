package com.jmc.binaria.sender.db;

import org.sql2o.Sql2o;

public class Sql2Connection {

	private static Sql2o sql2oConnection;

	public static Sql2o getSql2oConnetion() {
		if (sql2oConnection == null)
			sql2oConnection = new Sql2o("jdbc:mysql://localhost:3306/binaria_db_t", "root", "zxasqw12");
		return sql2oConnection;
	}

}
