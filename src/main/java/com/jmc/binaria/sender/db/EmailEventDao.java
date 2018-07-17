package com.jmc.binaria.sender.db;

import java.util.List;

import com.jmc.binaria.sender.model.EmailEvent;

public interface EmailEventDao {
	
	List<EmailEvent> findByEsmptpId(String smptId);

}
