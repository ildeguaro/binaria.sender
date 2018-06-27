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

import org.bouncycastle.util.encoders.Base64;
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
		logger.info(" FINALIZADA OBTENCION PAQUETES DE FTP ");

		
		Campaign campaing;
		Customer customer = new Customer();
		customer.setId(payload.getCustomerId());
		String nameCampaig = payload.getEmailDescription();
		Long ordenImpresionId = Long.parseLong(payload.getOrdenImpresionId());
		String emailTemplate = new String(Base64.decode(payload.getEmailTemplateBase64()));
		campaing = campaignDao.createCampaing(customer, nameCampaig, ordenImpresionId, emailTemplate);
		logger.info(" SEPARANDO PDF DE PAQUETES Y ENCOLANDO DETALLE DE ENVIO PARA LA ORDEN: {}",campaing.getId());
		int count = 0;
		for (File arch : listaPaqueteArchivos) {
			String dir = "/tmp/SNDR_PKG_" + arch.getName().replace(".pdf", "");
			count+=BinariaUtil.separarDocumentosYEncolarEnvioPorPdf(arch, dir, campaing);
		}
		logger.info(" CREADA LA ORDEN DE ENVIO {}",campaing.getId());
		logger.info(" ENVIOS POR REALIZAR {}",count);
		return campaing;
	}	
		
	public List<Campaign> getAllCampaign(){
		return campaignDao.allCampaign();
	}
	
	public List<EmailCampaign> getEmailCampaignByBasicSearch(long ordenId, String address, String names, String searchFields) {
		return emailCampaignDao.findEmailCampaignByOrdenIdAddressNamesOrFieldsSearch(ordenId, address, names, searchFields);
	}

}
