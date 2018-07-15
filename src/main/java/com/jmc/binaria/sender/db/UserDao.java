package com.jmc.binaria.sender.db;

import com.jmc.binaria.sender.model.User;

public interface UserDao {
	
	public User autenticate(String user, String password);

}
