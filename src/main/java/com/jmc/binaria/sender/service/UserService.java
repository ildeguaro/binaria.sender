package com.jmc.binaria.sender.service;

import com.jmc.binaria.sender.db.UserDao;
import com.jmc.binaria.sender.db.UserDaoImpl;
import com.jmc.binaria.sender.model.User;

public class UserService {
	
	private UserDao userDao;
	
	public UserService() {
		userDao = new UserDaoImpl();
	}
	
	public User autenticar(String login, String pass) {
		return userDao.autenticate(login, pass);
	}
}
