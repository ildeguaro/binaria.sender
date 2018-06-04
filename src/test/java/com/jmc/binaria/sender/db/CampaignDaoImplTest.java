package com.jmc.binaria.sender.db;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.jmc.binaria.sender.model.Customer;
import com.jmc.binaria.sender.model.Campaign;

@RunWith(JUnit4.class)
public class CampaignDaoImplTest {
	
	@Test
	public void createEmailCampaingTest() {
		Customer customer = new Customer();
		customer.setId(1);
		String nameCampaig = "ENVIOS EMAIL 2018";
		Long ordenImpresionId = 2l ;
	    CampaignDao dao = new CampaignDaoImpl();
	    Campaign dato = dao.createCampaing(customer, nameCampaig, ordenImpresionId);
	    assertNotNull(dato);	    
	}

}
