package com.jmc.binaria.sender.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ntp.TimeStamp;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmc.binaria.sender.db.EmailCampaignDao;
import com.jmc.binaria.sender.db.EmailCampaignDaoImpl;
import com.jmc.binaria.sender.db.EmailEventDao;
import com.jmc.binaria.sender.db.EmailEventDaoImpl;
import com.jmc.binaria.sender.model.EmailCampaign;
import com.jmc.binaria.sender.model.EmailEvent;
import com.jmc.binaria.sender.model.Sender;
import com.jmc.binaria.sender.model.api.EmailEvents;
import com.jmc.binaria.sender.model.api.client.EmailEventResponse;
import static com.jmc.binaria.sender.model.api.EmailEvents.*;

public class EmailCampaignService {

	static Logger logger = LoggerFactory.getLogger(EmailCampaignService.class);

	private EmailCampaignDao emailCampaignDao;

	private EmailEventDao emailEventDao;

	private static String EVENTS_EMAIL_URI = "monitor.jmcxpress.com";

	private static String EVENTS_EMAIL_ENDPOINT = "/event/";

	private final ObjectMapper objectMapper;

	public EmailCampaignService() {
		emailCampaignDao = new EmailCampaignDaoImpl();
		emailEventDao = new EmailEventDaoImpl();
		objectMapper = new ObjectMapper();
		// objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
		// false);
	}

	public EmailCampaign selectEmailCampaignToSendAssigned(Sender sender) {
		return emailCampaignDao.selectEmailToSendAssigned(sender.getId());

	}

	public List<EmailCampaign> selectEmailCampaignToSendNoAssigned(Sender sender, int quantity) {
		List<EmailCampaign> list = emailCampaignDao.selectEmailToSendNoAssigned(quantity, sender);
		list.parallelStream().forEach(e -> {
			e.setSenderId(sender.getId());
			emailCampaignDao.updateSending(e);
		});
		return list;

	}

	public void updateSending(EmailCampaign email) {
		this.emailCampaignDao.updateSending(email);
	}

	public boolean emailWasSent(EmailCampaign email) {
		return this.emailCampaignDao.getEmailSent(email);
	}

	public EmailCampaign getEmailById(long id) {
		return this.emailCampaignDao.findById(id);
	}

	public EmailEvents getEventsEmail(long emailId) {
		EmailCampaign email = this.getEmailById(emailId);
		EmailEvents events = new EmailEvents();
		if (email != null) {
			events.setEmail(email.getAddresses());
			events.setRecipent(email.getNames());
			events.setId(email.getEsmtpId());
			events.setSent(email.getSendingDate());
		}
		List<EmailEvent> emailEventList = emailEventDao.findByEsmptpId(email.getEsmtpId());
		emailEventList.forEach(e -> {
			if (e.getEventType().equals(PROCESSED_TYPE)) {
				events.setProcessed(true);
				events.setProcessedDate(e.getEventDate());
			}

			if (e.getEventType().equals(DELIVERED_TYPE)) {
				events.setDelivered(true);
				events.setDeliveredDate(e.getEventDate());
			}
			if (e.getEventType().equals(OPEN_TYPE)) {
				events.setOpen(true);
				events.setOpenDate(e.getEventDate());
			}
		});
		return events;
	}

	@Deprecated
	public EmailEvents getEventsEmailViaExternalService(long emailId) {
		EmailCampaign email = this.getEmailById(emailId);
		DefaultHttpClient httpclient = new DefaultHttpClient();
		List<EmailEventResponse> emailEventResponse = new ArrayList<EmailEventResponse>();
		EmailEvents events = new EmailEvents();
		try {
			if (email != null) {
				events.setEmail(email.getAddresses());
				events.setRecipent(email.getNames());
				events.setId(email.getEsmtpId());
				events.setSent(email.getSendingDate());
				HttpHost target = new HttpHost(EVENTS_EMAIL_URI);
				HttpGet getRequest = new HttpGet(EVENTS_EMAIL_ENDPOINT + email.getEsmtpId());
				logger.info("Gettings Events to {}:{} ", email.getEsmtpId(), email.getAddresses());
				logger.info("Executing request to {}" + target);
				HttpResponse httpResponse = httpclient.execute(target, getRequest);
				HttpEntity entity = httpResponse.getEntity();
				logger.info("Http Status Code : {} ", httpResponse.getStatusLine());

				if (entity != null) {
					Object object = objectMapper
							.readValue(EntityUtils.toString(entity).replace("<", "").replace(">", ""), Object.class);
					emailEventResponse = objectMapper.convertValue(object,
							new TypeReference<List<EmailEventResponse>>() {
							});
					logger.info("{}", object);
				}

				emailEventResponse.forEach(e -> {
					if (e.getEvent().equals(PROCESSED_TYPE)) {
						events.setProcessed(true);
						events.setProcessedDate(new TimeStamp(e.getTimestamp()).getDate());
					}

					if (e.getEvent().equals(DELIVERED_TYPE)) {
						events.setDelivered(true);
						events.setDeliveredDate(new TimeStamp(e.getTimestamp()).getDate());
					}
					if (e.getEvent().equals(OPEN_TYPE)) {
						events.setOpen(true);
						events.setOpenDate(new TimeStamp(e.getTimestamp()).getDate());
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
			logger.info("Done");
		}

		return events;
	}

}
