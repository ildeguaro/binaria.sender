package com.jmc.binaria.sender.service;

import com.jmc.binaria.sender.db.SenderDao;
import com.jmc.binaria.sender.db.SenderDaoImpl;
import com.jmc.binaria.sender.model.Sender;

public class SenderService {
	
	private SenderDao senderDao;
	
	private int port;
	
	public SenderService() {
		this.senderDao = new SenderDaoImpl();
	}
	
	public Sender registraMe(String host, int portBase, long customerId, String pid) {
			
		String name = "";
		if (senderDao.existsMainSender()) {
			name = Sender.SENDER_NAME_PREFIX + (this.senderDao.nextSenderId());
			port = portBase+this.senderDao.nextSenderId()+1;
		}else { 
			name = Sender.SENDER_MAIN_NAME;
			port = portBase;
		}Sender sender = new Sender();	
		sender.setName(name);
		String uri = "http://"+host+":"+port+"/"+sender.getName()+"/";		
		sender.setUriAccess(uri);
		sender.setCustomerId(customerId);
		sender.setPid(pid);
		sender = senderDao.createSender(sender);		
		return sender;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean killme(Sender sender) {
		return senderDao.delete(sender);
	}
	
	public Sender senderByName(String name) {
		return senderDao.findByName(name);
	}

}
