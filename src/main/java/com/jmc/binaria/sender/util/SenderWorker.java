package com.jmc.binaria.sender.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmc.binaria.sender.model.EmailCampaign;
import com.jmc.binaria.sender.model.Sender;
import com.jmc.binaria.sender.model.SmtpSettings;
import com.jmc.binaria.sender.service.CustomerService;
import com.jmc.binaria.sender.service.EmailCampaignService;
import com.jmc.binaria.sender.service.SenderService;

public class SenderWorker extends TimerTask {

	Logger logger = LoggerFactory.getLogger(SenderWorker.class);

	private EmailCampaignService emailCampaignService;

	private SenderService senderService;

	private CustomerService customerService;

	private Sender sender;

	private SmtpSettings smtpSettings;

	private Properties props;
	
	public static List<EmailCampaign> emailList;
	
	private static int count;
	
	private int threadsToSend;
	
	private List<SenderThread> threadList;

	public SenderWorker(Sender sender, int threadsToSend) {
		emailList = new ArrayList<>();
		this.senderService = new SenderService();
		this.sender = sender;
		this.emailCampaignService = new EmailCampaignService();
		this.customerService = new CustomerService();
		this.smtpSettings = this.customerService.getSmtpSetting(sender.getCustomerId());
		this.props = new Properties();
		this.props.put("mail.smtp.auth", "true");
		this.props.put("mail.smtp.starttls.enable", "true");
		this.props.put("mail.smtp.host", smtpSettings.getHostname());
		this.props.put("mail.smtp.port", smtpSettings.getPort());
		this.props.put("mail.smtp.user", smtpSettings.getUsername());
		this.props.put("mail.smtp.psw", smtpSettings.getPassword());
		this.threadsToSend = threadsToSend;
		logger.info("Properties : {}", this.props);
		threadList = new ArrayList<>();
		if (threadsToSend > 0) {
			for (int i = 0; i < threadsToSend; i++) {
				threadList.add(new SenderThread(this.props,(i+1),this.sender,this.emailCampaignService,this.smtpSettings));
			}
			logger.info("Se registraron {} Thread Senders",threadsToSend);
		}else {
			threadList.add(new SenderThread(this.props,1,this.sender,this.emailCampaignService,this.smtpSettings));
			logger.info("Se registro 1 Thread Senders");
		}
		
		
	}

	@Override	
	public void run() {
		this.sender = senderService.senderByName(sender.getName());
		if (sender.isEnabled()) {
			logger.debug("{} : buscando correo para enviar...", this.sender.getName());
			if (sender.getAsignationType().equals(Sender.ASSIGN_ANY)) {
				count = 0;
				emailList  = emailCampaignService.selectEmailCampaignToSendNoAssigned(sender, this.threadsToSend);
				emailList.parallelStream().forEach(e-> {
					new SenderThread(this.props,1,this.sender,this.emailCampaignService,this.smtpSettings).send(e);					
				});				
			}
			
		}
		
	}
	public static synchronized boolean canSent() {		
		return (count+1)==emailList.size();
	}
	
	public static synchronized EmailCampaign getEmailItem() {
		count++;
		if (emailList.size() > 0)
			return emailList.get((count-1));
		return null;
	}
	
	public void run2() {
		this.sender = senderService.senderByName(sender.getName());
		if (sender.isEnabled()) {
			logger.debug("{} : buscando correo para enviar...", this.sender.getName());
			EmailCampaign email = null;
			if (sender.getAsignationType().equals(Sender.ASSIGN_ANY))
				email = emailCampaignService.selectEmailCampaignToSendNoAssigned(sender,10).get(0);
			else
				email = emailCampaignService.selectEmailCampaignToSendAssigned(sender);
			if (email != null) {
				email.setSenderId(sender.getId());
				emailCampaignService.updateSending(email);
				logger.info("{} : encontrado {} para enviar", this.sender.getName(), email.getAddresses());
				try {
//					if (new SenderThread(this.props, 1, email.getAddresses(), email.getNames(), smtpSettings.getFrom(), smtpSettings.getSubject(),
//							email.getContentEmail(), smtpSettings.getAttachmenName(), email.getCampaignName(),
//							email.getAttachmentPath(), this.sender.isDebug()).send());

					logger.info("{} : enviado correctamente a {}", this.sender.getName(), email.getAddresses());
					email.setWasSent(true);

				} catch (Exception e) {
					e.printStackTrace();
					email.setWasSent(false);
					email.setError(e.getMessage() + " Causa: " + e.getCause());
					logger.error("{} : error intentando enviar  a: {}. Causa:  ", this.sender.getName(),
							email.getAddresses(), e.getCause());
				}
				
				emailCampaignService.updateSending(email);

			}
		}

	}

}
