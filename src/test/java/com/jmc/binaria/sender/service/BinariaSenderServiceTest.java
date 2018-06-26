package com.jmc.binaria.sender.service;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Ignore;
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
			"  },  \n" + 
			"  \"emailTemplateBase64\": \"PGJvZHk+W2N1ZW50YV0geSBbc2FsZG9pbmljaWFsXSB0YW1iaWVuIFtzYWxkb2ZpbmFsXSBjb24gZW1pc2lvbiBbZmVjaGFlbWlzaW9uXSBkZWJlcyBwYWdhciBbZmVjaGFtYXhwYWdvXTwvYm9keQ==\",\n" + 
			"\n" + 
			"  \"customerId\":1,\n" + 
			"  \"ordenImpresionId\":\"3500\",\n" + 
			"  \"emailDescription\":\"ENVIO CAMPANA FEBRERO 2018\",\n" + 
			"  \"packagesName\" : [\"files/separa/PAQUETE_001.pdf\"]\n" + 
			"}";
	
	@Test
	public void createEmailCampaign() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		SendEmailPayload payload = objectMapper.readValue(body, SendEmailPayload.class);			   
		Campaign campaign = service.createEmailCampaign(payload);
		assertNotNull(campaign);		
	}
		
	@Ignore
	@Test
	public void getEmailCampaignBasicOnlyOrdenId() throws JsonParseException, JsonMappingException, IOException, InterruptedException {		
		service.getEmailCampaignByBasicSearch(1, "", "", "");
		//assertNotNull(campaign);		
	}
	
	@Ignore
	@Test
	public void getEmailCampaignBasicOnlyAddress() throws JsonParseException, JsonMappingException, IOException, InterruptedException {		
		service.getEmailCampaignByBasicSearch(0, "jcarslosm@gmail.com", "", "");
		//assertNotNull(campaign);		
	}
	
	@Ignore
	@Test
	public void getEmailCampaignBasicOnlYNames() throws JsonParseException, JsonMappingException, IOException, InterruptedException {		
		service.getEmailCampaignByBasicSearch(0, "", "NMD", "");
		//assertNotNull(campaign);		
	}
	@Ignore
	@Test
	public void getEmailCampaignBasicOnlyFieldsSearch() throws JsonParseException, JsonMappingException, IOException, InterruptedException {		
		service.getEmailCampaignByBasicSearch(0, "", "", "MY");
		//assertNotNull(campaign);		
	}
	
	@Test
	public void getEmailCampaignBasicOnlyOrdenIdAndAddress() throws JsonParseException, JsonMappingException, IOException, InterruptedException {		
		service.getEmailCampaignByBasicSearch(1, "jcarslosm@gmail.com", "", "");
		//assertNotNull(campaign);		
	}


}
