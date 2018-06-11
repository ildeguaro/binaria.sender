package com.jmc.binaria.sender.db;

import java.util.List;

import com.jmc.binaria.sender.model.Sender;

public interface SenderDao {
	
	public Sender createSender(Sender sender);
	
	public Sender findByName(String name);
	
	public int nextSenderId();
	
	public List<Sender> findAll();
	
	public boolean delete(Sender sender); 
}
