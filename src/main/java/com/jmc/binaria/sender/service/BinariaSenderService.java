package com.jmc.binaria.sender.service;

import com.jmc.binaria.sender.model.EmailCampaign;
import com.jmc.binaria.sender.model.FtpSettings;
import com.jmc.binaria.sender.model.SmtpSettigs;
import com.jmc.binaria.sender.util.BinariaUtil;
import com.jmc.binaria.sender.util.FTPUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class BinariaSenderService {

	private Properties props;

	private FtpSettings ftpSettings;

	public EmailCampaign createEmailCampaign(FtpSettings ftpSettings, SmtpSettigs stmpSettings, String[] packages)
			throws IOException, InterruptedException {
		this.ftpSettings = ftpSettings;
		props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", stmpSettings.getHostname());
		props.put("mail.smtp.port", stmpSettings.getPort());
		props.put("mail.smtp.user", stmpSettings.getUsername());
		props.put("mail.smtp.psw", stmpSettings.getUsername());
		props.put("mail.smtp.ssl.enable", "true");
		EmailCampaign campaing = new EmailCampaign();
		System.out.println("Properties Sendemail : " + props);

		List<File> listaPaqueteArchivos = new ArrayList<File>();

		List<String> listaDeNombreDePaquetes = new ArrayList<String>();
		listaDeNombreDePaquetes.addAll(Arrays.asList(packages));
		File paquetePdf = null;
		System.out.println("OBTENIENDO PAQUETES DE FTP");
		FTPUtils.conectar(ftpSettings.getHost(), Integer.parseInt(ftpSettings.getPort()),
				ftpSettings.getUsername(), ftpSettings.getPassword());
		for (String elemento : listaDeNombreDePaquetes) {
			System.out.println("---- > "+elemento);
			paquetePdf = BinariaUtil.getPaqueteOrdenImpresionDesdeFTP(elemento);
			listaPaqueteArchivos.add(paquetePdf);
			System.out.println("Descargado : " + paquetePdf.getAbsolutePath());
		}
		FTPUtils.desconectar();
		System.out.println(" REALIZADO OBTENCION PAQUETES DE FTP ");

		System.out.println(" SEPARANDO PDF DE PAQUETES ");
		for (File arch : listaPaqueteArchivos) {
			String dir = "/tmp/PAQUETE_" + arch.getName().replace(".pdf", "");
			BinariaUtil.separarDocumentos(arch, dir);
			File temp = new File(dir);
			// colaArchivosEnvio.addAll(Arrays.asList(temp.listFiles()));
			// Encolar en la tabla de envios
		}
		
		return campaing;
	}

}
