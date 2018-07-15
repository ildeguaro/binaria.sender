package com.jmc.binaria.sender.util;

import com.jmc.binaria.sender.db.EmailCampaignDao;
import com.jmc.binaria.sender.db.EmailCampaignDaoImpl;
import com.jmc.binaria.sender.db.EmailCategoryDao;
import com.jmc.binaria.sender.db.EmailCategoryDaoImpl;
import com.jmc.binaria.sender.model.Campaign;
import com.jmc.binaria.sender.model.EmailCampaign;
import com.jmc.binaria.sender.model.EmailCategory;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinariaUtil {

	static Logger logger = LoggerFactory.getLogger(BinariaUtil.class);

	private static EmailCampaignDao emailCampaignDao = new EmailCampaignDaoImpl();
	
	private static EmailCategoryDao emailCategoryDao = new EmailCategoryDaoImpl();

	public BinariaUtil() {
		// Nothing to do
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static int separarDocumentosYEncolarEnvioPorPdf(File archivoPadre, String ruta, Campaign campaign) {
		int documentCounts = 0;

		try {
			PdfReader pdfReaderPadre = new PdfReader(new FileInputStream(archivoPadre));
			List<Map> bookmarksLst = SimpleBookmark.getBookmark(pdfReaderPadre);
			File pdfNuevo = null;
			Document documentoHijo = null;
			PdfCopy copia = null;
			PdfImportedPage page = null;
			StringTokenizer sti = null;
			String correlativo = null;
			String nombre = null;
			String identificador = null;
			String extra = null;
			int pagInicio = 0;
			int pagFin = 0;
			logger.info(" Bookmark Size : {} ", bookmarksLst.size());

			for (int i = 0; i < bookmarksLst.size(); i++) {
				sti = new StringTokenizer(bookmarksLst.get(i).get("Title").toString(), "|");
				String destinatario = "";
				if (sti != null && sti.hasMoreElements()) {
					destinatario = sti.nextToken();
					nombre = sti.nextToken();
					identificador = sti.nextToken();
					correlativo = sti.nextToken();
					extra = sti.nextToken();
					File carpeta = new File(ruta);
					if (!carpeta.exists()) {
						carpeta.mkdirs();
					}
					String idFileName = (i + 1) + "-campaign-" + campaign.getId() + "-" + destinatario;
					pdfNuevo = new File(carpeta + System.getProperty("file.separator"), idFileName + ".pdf");

				}
				documentoHijo = new Document();
				copia = new PdfCopy(documentoHijo, new FileOutputStream(pdfNuevo));
				pagInicio = obtenerPagina(bookmarksLst.get(i));
				if (i >= bookmarksLst.size() - 1) {
					pagFin = pdfReaderPadre.getNumberOfPages() + 1;
				} else {
					pagFin = obtenerPagina(bookmarksLst.get(i + 1));
				}

				documentoHijo.open();
				for (int j = pagInicio; j < pagFin; j++) {
					page = copia.getImportedPage(pdfReaderPadre, j);
					copia.addPage(page);
				}
				documentoHijo.close();
				encolaDocuments(pdfNuevo, new File(ruta).getName(), campaign.getId(), destinatario, nombre, campaign.getEmailTemplate(),
						extra);
				documentCounts++;
			}
		} catch (Exception e) {
			logger.error(" Error separando pdf. Message : {} ", e.getMessage());
			logger.error(" Error separando pdf. Cause : {} ", e.getCause());
		}
		return documentCounts;
	}

	public static File getPaqueteOrdenImpresionDesdeFTP(String host, String port, String user,
			String pass, String nombreArchivo) throws IOException {
		try {

			String[] cadenaSeparada = nombreArchivo.split("/");
			String archivo = cadenaSeparada[cadenaSeparada.length - 1];
			String dir = nombreArchivo.substring(0, nombreArchivo.length() - archivo.length());
			File archivoBuscado = BinariaArchivo.descargaFTP(host, port, user, pass, dir, archivo);
			return archivoBuscado;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static Integer obtenerPagina(Map bookmark) {
		StringTokenizer sti = new StringTokenizer((String) bookmark.get("Page"), " ");
		return Integer.parseInt(sti.nextToken());
	}

	private static void encolaDocuments(File file, String paqueteName, String campaignId, String direccion,
			String nombreDestinatario, String emailTemplate, String fieldsSearch) {
		List<EmailCategory> categories = new ArrayList<EmailCategory>();
		try {
			fieldsSearch = fieldsSearch.replace("^", "@");
			StringBuilder camposDeBusqueda = new StringBuilder();
			String[] mapValueString = fieldsSearch.split("@");
			for (String val : mapValueString) {
				String[] splitedString = val.split("=");
				String key = splitedString[0].replace("{", "");
				String value = splitedString[1].replace("}", "");
				emailTemplate = emailTemplate.replace(key, value);
				camposDeBusqueda.append(key.replace("[", "").replace("]", "")).append("=").append(value).append(";");
				if (key.toLowerCase().contains("categoria_")) {
					categories.add(new EmailCategory(0, value));
				}
			}
			EmailCampaign ec = new EmailCampaign();
			ec.setCampaignId(campaignId);
			ec.setAddresses(direccion);
			ec.setNames(nombreDestinatario);
			ec.setPackageId(paqueteName);
			ec.setAttachmentPath(file.getCanonicalPath());

			ec.setContentEmail(emailTemplate);
			ec.setFieldsSearch(camposDeBusqueda.toString());
			ec = emailCampaignDao.createEmailCampaing(ec);
			
			logger.info(" Encolado para enviar {}", ec.getAddresses());
			logger.info(" Su adjunto {}", file.getCanonicalPath());
			
			final long emailId = ec.getId();
			categories.forEach( c -> {
				c.setEmailCampaingId(emailId);
			});
			
			if (emailCategoryDao.insertAll(categories))
				logger.info(" Se agregar categoria para {}", ec.getAddresses());
			else
				logger.error(" No se pudo agregar categoria para {}", ec.getAddresses());
			
			

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error creado detalle de envio. Message : {} ", e.getMessage());
			logger.error("Error creado detalle de envio. Cause : {} ", e.getCause());

		}

	}

}
