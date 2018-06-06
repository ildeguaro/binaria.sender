package com.jmc.binaria.sender.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimerTask;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author ildemaro
 */
public class SenderWorker extends TimerTask {

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
	private String nombreAdjunto;
	private boolean debug;
	private String name;

	public SenderWorker(Properties props, Integer idThread, String de, String asunto, String contenidoHtml,
			String nombreAdjunto, String producto, boolean debug) throws InterruptedException {
		this.producto = producto;
		this.idThread = idThread;
		this.de = de;
		this.asunto = asunto;
		this.contenidoHtmlTemplate = contenidoHtml;
		this.nombreAdjunto = nombreAdjunto;

		this.manejaArchivos = new BinariaArchivo();

		this.direccionesFallidas = new ArrayList<String>();
		this.errores = new ArrayList<String>();
		this.props = props;
		this.debug = debug;

	}

	public void run() {
		Transport t = null;
		StringBuilder cadenaHeader;
		File adjunto = null;
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
			System.err.println(contenidoHtml);
			MimeMessage message = null;

			if (direccion != null || direccion != "null") {
				String nombre = "" + nombreDestinatario.replace("¬", "/");
				direccion = direccion.split(";")[0];
				cadenaHeader = new StringBuilder(producto);
				cadenaHeader.append("+");

				if (direccion.contains(";")) {
					cadenaHeader.append(direccion.split(";")[0].replace("@", "="));
				}
				if (direccion.contains(",")) {
					cadenaHeader.append(direccion.split(",")[0].replace("@", "="));
				} else if (!direccion.contains(";") || !direccion.contains(",")) {
					cadenaHeader.append(direccion.replace("@", "="));
				}
				cadenaHeader.append("@");
				cadenaHeader.append(subDominio);

				contenidoHtml = contenidoHtml.replace("[sunombre]", nombre);
				t = JavaMailManager.conectarSMTP(props, debug);

				message = JavaMailManager.crearMensaje(contenidoHtml, de, nombre + " " + asunto);

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

			}

		} catch (Exception e) {
			e.printStackTrace();
			errores.add(e.getMessage());
			if (e.getMessage().contains("Invalid Addresses")) {
				direccionesFallidas.add(direccion);
			}
		}
		if (direccion == null || direccion.contains("null")) {
			// this.mailSender.notify("\n NO SE PUDO ENTREGAR A: <" + nombreDestinatario +
			// "> ... CAUSA : Direccion vacia");
		}
		adjunto.delete();
		k++;
		// }

		// this.mailSender.notify(" Finaliz� el Envio de " + this.getName());

		if (direccionesFallidas.size() > 0) {
		}

		if (errores.size() > 0) {
		}
		try {
			t.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}