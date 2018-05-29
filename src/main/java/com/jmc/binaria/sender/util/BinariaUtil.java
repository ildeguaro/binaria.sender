package com.jmc.binaria.sender.util;

import com.jmc.core.exception.GeneralException;
import com.jmc.core.util.archivo.BinariaArchivo;
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

public class BinariaUtil {

	private BinariaUtil() {
		// Nothing to do
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static void separarDocumentos(File archivoPadre, String ruta) {

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
			for (int i = 0; i < bookmarksLst.size(); i++) {
				sti = new StringTokenizer(bookmarksLst.get(i).get("Title").toString(), "|");
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
					pdfNuevo = new File(carpeta, System.getProperty("file.separator") + i + "|" + destinatario + "|"
							+ nombre.replace("/", "�") + "|" + correlativo + "|" + extra + ".pdf");

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
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ERROR --- GRABAR EN LOGS");
		}
	}

	public static File getPaqueteOrdenImpresionDesdeFTP(String ftpHost, String ftpPor, String ftpUser, String ftpPsw,
			String nombreArchivo) throws IOException {
		try {

			String[] cadenaSeparada = nombreArchivo.split("/");
			String archivo = cadenaSeparada[cadenaSeparada.length - 1];
			String dir = nombreArchivo.substring(0, nombreArchivo.length() - archivo.length());

			File archivoBuscado = new BinariaArchivo().descargaFTP(ftpHost, ftpPor, ftpUser, ftpPsw, dir, archivo);
			return archivoBuscado;
		} catch (GeneralException e) {
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
