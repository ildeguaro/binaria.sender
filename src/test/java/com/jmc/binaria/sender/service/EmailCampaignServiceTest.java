package com.jmc.binaria.sender.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.jmc.binaria.sender.model.EmailCampaign;
import com.jmc.binaria.sender.model.api.EmailEvents;

@RunWith(JUnit4.class)
public class EmailCampaignServiceTest {

	private EmailCampaignService emailCampaignService;

	@Before
	public void setup() {
		emailCampaignService = new EmailCampaignService();
	}

	@Test
	public void test_getEventsEmail() {
		EmailCampaign email = new EmailCampaign();
		email.setEsmtpId("3WwGiVN2TH6sHOYMxvr-aQ");
		email.setId(1);
		EmailEvents result = emailCampaignService.getEventsEmail(email.getId());
		assertNotNull(result);

	}

}
