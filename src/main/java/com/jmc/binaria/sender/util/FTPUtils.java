package com.jmc.binaria.sender.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;

public class FTPUtils {

	private static FTPClient ftp;

	public static void conectar(String hostname, int port, String user, String pass)
			throws SocketException, IOException {
		if (ftp == null)
			ftp = new FTPClient();

		ftp.connect(hostname, port);

		if (ftp.login(user, pass))
			System.out.println("FTP login OK");
		else
			System.out.println("FTP login Error");
	}

	public static File descargarArchivo(String hostFile, String localFile) throws FileNotFoundException, IOException {
		BufferedOutputStream buffOut = new BufferedOutputStream(new FileOutputStream(localFile));
		if (ftp.retrieveFile(hostFile, buffOut))
			System.out.println("FTP Descarga correcta file : " + localFile);
		else
			System.out.println("FTP Error Descarga file : " + hostFile);

		buffOut.close();
		return new File(localFile);
	}

	public static File descargar(String dirHostFile, String hostFile) throws FileNotFoundException, IOException {
		String completeLocalFile ="/tmp/"+hostFile;
		BufferedOutputStream buffOut = new BufferedOutputStream(new FileOutputStream(completeLocalFile));
		if (ftp.retrieveFile(dirHostFile + "/" + hostFile, buffOut))
			System.out.println("FTP Descarga correcta file : " + completeLocalFile);
		else
			System.out.println("FTP Error Descarga file : " + hostFile);

		buffOut.close();

		return new File(completeLocalFile);
	}

	public static void desconectar() {
		try {
			if (ftp != null)
				ftp.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}