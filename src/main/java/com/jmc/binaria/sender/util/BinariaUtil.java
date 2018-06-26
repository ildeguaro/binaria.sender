package com.jmc.binaria.sender.util;


import com.jmc.binaria.sender.db.FileCampaignDao;
import com.jmc.binaria.sender.db.FileCampaignDaoImpl;
import com.jmc.binaria.sender.model.Campaign;
import com.jmc.binaria.sender.model.FileCampaign;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinariaUtil {
	
	static Logger logger = LoggerFactory.getLogger(BinariaUtil.class);
	
	private static FileCampaignDao fileCampaignDao = new FileCampaignDaoImpl();

	private BinariaUtil() {
		// Nothing to do
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static void separarDocumentos(File archivoPadre, String ruta, Campaign campaign) {

		String destinatario = "";
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
				String idFileName;
				FileCampaign fileCampaign = new FileCampaign();
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
					idFileName =  i + "-campiang-"+campaign.getId()+ "-" + destinatario ;
					pdfNuevo = new File(carpeta+System.getProperty("file.separator"),idFileName+ ".pdf");
					fileCampaign.setCampaignId(campaign.getId());
					fileCampaign.setFileId(idFileName);
					fileCampaign.setFilePath(pdfNuevo.getAbsolutePath());
//					pdfNuevo = new File(carpeta, System.getProperty("file.separator") + i + "|" + destinatario + "|"
//							+ nombre.replace("/", "¬") + "|" + correlativo + "|" + extra + ".pdf");

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
				fileCampaignDao.save(fileCampaign);
				logger.info(" Separado Documento : {} ", fileCampaign.getFilePath());
			}
		} catch (Exception e) {
			logger.error(" Error separando pdf. Message : {} ", e.getMessage());
			logger.error(" Error separando pdf. Cause  : {} ", e.getCause());			
		}
	}

	public static File getPaqueteOrdenImpresionDesdeFTP(String nombreArchivo) throws IOException {
		try {

			String[] cadenaSeparada = nombreArchivo.split("/");
			String archivo = cadenaSeparada[cadenaSeparada.length - 1];
			String dir = nombreArchivo.substring(0, nombreArchivo.length() - archivo.length());
			
			File archivoBuscado = FTPUtils.descargar(dir, archivo);
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

}
