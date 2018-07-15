package com.jmc.binaria.sender.db;

import java.util.List;

import com.jmc.binaria.sender.model.EmailCategory;

public interface EmailCategoryDao {
	
	public void insert(EmailCategory emailCategory);
		
	public boolean insertAll(List<EmailCategory> emailCategoryList);

}
