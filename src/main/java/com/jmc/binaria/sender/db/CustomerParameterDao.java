package com.jmc.binaria.sender.db;

import java.util.List;

import com.jmc.binaria.sender.model.CustomerParameter;

public interface CustomerParameterDao {
	
	List<CustomerParameter> findByCustomerId(long customerId);

}
