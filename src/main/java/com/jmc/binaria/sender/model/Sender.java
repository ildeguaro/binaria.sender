package com.jmc.binaria.sender.model;

public class Sender {
	
	public static String ASSIGN_TO_ME = "TO_ME";
	
	public static String ASSIGN_ANY = "ANY";
	
	public static String SENDER_MAIN_NAME = "binaria.sender.main";
	
	public static String SENDER_NAME_PREFIX = "binaria.sender.";
	
	private int id;
	
	private String name;
	
	private String uriAccess;
	
	private String asignationType;
	
	private long customerId;
	
	private boolean enabled;
	
	private boolean debug;	
	
	private String pid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUriAccess() {
		return uriAccess;
	}

	public void setUriAccess(String uriAccess) {
		this.uriAccess = uriAccess;
	}

	public String getAsignationType() {
		return asignationType;
	}

	public void setAsignationType(String asignationType) {
		this.asignationType = asignationType;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
