package com.jmc.binaria.sender.service;

import com.jmc.binaria.sender.db.CampaignDao;
import com.jmc.binaria.sender.db.CampaignDaoImpl;
import com.jmc.binaria.sender.db.EmailCampaignDao;
import com.jmc.binaria.sender.db.EmailCampaignDaoImpl;
import com.jmc.binaria.sender.model.Customer;
import com.jmc.binaria.sender.model.EmailCampaign;
import com.jmc.binaria.sender.model.Campaign;
import com.jmc.binaria.sender.model.FtpSettings;
import com.jmc.binaria.sender.model.api.SendEmailPayload;
import com.jmc.binaria.sender.util.BinariaUtil;
import com.jmc.binaria.sender.util.FTPUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

public class BinariaSenderService {

	private Properties props;

	private FtpSettings ftpSettings;

	private CampaignDao campaignDao;

	private EmailCampaignDao emailCampaignDao;

	private StringTokenizer sti;

	public BinariaSenderService() {
		campaignDao = new CampaignDaoImpl();
		emailCampaignDao = new EmailCampaignDaoImpl();
	}

	public Campaign createEmailCampaign(SendEmailPayload payload) throws IOException, InterruptedException {
		this.ftpSettings = payload.getFtpValues();
		props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", payload.getSmtpValues().getHostname());
		props.put("mail.smtp.port", payload.getSmtpValues().getPort());
		props.put("mail.smtp.user", payload.getSmtpValues().getUsername());
		props.put("mail.smtp.psw", payload.getSmtpValues().getPassword());
		props.put("mail.smtp.ssl.enable", "true");

		System.out.println("Properties Sendemail : " + props);

		List<File> listaPaqueteArchivos = new ArrayList<File>();

		List<String> listaDeNombreDePaquetes = new ArrayList<String>();
		listaDeNombreDePaquetes.addAll(Arrays.asList(payload.getPackagesName()));
		File paquetePdf = null;
		System.out.println("OBTENIENDO PAQUETES DE FTP");
		FTPUtils.conectar(ftpSettings.getHost(), Integer.parseInt(ftpSettings.getPort()), ftpSettings.getUsername(),
				ftpSettings.getPassword());
		for (String elemento : listaDeNombreDePaquetes) {
			paquetePdf = BinariaUtil.getPaqueteOrdenImpresionDesdeFTP(elemento);
			listaPaqueteArchivos.add(paquetePdf);
		}
		FTPUtils.desconectar();
		System.out.println(" REALIZADO OBTENCION PAQUETES DE FTP ");

		
		Campaign campaing;
		Customer customer = new Customer();
		customer.setId(payload.getCustomerId());
		String nameCampaig = payload.getEmailDescription();
		Long ordenImpresionId = Long.parseLong(payload.getOrdenImpresionId());
		campaing = campaignDao.createCampaing(customer, nameCampaig, ordenImpresionId);
		System.out.println(" SEPARANDO PDF DE PAQUETES ");
		for (File arch : listaPaqueteArchivos) {
			String dir = "/tmp/PAQUETE_" + arch.getName().replace(".pdf", "");
			BinariaUtil.separarDocumentos(arch, dir);
			File temp = new File(dir);
			encolaDocuments(temp,"PAQUETE_" + arch.getName().replace(".pdf", ""), campaing.getId());

		}
		return campaing;
	}

	private void encolaDocuments(File file, String paqueteName, String campaignId) {

		// String id = sti.nextToken();
		
		try {
			
			for (File f : file.listFiles()) {
				sti = new StringTokenizer(f.getName(), "|");
				String id = sti.nextToken();
				String direccion = sti.nextToken();
				String nombreDestinatario = sti.nextToken();
				StringBuilder camposDeBusqueda = new StringBuilder();

				String chain = f.getName().replace("|", "~");
				String[] valuesChain = chain.split("~");

				String[] mapValueString = valuesChain[valuesChain.length - 1].replace(".pdf", "").replace("^", "~")
						.split("~");

				for (String val : mapValueString) {
					String[] splitedString = val.split("=");
					String key = splitedString[0].replace("{", "");
					String value = splitedString[1].replace("}", "");
					camposDeBusqueda.append(key).append("=").append(value).append(";");
				}
				EmailCampaign ec = new EmailCampaign();
				ec.setCampaignId(campaignId);
				ec.setAddresses(direccion);
				ec.setNames(nombreDestinatario);
				ec.setPackageId(paqueteName);
				ec.setFieldsSearch(camposDeBusqueda.toString());
				emailCampaignDao.createEmailCampaing(ec);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
