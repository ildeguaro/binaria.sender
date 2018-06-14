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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinariaSenderService {
	
	Logger logger = LoggerFactory.getLogger(BinariaSenderService.class);


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

		List<File> listaPaqueteArchivos = new ArrayList<File>();

		List<String> listaDeNombreDePaquetes = new ArrayList<String>();
		listaDeNombreDePaquetes.addAll(Arrays.asList(payload.getPackagesName()));
		File paquetePdf = null;
		logger.info("OBTENIENDO PAQUETES DE FTP");
		FTPUtils.conectar(ftpSettings.getHost(), Integer.parseInt(ftpSettings.getPort()), ftpSettings.getUsername(),
				ftpSettings.getPassword());
		for (String elemento : listaDeNombreDePaquetes) {
			paquetePdf = BinariaUtil.getPaqueteOrdenImpresionDesdeFTP(elemento);
			listaPaqueteArchivos.add(paquetePdf);
		}
		FTPUtils.desconectar();
		logger.info(" REALIZADO OBTENCION PAQUETES DE FTP ");

		
		Campaign campaing;
		Customer customer = new Customer();
		customer.setId(payload.getCustomerId());
		String nameCampaig = payload.getEmailDescription();
		Long ordenImpresionId = Long.parseLong(payload.getOrdenImpresionId());
		campaing = campaignDao.createCampaing(customer, nameCampaig, ordenImpresionId,payload.getEmailTemplate());
		logger.info(" SEPARANDO PDF DE PAQUETES ");
		for (File arch : listaPaqueteArchivos) {
			String dir = "/tmp/PAQUETE_" + arch.getName().replace(".pdf", "");
			BinariaUtil.separarDocumentos(arch, dir);
			File temp = new File(dir);
			encolaDocuments(temp,"PAQUETE_" + arch.getName().replace(".pdf", ""), 
					campaing.getId(), campaing.getEmailTemplate());

		}
		return campaing;
	}

	private void encolaDocuments(File file, String paqueteName, String campaignId, String contentEmail) {		
		try {			
			for (File f : file.listFiles()) {
				String contentHtml = contentEmail;
				sti = new StringTokenizer(f.getName(), "|");
				sti.nextToken();
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
					contentHtml = contentHtml.replace(key, value);
					camposDeBusqueda.append(key.replace("[", "").replace("]","")).append("=").append(value).append(";");
				}
				EmailCampaign ec = new EmailCampaign();
				ec.setCampaignId(campaignId);
				ec.setAddresses(direccion);
				ec.setNames(nombreDestinatario);
				ec.setPackageId(paqueteName);
				ec.setAttachmentPath(f.getCanonicalPath());
				
				ec.setContentEmail(contentHtml);
				ec.setFieldsSearch(camposDeBusqueda.toString());
				emailCampaignDao.createEmailCampaing(ec);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		
	public List<Campaign> getAllCampaign(){
		return campaignDao.allCampaign();
	}
	
	public List<EmailCampaign> getEmailCampaignByBasicSearch(long ordenId, String address, String names, String searchFields) {
		return emailCampaignDao.findEmailCampaignByOrdenIdAddressNamesOrFieldsSearch(ordenId, address, names, searchFields);
	}

}
