package com.jmc.binaria.sender.db;

import org.sql2o.Sql2o;

public class Sql2Connection {

	private static Sql2o sql2oConnection;

	public static Sql2o getSql2oConnetion() {
		if (sql2oConnection == null)
			//sql2oConnection = new Sql2o("jdbc:mysql://172.16.90.38:3306/binaria_db_t?autoReconnect=true&useSSL=false", "binaria_admin", "2ad.PDB1");
			sql2oConnection = new Sql2o("jdbc:mysql://localhost:3306/binaria_db_t?autoReconnect=true&useSSL=false", "root", "zxasqw12");
		return sql2oConnection;
	}

}
