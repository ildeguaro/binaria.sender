package com.jmc.binaria.sender.controller;

import static spark.Spark.get;

import com.google.gson.Gson;
import com.jmc.binaria.sender.model.api.EmailEvents;
import com.jmc.binaria.sender.service.EmailCampaignService;

public class EmailCampaignController {

	private static String API_PATH = "/api/emails/";
	
	private static EmailCampaignService emailCampaignService;
	
	public static void routes() {
		emailCampaignService = new EmailCampaignService();
		get(API_PATH + "/events/:emaild", (req, res) -> {
			String id = req.params(":emaild");
			long longId = 0;
			if (id != null)
				longId = Long.parseLong(id);
			res.type("application/json");
			EmailEvents ee = emailCampaignService.getEventsEmail(longId);
			return new Gson().toJson(ee);
		});
	}

}
