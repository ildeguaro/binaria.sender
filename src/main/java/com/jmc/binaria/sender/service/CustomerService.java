package com.jmc.binaria.sender.service;

import java.util.List;

import com.jmc.binaria.sender.db.CustomerParameterDao;
import com.jmc.binaria.sender.db.CustomerParameterDaoImpl;
import com.jmc.binaria.sender.model.CustomerParameter;
import com.jmc.binaria.sender.model.SmtpSettings;

public class CustomerService {

	private CustomerParameterDao customerParameterDao;

	public CustomerService() {
		customerParameterDao = new CustomerParameterDaoImpl();
	}

	public SmtpSettings getSmtpSetting(long customerId) {
		SmtpSettings setting = new SmtpSettings();
		List<CustomerParameter> parameters = customerParameterDao.findByCustomerId(customerId);
		for (CustomerParameter c : parameters) {
			if (c.getParameterTypeId() == 01)
				setting.setHostname(c.getParameterValue());
			if (c.getParameterTypeId() == 03)
				setting.setPort(Integer.parseInt(c.getParameterValue()));
			if (c.getParameterTypeId() == 06)
				setting.setUsername(c.getParameterValue());
			if (c.getParameterTypeId() == 07)
				setting.setPassword(c.getParameterValue());
			if (c.getParameterTypeId() == 20)
				setting.setSubject(c.getParameterValue());
			if (c.getParameterTypeId() == 21)
				setting.setAttachmenName(c.getParameterValue());
			if (c.getParameterTypeId() == 15)
				setting.setFrom(c.getParameterValue());
		}
		return setting;

	}

}
