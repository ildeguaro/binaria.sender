package com.jmc.binaria.sender.controller;

import static spark.Spark.get;

import java.util.List;

import com.google.gson.Gson;
import com.jmc.binaria.sender.model.stats.StatsSent;
import com.jmc.binaria.sender.service.StatsService;

public class StatsController {

	private static String API_PATH = "/api/stats/";

	private static StatsService statsService;

	public static void routes() {
		statsService = new StatsService();
		get(API_PATH + "by-category/:ordenid", (req, res) -> {
			String ordenid = req.params(":ordenid");
			long longId = 0;
			if (ordenid != null)
				longId = Long.parseLong(ordenid);
			res.type("application/json");
			
			List<StatsSent> items = statsService.getStatsSent(longId);
			return new Gson().toJson(items);
		});
	}

}
