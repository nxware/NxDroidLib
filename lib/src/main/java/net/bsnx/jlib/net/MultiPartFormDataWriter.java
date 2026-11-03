package net.bsnx.jlib.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * Schrei in einen Stream fuer einen HTTP Fileupload via POST
 * 
 * @see C# nx.Lib.Net#WriteHTTPUploadFile
 * @author Bjoern R. Salgert
 * @date 10.08.2011
 */
public class MultiPartFormDataWriter {

	/**
	 * Test
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MultiPartFormDataWriter obj = new MultiPartFormDataWriter(System.out, MULTI_PART_BOUNDARY);
		obj.writeHttpUploadFile("V:/bytes.txt", "f");
	}
	
	public static final String MIME = "multipart/form-data";
	public static final String MULTI_PART_BOUNDARY = "7cf2a327f01ae";
	
	/**
	 * 
	 * @param out		Ausgabestrom der an den HTTP-Server geschrickt wird Header wurden schon rein geschrieben
	 * @param boundary	Boundary das im Header angegeben wurde
	 */
	public MultiPartFormDataWriter(OutputStream out, String boundary) {
		this.out = out;
		this.boundary = boundary;
	}
	
	private OutputStream out;
	private String boundary;
	
	public void writeHttpUploadFile(String filePath, String name) {
		try {
			FileInputStream fileStream = new FileInputStream(filePath);
			File file = new File(filePath);
			byte[] fileData = new byte[(int)file.length()];
			fileStream.read(fileData);
			fileStream.close();
			String fileName = file.getName();
			writeHttpUploadFile(fileData, name, fileName);
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim Schreiben einer Datei in den HTTP-UploadStream", e);
		}
	}
	
	public void writeHttpUploadFile(byte[] fileData, String name, String fileName) {
		try {
			String line = "--" + boundary + "\r\n" + "content-disposition: form-data; name=\"" + name + "\"; filename=\"" + fileName + "\"\r\n";
			out.write(line.getBytes());
			out.write("Content-Type: text/plain\r\n\r\n".getBytes());
			out.write(fileData);
			out.write(("\r\n--" + boundary + "--\r\n").getBytes());
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim Schreiben einer Datei in den HTTP-UploadStream", e);
		}
	}
	
	public void writeHttpPostField(String name, String data) {
		try {
			out.write(("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n").getBytes());
			out.write(data.getBytes());
			out.write(("\r\n--" + boundary + "--\r\n").getBytes());
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim Schreiben eines POST-Feldes in den HTTP-UploadStream", e);
		}	
	}

	public void writeHttpUploadFileData(String fileFieldName, String fileName, byte[] data) {
		try {
			String line = "--" + boundary + "\r\n" + "content-disposition: form-data; name=\"" + fileFieldName + "\"; filename=\"" + fileName + "\"\r\n";
			out.write(line.getBytes());
			out.write("Content-Type: text/plain\r\n\r\n".getBytes());
			out.write(data);
			out.write(("\r\n--" + boundary + "--\r\n").getBytes());
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim Schreiben einer Datei in den HTTP-UploadStream", e);
		}
	}

}
