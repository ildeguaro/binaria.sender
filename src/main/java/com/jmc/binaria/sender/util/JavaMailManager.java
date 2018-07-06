package com.jmc.binaria.sender.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.sun.mail.smtp.SMTPTransport;

public class JavaMailManager {

	private static Session session;
	private static SMTPTransport t;
	private static List<String> listaDirecciones;

	synchronized public static Transport conectarSMTP(Properties prop, String returnPath, boolean debug)
			throws MessagingException {
		prop.put("mail.smtp.from", returnPath);
		session = Session.getDefaultInstance(prop, null);
		session.setDebug(debug);

		t = (SMTPTransport) session.getTransport("smtp");

		if (prop.getProperty("mail.smtp.starttls.enable").equals("true")) {
			if (!t.isConnected()) {
				t.connect(prop.getProperty("mail.smtp.user"), prop.getProperty("mail.smtp.psw"));
			} else if (!t.isConnected()) {
				t.connect();
			}
		}

		return t;
	}

	synchronized public static SMTPTransport conectarSMTP(Properties prop, boolean debug) throws MessagingException {
		session = Session.getDefaultInstance(prop, null);
		session.setDebug(debug);
		t = (SMTPTransport) session.getTransport("smtp");

		if (prop.getProperty("mail.smtp.starttls.enable").equals("true")) {
			if (!t.isConnected()) {
				t.connect(prop.getProperty("mail.smtp.user"), prop.getProperty("mail.smtp.psw"));
			} else if (!t.isConnected()) {
				t.connect();
			}
		}

		return t;
	}

	public static MimeMessage crearMensaje(String contenidoHtml, String de, String asunto) throws MessagingException {
		MimeMultipart completo = new MimeMultipart();
		BodyPart cuerpo = new MimeBodyPart();
		cuerpo.setContent(contenidoHtml, "text/html; charset=ISO-8859-1");

		MimeMessage message = new MimeMessage(session);

		completo.addBodyPart(cuerpo);

		message.setFrom(new InternetAddress(de));
		message.setSubject(asunto);
		message.setContent(completo);

		return message;
	}

	synchronized public static void personalizarBody(String nombre, String contenidoHtml, MimeMessage message)
			throws MessagingException {

		MimeMultipart completo = new MimeMultipart();
		BodyPart cuerpo = new MimeBodyPart();
		cuerpo.setContent(contenidoHtml.replace("[sunombre]", nombre), "text/html; charset=utf-8");
		completo.addBodyPart(cuerpo);
		message.setSubject(nombre + " " + message.getSubject());
		message.setContent(completo);

	}

	synchronized public static void agregarAdjunto(MimeMessage message, byte[] adjunto, String nombreDeAdjunto,
			String extension) throws MessagingException, IOException {

		MimeMultipart completo = null;

		if (message.getContent() != null) {
			completo = (MimeMultipart) message.getContent();
		} else {
			completo = new MimeMultipart();
		}

		BodyPart cuerpo = new MimeBodyPart();
		cuerpo.setDataHandler(
				new DataHandler(new ByteArrayDataSource(adjunto, "application/" + extension.replace(".", ""))));
		cuerpo.setFileName(nombreDeAdjunto + extension);

		completo.addBodyPart(cuerpo);
		message.setContent(completo);
	}

	public static File guardarDireccionesFallidas(File archivo) throws FileNotFoundException {

		PrintStream ps = new PrintStream(archivo);
		for (String linea : listaDirecciones) {
			ps.println(linea);
			System.out.println("Leyendo : " + linea);
		}
		ps.close();
		return archivo;
	}

	synchronized public static void agregarDireccionesFallidas(String valor) {
		listaDirecciones.add(valor);
	}

	synchronized public static void agregarDireccionesFallidas(List<String> lista) {
		for (String valor : lista) {
			listaDirecciones.add(valor);
		}
	}

	synchronized public static void agregarDireccionesFallidas(File archivoIndividual) throws IOException {

		FileInputStream fis = new FileInputStream(archivoIndividual);
		InputStreamReader inputReader = new InputStreamReader(fis, "ISO8859_1");
		BufferedReader entrada = new BufferedReader(inputReader);
		String cadena = "";
		while ((cadena = entrada.readLine()) != null) {
			listaDirecciones.add(cadena.trim());
		}

		entrada.close();
		inputReader.close();
	}

	public static void crearListaCorreosErrados() {
		listaDirecciones = new ArrayList<String>();
	}

}
