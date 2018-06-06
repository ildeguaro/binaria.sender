package com.jmc.binaria.sender;

import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
//import java.util.Timer;
//import java.util.TimerTask;

import com.jmc.binaria.sender.model.Campaign;
import com.jmc.binaria.sender.model.Sender;
import com.jmc.binaria.sender.model.api.SendEmailPayload;
import com.jmc.binaria.sender.service.BinariaSenderService;
import com.jmc.binaria.sender.service.SenderService;
//import com.jmc.binaria.sender.util.RunTask;
import org.codehaus.jackson.map.ObjectMapper;
import com.google.gson.Gson;
/**
 * Main App Class
 *
 */
public class App {

	private static String CONTEXT_PATH = "/binaria.sender";

	private static String API_PATH = "/api/";

	private static ObjectMapper objectMapper;

	private static BinariaSenderService service;
	
	private static SenderService senderService;
	
	private static Sender sender;
	
	public static void main(String[] args) throws InterruptedException, UnknownHostException {

		service = new BinariaSenderService();
		senderService = new SenderService();
		objectMapper = new ObjectMapper();
		
		sender = senderService.registraMe(InetAddress.getLocalHost().getHostAddress(), 1150);
		port(senderService.getPort());
		
		String uriBase = "/"+sender.getName() + API_PATH;
		
		/**********************************
		 * Identifica el sender levantado *
		 **********************************/
		get(uriBase + "whoami", new Route() {
			public String handle(Request rqst, Response rspns) throws Exception {
				rspns.type("application/json");
				rspns.status(200);
				return new Gson().toJson(sender);
			}
		});
		
		
		/***************************************
		 * Crea un Campana de Envio de Correos *
		 ***************************************/
		post(uriBase+ "campaigns", new Route() {
			public String handle(Request rqst, Response rspns) throws Exception {
				rspns.type("application/json");

				System.out.println("POST : " + rqst.contentType());
				System.out.println("POST : " + rqst.body());
				SendEmailPayload payload = objectMapper.readValue(rqst.body(), SendEmailPayload.class);
				Campaign campaign = service.createEmailCampaign(payload);
				if (campaign!=null)
					rspns.status(200);
				else
					rspns.status(500);

				return new Gson().toJson(campaign);
			}
		});
		
		
		
		System.out.println(
				"Running service: binaria.sender on : \n" + sender.getUriAccess());

//		TimerTask task = new RunTask();
//
//		Timer timer = new Timer();
//		timer.schedule(task, 1000, 2000);
	}
}
