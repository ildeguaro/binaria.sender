package com.jmc.binaria.sender.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.uuid.UUIDType;
import com.fasterxml.uuid.impl.UUIDUtil;

public class StringUitl {
	
	public static String getUUID(String login) throws UnsupportedEncodingException {
		long timestamp = new Date().getTime();
		String source = login + timestamp;
		byte[] bytes = source.getBytes("UTF-8");
		UUID uuid = UUIDUtil.constructUUID(UUIDType.RANDOM_BASED, bytes);
		return uuid.toString();
	}

}
