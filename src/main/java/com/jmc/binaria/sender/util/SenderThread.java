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

	public SenderThread(Properties props, Integer idThread, String de, String asunto, String contenidoHtml,
			String nombreAdjunto, String producto, String rutaAdjunto, boolean debug) throws InterruptedException {
		this.producto = producto;
		this.idThread = idThread;
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

		String direccion;
		String nombreDestinatario;
		String id;
		double k = 1;
		byte[] pdfArray = null;

		sti = new StringTokenizer(adjunto.getName(), "|");
		id = sti.nextToken();
		direccion = sti.nextToken();
		nombreDestinatario = sti.nextToken();
		String contenidoHtml = contenidoHtmlTemplate;
		logger.info("Contenido html En Sender {} ", contenidoHtml);
		try {

			/*****************************************/
			String chain = adjunto.getName().replace("|", "~");
			String[] valuesChain = chain.split("~");

			String[] mapValueString = valuesChain[valuesChain.length - 1].replace(".pdf", "").replace("^", "~")
					.split("~");
			for (String val : mapValueString) {
				String[] splitedString = val.split("=");
				String key = splitedString[0].replace("{", "");
				String value = splitedString[1].replace("}", "");
				contenidoHtml = contenidoHtml.replace(key, value);
			}
			logger.info(" Body {}: ", contenidoHtml);
			MimeMessage message = null;

			if (direccion != null || direccion != "null") {
				String nombre = "" + nombreDestinatario.replace("¬", "/");
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
				//cadenaHeader.append("@");
				//cadenaHeader.append(subDominio);

				contenidoHtml = contenidoHtml.replace("[sunombre]", nombre);
				logger.info(" T : {}", t);
				t = JavaMailManager.conectarSMTP(props, debug);
				logger.info(" T : ", t);
				message = JavaMailManager.crearMensaje(contenidoHtml, de, nombre + " " + asunto);
				logger.info(" Message : ", message);
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