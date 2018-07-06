package com.jmc.binaria.sender.util;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jmc.binaria.sender.model.EmailCampaign;
import com.jmc.binaria.sender.model.Sender;
import com.jmc.binaria.sender.model.SmtpSettings;
import com.jmc.binaria.sender.service.EmailCampaignService;
import com.sun.mail.smtp.SMTPTransport;

/**
 *
 * @author ildemaro
 */
public class SenderThread {

	Logger logger = LoggerFactory.getLogger(SenderThread.class);

	private Integer idThread;
	private String nombreDelSender;
	private String de;
	private String contenidoHtmlTemplate;
	private String producto;
	private String subDominio;
	private String asunto;
	private BinariaArchivo manejaArchivos;
	private List<String> direccionesFallidas;
	private List<String> errores;
	private Properties props;
	private String rutaAdjunto;
	private String nombreAdjunto;
	private boolean debug;
	private String name;
	private String direccion;
	private String nombreDestinatario;
	private Sender sender;
	private EmailCampaignService emailCampaignService;
	private SmtpSettings smtpSettings;
	private EmailCampaign email;

	public SenderThread(Properties props, Integer idThread, Sender sender, EmailCampaignService emailCampaignService,
			SmtpSettings smtpSettings) {
		this.props = props;
		this.idThread = idThread;
		this.sender = sender;
		this.emailCampaignService = emailCampaignService;
		this.de = smtpSettings.getFrom();
		this.asunto = smtpSettings.getSubject();
		this.manejaArchivos = new BinariaArchivo();
		this.nombreAdjunto = smtpSettings.getAttachmenName();

	}

	public SenderThread(Properties props, Integer idThread, String direccion, String nombreDestinatario, String de,
			String asunto, String contenidoHtml, String nombreAdjunto, String producto, String rutaAdjunto,
			boolean debug) throws InterruptedException {
		this.producto = producto;
		this.idThread = idThread;
		this.direccion = direccion;
		this.nombreDestinatario = nombreDestinatario;
		this.de = de;
		this.asunto = asunto;
		this.contenidoHtmlTemplate = contenidoHtml;
		this.nombreAdjunto = nombreAdjunto;
		this.rutaAdjunto = rutaAdjunto;
		this.manejaArchivos = new BinariaArchivo();

	}

	public void send(EmailCampaign email) {
		debug = sender.isDebug();
		email.setSenderId(sender.getId());
		SMTPTransport t = null;
		File adjunto = new File(email.getAttachmentPath());
		byte[] pdfArray = null;
		String contenidoHtml = email.getContentEmail();
		try {
			MimeMessage message = null;
			String direccion = email.getAddresses();
			String nombreDestinatario = email.getNames();
			if (direccion != null || direccion != "null") {
				nombreDestinatario = "" + nombreDestinatario.replace("¬", "/");
				direccion = direccion.split(";")[0];
				if (direccion.contains(";")) {
					// cadenaHeader.append(direccion.split(";")[0].replace("@", "="));
				}
				if (direccion.contains(",")) {
					// cadenaHeader.append(direccion.split(",")[0].replace("@", "="));
				} else if (!direccion.contains(";") || !direccion.contains(",")) {
					// cadenaHeader.append(direccion.replace("@", "="));
				}

				contenidoHtml = contenidoHtml.replace("[sunombre]", nombreDestinatario);

				t = JavaMailManager.conectarSMTP(props, debug);
				
				message = JavaMailManager.crearMensaje(contenidoHtml, de, nombreDestinatario + " " + asunto);
				if (direccion.contains(";")) {
					for (String str : direccion.split(";")) {
						message.setRecipient(Message.RecipientType.TO, new InternetAddress(str));
					}
				} else if (direccion.contains(",")) {
					for (String str : direccion.split(",")) {
						message.setRecipient(Message.RecipientType.TO, new InternetAddress(str));
					}
				} else {
					message.setRecipient(Message.RecipientType.TO, new InternetAddress(direccion));
				}

				pdfArray = manejaArchivos.convertirFileADataBinaria(adjunto);
				JavaMailManager.agregarAdjunto(message, pdfArray, nombreAdjunto, ".pdf");

				if (adjunto.exists()) {
					if (!emailCampaignService.emailWasSent(email)) {
						t.sendMessage(message, message.getAllRecipients());
						String response = t.getLastServerResponse();
						String esmtpId = (response.contains("as ")) ? response.split("as ")[1] : "ESMP-ID-NO-FOUND";
						email.setEsmtpId(esmtpId.trim());						
						email.setWasSent(true);
						email.setResponse("sender-thread-" + this.idThread + " entrego el correo exitosamente");
						emailCampaignService.updateSending(email);
						logger.info(email.getResponse());
						adjunto.delete();
					} else {
						email.setResponse(email.getResponse() + "\r " + "sender-thread-" + this.idThread
								+ ": se intento enviar un correo marcado como enviado ");
						emailCampaignService.updateSending(email);
						logger.error("No se enviara el correo a {} porque estaba marcado como enviado ",
								email.getAddresses());
					}
				} else {
					throw new Exception("No se encontro archivo para adjuntar para " + email.getAddresses());
				}

			}

		} catch (Exception e) {
			email.setWasSent(false);
			email.setError(e.getMessage());
			logger.error(email.getError());
			emailCampaignService.updateSending(email);
			try {
				t.close();
			} catch (MessagingException ex) {
				ex.printStackTrace();
			}
		}


	}

	public void run() {
		try {
			this.send(null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public EmailCampaign getEmail() {
		return email;
	}

	public void setEmail(EmailCampaign email) {
		this.email = email;
	}

}
