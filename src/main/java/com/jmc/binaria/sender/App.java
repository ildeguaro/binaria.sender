package com.jmc.binaria.sender;

import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.*;

import com.jmc.binaria.sender.model.EmailCampaign;
import com.jmc.binaria.sender.model.api.SendEmailPayload;
import com.jmc.binaria.sender.service.BinariaSenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Main App Class
 *
 */
public class App {

	private static String CONTEXT_PATH = "/binaria.sender";

	private static String API_PATH = "/api/";

	public static void main(String[] args) {
		
		final BinariaSenderService service = new BinariaSenderService();
		
		final ObjectMapper objectMapper =  new ObjectMapper();
		
		post(CONTEXT_PATH + API_PATH + "sendemail", new Route() {
			public Object handle(Request rqst, Response rspns) throws Exception {				
				System.out.println("POST : "+rqst.contentType()); 
			    System.out.println("POST : "+rqst.body()); 
			    SendEmailPayload payload = objectMapper.readValue(rqst.body(), SendEmailPayload.class);			   
				String[] packages = payload.getPackagesName();
				EmailCampaign campaign = service.createEmailCampaign(payload.getFtpValues(), payload.getSmtpValues(), packages);
				return campaign;
			}
		});
		System.out.println(
				"Running service: binaria.sender on : \n" + "http://localhost:" + port() + CONTEXT_PATH + API_PATH);

	}
}
