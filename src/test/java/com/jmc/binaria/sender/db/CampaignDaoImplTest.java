package com.jmc.binaria.sender.db;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.jmc.binaria.sender.model.Customer;
import com.jmc.binaria.sender.model.Campaign;

@RunWith(JUnit4.class)
public class CampaignDaoImplTest {
	
	@Ignore
	@Test
	public void createCampaingTest() {
		Customer customer = new Customer();
		customer.setId(1);
		String nameCampaig = "ENVIOS EMAIL 2018";
		Long ordenImpresionId = 2l ;
		String html = "<body>[cuenta] y [saldoinicial] tambien [saldofinal] con emision [fechaemision] debes pagar [fechamaxpago]</body>";
	    CampaignDao dao = new CampaignDaoImpl();
	    Campaign dato = dao.createCampaing(customer, nameCampaig, ordenImpresionId,html);
	    assertNotNull(dato);	    
	}
	
	@Test
	public void gelAllCampaingTest() {
	    CampaignDao dao = new CampaignDaoImpl();
	    List<Campaign> dato = dao.allCampaign();
	    assertNotNull(dato);	    
	}

}
