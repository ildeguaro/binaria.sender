package com.jmc.binaria.sender.model;

public class EnvironmentVar {
	
	private String pid;
	
	private int clientId;
	
	private int threads;
	
	private int portBase;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public int getPortBase() {
		return portBase;
	}

	public void setPortBase(int portBase) {
		this.portBase = portBase;
	}
	
	

}
