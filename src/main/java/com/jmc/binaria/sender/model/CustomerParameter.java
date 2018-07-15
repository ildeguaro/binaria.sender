package com.jmc.binaria.sender.model;

public class CustomerParameter {
	
	private long id;
	
	private long parameterTypeId;
	
	private long customerId;
	
	private String parameterValue;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParameterTypeId() {
		return parameterTypeId;
	}

	public void setParameterTypeId(long parameterTypeId) {
		this.parameterTypeId = parameterTypeId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

}
