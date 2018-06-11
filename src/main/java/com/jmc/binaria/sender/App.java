package com.jmc.binaria.sender;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import static spark.Spark.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
//import java.util.Timer;
//import java.util.TimerTask;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jmc.binaria.sender.model.Campaign;
import com.jmc.binaria.sender.model.EmailCampaign;
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

	private static String API_PATH = "/api/";

	private static ObjectMapper objectMapper;

	private static BinariaSenderService service;

	private static SenderService senderService;

	private static Sender sender;

	public static void main(String[] args) throws InterruptedException, UnknownHostException {

		final ThymeleafTemplateEngine engineView = new ThymeleafTemplateEngine();

		service = new BinariaSenderService();
		senderService = new SenderService();
		objectMapper = new ObjectMapper();

		sender = senderService.registraMe(InetAddress.getLocalHost().getHostAddress(), 1150);
		port(senderService.getPort());

		staticFiles.location("/templates");

		String uriApiBase = "/" + sender.getName() + API_PATH;
		

		/**********************************
		 * Home *
		 **********************************/
		get("/" + sender.getName() + "/", new Route() {
			public String handle(Request rqst, Response rspns) throws Exception {
				Map<String, String> varables = new HashMap<String, String>();
				varables.put("instance", ":"+port()+"/"+sender.getName());
				return engineView.render(new ModelAndView(varables, "home"));
			}
		});
		

		/**********************************
		 * Busqueda Basica *
		 **********************************/
		get("/" + sender.getName() + "/busqueda-basica", new Route() {
			public String handle(Request rqst, Response rspns) throws Exception {
				List<Campaign> campaigns = service.getAllCampaign();
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("ordenes",campaigns);
				variables.put("instance", sender.getName());
				return engineView.render(new ModelAndView(variables, "busqueda-basica"));
			}
		});
		
		/**********************************
		 * Busqueda Avanzada *
		 **********************************/
		get("/" + sender.getName() + "/busqueda-avanzada", new Route() {
			public String handle(Request rqst, Response rspns) throws Exception {
				Map<String, String> varables = new HashMap<String, String>();
				varables.put("instance", ":"+port()+"/"+sender.getName());
				return engineView.render(new ModelAndView(varables, "busqueda-avanzada"));
			}
		});
		
		/**********************************
		 * Buscar en envio *
		 **********************************/
		get("/" + sender.getName() + "/find", new Route() {
			public String handle(Request rqst, Response rspns) throws Exception {
				String words = rqst.queryParams("words");
				String ordenIdString = rqst.queryParams("ordenId");
				String email = rqst.queryParams("email");
				String name = rqst.queryParams("name");
				long ordenId = 0;
					
				System.out.println("Palabra Clave : "+words);
				List<Campaign> campaigns = service.getAllCampaign();
				List<EmailCampaign> result = new ArrayList<EmailCampaign>();
				
				if (words==null)
					words = "";
				
				if (ordenIdString != null && !ordenIdString.isEmpty())
					ordenId = Long.parseLong(ordenIdString);
				
				if (email == null)
					email = "";
				
				if (name == null)
					name = "";
				
				result = service.getEmailCampaignByBasicSearch(ordenId, email, name, words);
				
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("instance",sender.getName());
				variables.put("ordenes",campaigns);
				variables.put("result", result);
				variables.put("ordenId", ordenId);
				variables.put("email", email);
				variables.put("words", words);
				variables.put("name", name);
				return engineView.render(new ModelAndView(variables, "busqueda-basica"));
			}
		});

		/**********************************
		 * Identifica el sender levantado *
		 **********************************/
		get(uriApiBase+"/whoami", new Route() {
			public String handle(Request rqst, Response rspns) throws Exception {
				rspns.type("application/json");
				rspns.status(200);
				return new Gson().toJson(sender);
			}
		});
		
		

		/***************************************
		 * Crea un Campana de Envio de Correos *
		 ***************************************/
		post(uriApiBase+"/campaigns", new Route() {
			public String handle(Request rqst, Response rspns) throws Exception {
				rspns.type("application/json");

				System.out.println("POST : " + rqst.contentType());
				System.out.println("POST : " + rqst.body());
				SendEmailPayload payload = objectMapper.readValue(rqst.body(), SendEmailPayload.class);
				Campaign campaign = service.createEmailCampaign(payload);
				if (campaign != null)
					rspns.status(200);
				else
					rspns.status(500);

				return new Gson().toJson(campaign);
			}
		});

		System.out.println("Running service: binaria.sender on : \n" + sender.getUriAccess());
		

		// TimerTask task = new RunTask();
		//
		// Timer timer = new Timer();
		// timer.schedule(task, 1000, 2000);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if (senderService.killme(sender))
					System.out.println(sender.getName()+" says : See you baby!! :(");
				else
					System.out.println(sender.getName()+" says : Can not killme :)");
			}
		});
	}
	
	
}
