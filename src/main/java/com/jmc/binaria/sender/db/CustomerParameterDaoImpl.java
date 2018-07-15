package com.jmc.binaria.sender.db;

import java.util.ArrayList;
import java.util.List;

import org.sql2o.Connection;

import com.jmc.binaria.sender.model.CustomerParameter;

public class CustomerParameterDaoImpl implements CustomerParameterDao {
	
	private static String TABLE_NAME = "parametros_x_cliente";


	public List<CustomerParameter> findByCustomerId(long customerId) {
		List<CustomerParameter> result = null;
		Connection conn = null;
		try {
			result = new ArrayList<CustomerParameter>();
			conn = Sql2Connection.getSql2oConnetion().open();
			result = conn.createQuery("select cliente_id,parametro_tipo_id,valor  from " + TABLE_NAME + " where cliente_id = :customerId order by id desc")
					.addParameter("customerId", customerId)					
					.addColumnMapping("cliente_id", "customerId")
					.addColumnMapping("parametro_tipo_id", "parameterTypeId")
					.addColumnMapping("valor", "parameterValue")					
					.executeAndFetch(CustomerParameter.class);
			
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
