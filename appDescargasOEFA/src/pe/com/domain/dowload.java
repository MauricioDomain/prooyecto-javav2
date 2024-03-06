package pe.com.domain;

import java.awt.geom.AffineTransform;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.print.attribute.standard.DocumentName;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import pe.gob.sernanp.alfresco.bean.RptaBean;
import pe.gob.sernanp.alfresco.caller.CallServiceRest;
import pe.gob.sernanp.alfresco.util.Util;
import org.apache.commons.io.FileUtils;
public class dowload {

	private static String host;
	private static String port;
	private static String user;
	private static String password;
	private static String ruta_alfresco;
	private static String ruta_descarga;

	private static String ruta_excel;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String doc = "";
		String ver = "", anios = "", exp = "";
		Properties propiedad = new Properties();
		try {

			propiedad.load(new FileReader("descarga.properties"));

			host = propiedad.getProperty("HOST");
			port = propiedad.getProperty("PORT");
			user = propiedad.getProperty("USER");
			password = propiedad.getProperty("PASSWORD");
			ruta_descarga = propiedad.getProperty("RUTA_DESCARGA");
			ruta_excel = propiedad.getProperty("RUTA_EXCEL");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Ejecucion de la descarga");
		FileWriter fil = null;
		//OutputStream fos = null;
		File obj = null, obj2 = null, obj3 = null;
		try {

			File excel = new File(ruta_excel);
			fil = new FileWriter(ruta_excel.replace(".xlsx", ".txt"));
			if (excel.exists()) {
				FileInputStream fis = new FileInputStream(excel);
				XSSFWorkbook wb = new XSSFWorkbook(fis);
				XSSFSheet sheet = wb.getSheetAt(0);
				Iterator<Row> rowIterator = sheet.iterator();
				rowIterator.next();

				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();

					try {
						DataFormatter formatter = new DataFormatter();
						String order = formatter.formatCellValue(row.getCell(0));

						if (!order.isEmpty()) {

							String uuid = formatter.formatCellValue(row.getCell(0));

							RptaBean rptaBean = descargarArchivo(uuid);

							if (rptaBean != null) {
								System.out.println("CODIGO: " + rptaBean.getCode());
								System.out.println("MENSAJE: " + rptaBean.getMessage());
								if (rptaBean.getCode().equals("00000")) {

									//    File verd=new File(ruta_descarga + "/" + rptaBean.getFileName());
									//	fos = new FileOutputStream(ruta_descarga + "/" + rptaBean.getFileName());
									
										File file=new File(ruta_descarga + "/" + rptaBean.getFileName());										
										FileUtils.copyInputStreamToFile(rptaBean.getContentv2(), file);
									
									
									//	ByteArrayOutputStream os = new ByteArrayOutputStream();
									//	fos.write(rptaBean.getContentv2().readAllBytes());
									
									//	FileUtils.writeByteArrayToFile(verd, rptaBean.getContentv2().readAllBytes());
									/*	int read;
										long tam=5368709120L;
							            byte[] data = new byte[(int)tam];
							            while ((read = rptaBean.getContentv2().read(data)) != -1) {
							            	fos.write(data, 0, read);
							            	
							            }*/
							          //  FileUtils.writeByteArrayToFile(verd, data);
									//	fos.write(rptaBean.getContentv2());
										
										
									/*
									 * int bytesRead = -1; long totalBytesRead = 0; while ((bytesRead =
									 * input.read(buffer)) != -1) { fos.write(buffer, 0, bytesRead); totalBytesRead
									 * += bytesRead; if (totalBytesRead == rptaBean.getContent().length) { break; }
									 * }
									 */

									System.out.println("Documento Descargado -->" + "  Nombre Documento: "
											+ rptaBean.getFileName());
									
									fil.write(uuid + "  |  UUID:  " + rptaBean.getFileName() + "\n");
									System.out.println("\n");
									//fos.flush();
									//fos.close();
									gc();
									

								} else {
									System.out.println("ERROR" + rptaBean.getException());
								}

							} else {

								System.out.println("No hay archivo");
							}
							fil.flush();
						
						}

					} catch (Exception e) {
						System.err.println("Error al obtener el documento");
						e.printStackTrace();
					}
				}
				wb.close();
				fil.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static void gc() {
		Runtime garbage = Runtime.getRuntime();
		garbage.gc();
	}

	static RptaBean descargarArchivo(String uuid) {

		// CallServiceRest servicio=new CallServiceRest();
		// String rutaArchivoCarga=ruta_carga+"/"+documento;
		// String rutaAlfresco = ruta_alfresco;
		// String tipodoc = "esp:especificacion";
		// File file;
		try {

			// file = new File(rutaArchivoCarga);
			/// if (!uuid.equals("")) {

			return CallServiceRest.ServiceDownload(host, port, user, password, uuid);
			// } else {
			// System.out.println("No hay nada");
			// return null;

			// }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
