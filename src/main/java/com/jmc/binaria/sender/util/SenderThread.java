package com.jmc.binaria.sender.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public SenderThread(Properties props, Integer idThread, String direccion, String nombreDestinatario, String de, String asunto, String contenidoHtml,
			String nombreAdjunto, String producto, String rutaAdjunto, boolean debug) throws InterruptedException {
		this.producto = producto;
		this.idThread = idThread;
		this.direccion= direccion;
		this.nombreDestinatario = nombreDestinatario;
		this.de = de;
		this.asunto = asunto;
		this.contenidoHtmlTemplate = contenidoHtml;
		this.nombreAdjunto = nombreAdjunto;
		this.rutaAdjunto = rutaAdjunto;

		this.manejaArchivos = new BinariaArchivo();

		this.direccionesFallidas = new ArrayList<String>();
		this.errores = new ArrayList<String>();
		this.props = props;
		this.debug = debug;

	}

	public boolean send() throws Exception {
		Transport t = null;
		StringBuilder cadenaHeader;
		File adjunto = new File(rutaAdjunto);
		StringTokenizer sti = null;

		String id;
		byte[] pdfArray = null;

		//sti = new StringTokenizer(adjunto.getName(), "|");
		//id = sti.nextToken();
		//ti.nextToken();
		//nombreDestinatario = sti.nextToken();
		String contenidoHtml = contenidoHtmlTemplate;
		try {

			
			MimeMessage message = null;

			if (direccion != null || direccion != "null") {
				nombreDestinatario = "" + nombreDestinatario.replace("¬", "/");
				direccion = direccion.split(";")[0];
				//cadenaHeader = new StringBuilder(producto);
				//cadenaHeader.append("+");

				if (direccion.contains(";")) {
					//cadenaHeader.append(direccion.split(";")[0].replace("@", "="));
				}
				if (direccion.contains(",")) {
					//cadenaHeader.append(direccion.split(",")[0].replace("@", "="));
				} else if (!direccion.contains(";") || !direccion.contains(",")) {
					//cadenaHeader.append(direccion.replace("@", "="));
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
				t.sendMessage(message, message.getAllRecipients());
				if (adjunto.exists())
					adjunto.delete();
				return true;

			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				t.close();
			} catch (MessagingException ex) {
				ex.printStackTrace();
			}
			throw e;
		}
		return false;

	}

}
