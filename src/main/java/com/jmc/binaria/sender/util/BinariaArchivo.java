package com.jmc.binaria.sender.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 *
 * @author William Basabe
 */
public class BinariaArchivo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BinariaArchivo.class);
    /**
     * Constructo privado ara que la clase no pueda ser instanciada.
     */
    public BinariaArchivo() { }

    /**
     * Tamaño del Buffer por defecto.
     */
    private final int NUM = 512;
    
    /**
     *Propiedad opcional para la ruta de los archivos temporales generados 
     *por esta clase
     */
    private String ubicacionArchivos = null;
    /**
     * Método que convierte un archivo en su representación en un arreglo de
     * byte.
     * @param archivo El archivo a ser convertido.
     * @return Retornaun arrglo de byte que representa al archivo.
     */
    public byte[] convertirFileADataBinaria(final File archivo) {
        byte[] dataBinaria = null;
        FileInputStream streamArchivo = null;

        try {
            streamArchivo = new FileInputStream(archivo);
            dataBinaria = new byte[(int) archivo.length()];
            streamArchivo.read(dataBinaria);
            streamArchivo.close();
        } catch (IOException ex) {
            log.error("error",ex);
            
        } finally {
            if (streamArchivo != null) {
                try {
                    streamArchivo.close();
                } catch (IOException ex) {
                    log.error("error",ex);
                }
            }
        }
        return dataBinaria;
    }

    /**
     * Convierte un InputStream en un archivo con la extesión pasada por
     * parámetros. Se creará un archivo temporal.
     * @param fuente InputStream fuente para la generación del archivo.
     * @param extension la extessión con la cual se generará el archivo.
     * @param identificador Esta cadena se incluirá en el nombre del archivo
     * para identificar el mismo en caso de ser necesario.
     * @return Retona un objeto File contentivo con el archivo
     */
    public  File archivoDesdeStream(final InputStream fuente,
            final String extension, final String identificador)
            throws Exception {
        byte[] buf = new byte[NUM];
        int len;
        File archivo = null;
        if (fuente == null) {
            throw new Exception("error.sis.fuente_invalida");
        }
        try {
            /**
             * Angel Zambrano
             * ubicar los archivos temporales en un directorio diferente al del sistema.
             */
            if (ubicacionArchivos == null) {
                archivo = File.createTempFile("archivo_" + identificador + "_", "." + extension);
            } else {
                File auxTemp = new File(ubicacionArchivos);
                if (!auxTemp.exists()) {
                    auxTemp.mkdirs();
                }
                archivo = new File(auxTemp +File.separator+ "archivo_" + identificador + "_"+System.currentTimeMillis()+"." + extension);
            }
            archivo.deleteOnExit();
            OutputStream out = new FileOutputStream(archivo);
            while ((len = fuente.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
        } catch (IOException ex) {
            throw new Exception("error.sis.fuente_erronea");
        }
        return archivo;
    }


        
}
