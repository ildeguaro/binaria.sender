package com.jmc.binaria.sender.model;

public class Sender {
	
	public static String ASSIGN_TO_ME = "TO_ME";
	
	public static String ASSIGN_ANY = "ANY";
	
	private int id;
	
	private String name;
	
	private String uriAccess;
	
	private String asignationType;
	

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

}
