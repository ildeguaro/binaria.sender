package com.jmc.binaria.sender.model;

import java.util.Date;
import java.util.List;

import com.jmc.binaria.sender.model.stats.StatsGlobal;

public class Campaign {

	private long id;

	private String uuid;

	private String name;

	private Long customerId;

	private Customer customer;

	private String ordenImpresionId;

	private Date creationDate;
	
	private String emailTemplate;

	private Date sendingBeginDate;

	private Date sendingEndDate;
	
	private int duration;

	private String status;
	
	private int sentCount;
	
	private List<StatsGlobal> statsGlobal;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getOrdenImpresionId() {
		return ordenImpresionId;
	}

	public void setOrdenImpresionId(String ordenImpresionId) {
		this.ordenImpresionId = ordenImpresionId;
	}

	public String getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(String emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getSendingBeginDate() {
		return sendingBeginDate;
	}

	public void setSendingBeginDate(Date sendingBeginDate) {
		this.sendingBeginDate = sendingBeginDate;
	}

	public Date getSendingEndDate() {
		return sendingEndDate;
	}

	public void setSendingEndDate(Date sendingEndDate) {
		this.sendingEndDate = sendingEndDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<StatsGlobal> getStatsGlobal() {
		return statsGlobal;
	}

	public void setStatsGlobal(List<StatsGlobal> statsGlobal) {
		this.statsGlobal = statsGlobal;
	}

	public int getSentCount() {
		return sentCount;
	}

	public void setSentCount(int sentCount) {
		this.sentCount = sentCount;
	}	

}
