package com.jmc.binaria.sender;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import static spark.Spark.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;

import com.jmc.binaria.sender.model.Campaign;
import com.jmc.binaria.sender.model.EmailCampaign;
import com.jmc.binaria.sender.model.Sender;
import com.jmc.binaria.sender.model.User;
import com.jmc.binaria.sender.model.api.SendEmailPayload;
import com.jmc.binaria.sender.service.BinariaSenderService;
import com.jmc.binaria.sender.service.SenderService;
import com.jmc.binaria.sender.service.UserService;
import com.jmc.binaria.sender.util.SenderWorker;

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
	
	private static UserService userService;

	private static Sender sender;
	
	private static HashMap<String, Object> mapLogin = new HashMap<String, Object>();

	public static void main(String[] args) throws InterruptedException, NumberFormatException, IOException {

		final ThymeleafTemplateEngine engineView = new ThymeleafTemplateEngine();
		
		service = new BinariaSenderService();
		senderService = new SenderService();
		userService = new UserService();
		objectMapper = new ObjectMapper();
		int portBase  = Integer.parseInt(App.getPropertiesApp().getProperty("app.port-base"));
		long customerId  = Long.parseLong(App.getPropertiesApp().getProperty("app.client-id"));		
		sender = senderService.registraMe(InetAddress.getLocalHost().getHostAddress(), portBase, customerId);
		port(senderService.getPort());

		staticFiles.location("/templates");

		String uriApiBase = "/" + sender.getName() + API_PATH;
		
		/******************************************************************/
		
		final SenderWorker worker = new SenderWorker(sender);
		Timer timer = new Timer();
		timer.schedule(worker, 1000, 2000);
		/******************************************************************/

		/**********************************
		 * Home *
		 **********************************/
		get("/" + sender.getName() + "/:hash", new Route() {
			public String handle(Request rqst, Response rspns) throws Exception {				
				String hash = rqst.params(":hash");
				Map<String, Object> varables = new HashMap<String, Object>();
				varables.put("instance", ":"+port()+"/"+sender.getName());
				String view ="home";				
				return App.interceptorLogin(hash,varables,view);
			}
		});
		post("/" + sender.getName() + "/login", new Route() {
			public String handle(Request rqst, Response rspns) throws Exception {
				String login = rqst.queryParams("login");
				String password = rqst.queryParams("password");
				Map<String, Object> varables = new HashMap<String, Object>();
				if (login==null)
					login = "";
				if (password==null)
					password ="";
				User user = userService.autenticar(login, password);
				if (user!=null) {
					mapLogin.put(user.getPassword(), user);
					varables.put("user", user);
					rspns.redirect(user.getPassword());
				}		
				rspns.redirect("13132132");
				return "/" + sender.getName() + "/login";
			}
		});
		

		/**********************************
		 * Busqueda Basica *
		 **********************************/
		get("/" + sender.getName() + "/busqueda-basica/:hash", new Route() {
			public String handle(Request rqst, Response rspns) throws Exception {
				String hash = rqst.params(":hash");
				List<Campaign> campaigns = service.getAllCampaign();
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("ordenes",campaigns);
				variables.put("instance", sender.getName());
				String view ="busqueda-basica";				
				return App.interceptorLogin(hash,variables,view);
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
		get("/" + sender.getName() + "/find/:hash", new Route() {
			public String handle(Request rqst, Response rspns) throws Exception {
				String hash = rqst.params(":hash");

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
				
				String view ="busqueda-basica";				
				return App.interceptorLogin(hash,variables,view);
				
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
	
	public static Properties getPropertiesApp() throws IOException {	
		InputStream s = App.class.getResourceAsStream("/config.properties");
		Properties props = new Properties();
		props.load(s);
		return props;
	}
	
	public static String interceptorLogin(String hash, Map<String, Object> variables, String view) {
		if (hash==null)
			return new ThymeleafTemplateEngine()
					.render(new ModelAndView(new HashMap<String, String>(), "login"));
		User user =  (User) mapLogin.get(hash);
		if (user==null)
			return new ThymeleafTemplateEngine()
				.render(new ModelAndView(new HashMap<String, String>(), "login"));
		else {			
			variables.put("user",user);
			return new ThymeleafTemplateEngine().render(new ModelAndView(variables,view));
		}
		
		
		
	}
	
	
}
