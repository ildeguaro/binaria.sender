package com.jmc.binaria.sender.service;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmc.binaria.sender.model.Campaign;
import com.jmc.binaria.sender.model.api.SendEmailPayload;

@RunWith(JUnit4.class)
public class BinariaSenderServiceTest {
	
	private BinariaSenderService service = new BinariaSenderService();
	
	private ObjectMapper objectMapper =  new ObjectMapper();
	
	private String body = "{\n" + 
			"  \"ftpValues\": {\n" + 
			"    \"host\": \"localhost\",\n" + 
			"    \"port\": \"21\",\n" + 
			"    \"username\": \"ftpuser\",\n" + 
			"    \"password\": \"123456\"\n" + 
			"  },\n" + 
			"  \"smtpValues\": {\n" + 
			"    \"hostname\": \"hostname\",\n" + 
			"    \"port\": \"8142\",\n" + 
			"    \"username\": \"username\",\n" + 
			"    \"password\": \"password\",\n" + 
			"    \"from\": \"from\",\n" + 
			"    \"subject\": \"subject\",\n" + 
			"    \"body\": \"<body></body>\",\n" + 
			"    \"attachmenName\": \"SuEdoCuenta\"\n" + 
			"  },\n" + 
			"  \"customerId\":1,\n" + 
			"  \"ordenImpresionId\":\"3\",\n" + 
			"  \"emailDescription\":\"envio mes de enero 2018\",\n" + 
			"  \"packagesName\" : [\"files/pueba-separar/PAQUETE_001.pdf\",\"files/pueba-separar/00_DINNERS_DOCUMENTO.pdf\"]\n" + 
			"}";
	
	@Test
	public void createEmailCampaign() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		SendEmailPayload payload = objectMapper.readValue(body, SendEmailPayload.class);			   
		Campaign campaign = service.createEmailCampaign(payload);
		
	}


}
