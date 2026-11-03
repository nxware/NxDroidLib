package net.bsnx.jlib;

import net.bsnx.i.Callback;
import net.bsnx.jlib.collections.Collections;
import net.bsnx.jlib.collections.KV;
import net.bsnx.jlib.net.MultiPartFormDataWriter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

/**
 * Vereinfachter Umgang mit Netzwerkverbindungen
 * 
 * @test jlib.test.NetUtilsTest
 * @author Bjoern R. Salgert
 * @date 28.09.2010
 */
public class NetUtils {

	// {{{ wget
	
	/**
	 * Liefert den Inhalt der Seite von url zurueck.
	 * 
	 * Wenn die Seite nicht geladen werden kann wird "" zur�ckgegeben (also keine Exception)
	 * 
	 * @category wget
	 * @param url
	 * @return Inhalt der Seite
	 */
	public static String wget(String url) {
		try {
			return wget(new URL(url));
		} catch (MalformedURLException e) {
			System.err.println("wget - Error URL: "+ url);
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * @category wget
	 * @param url
	 * @param errorListener
	 * @return
	 */
	public static String wget(String url, Callback<Exception> errorListener) {
		try {
			URL _url = new URL(url);
			return wgetUnsafe(_url);
		} catch (Exception e) {
			if (errorListener!=null) errorListener.execute(e);
		}
		return "";
	}
	
	/**
	 * @category wget
	 * @param url
	 * @return
	 */
	public static String wget(URL url) {
		StringBuilder sb = new StringBuilder();
        HttpURLConnection urlConnection;
		try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            if ("gzip".equals(urlConnection.getContentEncoding())) {
            	inputStream = new GZIPInputStream(inputStream);
            }
            BufferedInputStream in = new BufferedInputStream(inputStream);
            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
	        String line;
	        while ((line = rd.readLine()) != null) {
	            sb.append(line);
	            sb.append('\n');
	        }
	        rd.close();
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
        	System.err.println("wget - Error");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error: wget io");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("wget - Error");
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * @category wget
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String wgetUnsafe(URL url) throws IOException {
		StringBuilder sb = new StringBuilder();
        HttpURLConnection urlConnection;
        urlConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        if ("gzip".equals(urlConnection.getContentEncoding())) {
        	inputStream = new GZIPInputStream(inputStream);
        }
        BufferedInputStream in = new BufferedInputStream(inputStream);
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
            sb.append('\n');
        }
        rd.close();
        urlConnection.disconnect();
		return sb.toString();
	}
	
	// }}}
	
	// {{{ downloadUrlToFile
	
	public static void downloadUrlToFile(String url, String path) {
		try {
            FileOutputStream fos = new FileOutputStream(path);
            downloadUrlToStream(new URL(url), fos,
            	new ErrorListener() {
					@Override public void trace(String error_msg) {}
				},
				new ProgressListener() {
					@Override public void progress(int done, int size) {}
				}
            );
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void downloadUrlToFile(String url, String path, ErrorListener error_op, ProgressListener progress_op, CancelListener queryCancal_op) {
		try {
            FileOutputStream fos = new FileOutputStream(path);
            downloadUrlToStream(new URL(url), fos, error_op, progress_op, queryCancal_op);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void downloadUrlToStream(URL url, OutputStream os, ErrorListener error_op, ProgressListener progress_op) {
		downloadUrlToStream(url, os, error_op, progress_op, null);
	}
	
	public static void downloadUrlToStream(URL url, OutputStream os, ErrorListener error_op, ProgressListener progress_op, CancelListener queryCancal_op) {
		try {
			if (queryCancal_op == null) {
				queryCancal_op = new CancelListener() {
					@Override public boolean queryCancel() {
						return false;
					}
				};
			}
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.connect();
	
	        int responseCode = conn.getResponseCode();
	        int completeSize = conn.getContentLength(); 
	
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	
	            byte tmp_buffer[] = new byte[4096];
	
	            InputStream is = conn.getInputStream();
	
	            int n;
	            int done = 0;
	
	            while ((n = is.read(tmp_buffer)) > 0) {
	            	done += n;
	                os.write(tmp_buffer, 0, n);
	                os.flush();
	                progress_op.progress(done, completeSize);
	                if (queryCancal_op.queryCancel()) {
	                	break;
	                }
	            }
	
	        } else {
	        	error_op.trace("HTTP response: " + responseCode);
	        } 
		} catch (Exception e) {
			e.printStackTrace();
			error_op.trace(e.getMessage());
		}
	}
	
	public interface ProgressListener {
		public void progress(int done, int size);
	}
	public interface ErrorListener {
		public void trace(String error_msg);
	}
	public interface CancelListener {
		public boolean queryCancel();
	}
	
	public static void downloadUrlToFileEx(String url, String path, ProgressListener progress_op) throws IOException {
		downloadUrlToFileEx(url, path, progress_op, null);
	}
	
	/**
	 * Wie downloadUrlToFile(...) nur das Exceptions geworfen werden
	 * 
	 * @param url
	 * @param path
	 * @param progress_op
	 * @throws IOException 
	 */
	public static void downloadUrlToFileEx(String url, String path, ProgressListener progress_op, CancelListener queryCancal_op) throws IOException {
		URL urlObj = new URL(url);
		FileOutputStream os = new FileOutputStream(path);
		if (progress_op == null) {
			progress_op = new ProgressListener() {
				@Override public void progress(int done, int size) {}
			};
		}
		if (queryCancal_op == null) {
			queryCancal_op = new CancelListener() {
				@Override public boolean queryCancel() {
					return false;
				}
			};
		}
		HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        conn.connect();

        int responseCode = conn.getResponseCode();
        int completeSize = conn.getContentLength(); 

        if (responseCode == HttpURLConnection.HTTP_OK) {
            byte tmp_buffer[] = new byte[4096];
            InputStream is = conn.getInputStream();
            int n;
            int done = 0;
            while ((n = is.read(tmp_buffer)) > 0) {
            	done += n;
                os.write(tmp_buffer, 0, n);
                os.flush();
                progress_op.progress(done, completeSize);
                if (queryCancal_op.queryCancel()) {
                	break;
                }
            }
            os.close();
        } else {
        	os.close();
        	throw new RuntimeException("HTTP response: " + responseCode);
        } 
	}
	
	// }}}
	
	/**
	 * @link http://php.net/urlencode
	 */
	public static String urlencode(String data) {
		try {
			return URLEncoder.encode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Fehler beim URL-Encoding", e);
		}
	}
	
	/**
	 * Liefert den Dateinamen aus einer URL zur�ck
	 * 
	 * @param url	z.B. http://www.bsnx.net/downloads/nxnet.zip
	 * @return		z.B. nxnet.zip
	 */
	public static String getFilenameFromUrl(String url) {
		String[] array = url.split("/");
		return array[array.length-1];
	}
	
	/**
	 * Versendet einen HTTP-Post Request
	 * 
	 * Die Post-Daten aus $postData werden "application/x-www-form-urlencoded" �bertragen 
	 * 
	 * @param url
	 * @param postData
	 * @return	Inhalt der Seite (Antwort vom Webserver)
	 */
	public static String post(String url, Map<String,String> postData) {
		try {
		    List<String> fields = new ArrayList<String>();
		    for (String key : postData.keySet()) {
				fields.add(urlencode(key) + "=" + postData.get(key));
			}
		    String data = Collections.implode(fields, "&");
		    
		    URL _url = new URL(url);
		    URLConnection conn = _url.openConnection();
		    conn.setDoOutput(true);
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(data);
		    wr.flush();

		    InputStream is = conn.getInputStream();
		    String res = Streams.toString(is);
		    wr.close();
		    is.close();
		    return res;
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim versenden des POST-Requestes", e);
		}
	}
	
	/**
	 * 
	 * @param url
	 * @param postData	POST Felder, oder null wenn man keine hat
	 * @param files
	 * @return
	 */
	public static String postUpload(String url, Map<String,String> postData, Map<String,String> files) {
		try {
			if (postData == null) postData = new HashMap<String, String>();
			URL _url = new URL(url);
		    URLConnection conn = _url.openConnection();
		    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + MultiPartFormDataWriter.MULTI_PART_BOUNDARY);
		    conn.setDoOutput(true);
		    OutputStream out = conn.getOutputStream();
		    MultiPartFormDataWriter writer = new MultiPartFormDataWriter(out, MultiPartFormDataWriter.MULTI_PART_BOUNDARY);
		    for (Entry<String,String> entry : postData.entrySet()) {
		    	writer.writeHttpPostField(entry.getKey(), entry.getValue());
			}
		    for (Entry<String,String> entry : files.entrySet()) {
		    	writer.writeHttpUploadFile(entry.getValue(), entry.getKey());
			}
		    out.flush();
		    InputStream is = conn.getInputStream();
		    String res = Streams.toString(is);
		    out.close();
		    is.close();
		    return res;
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim versenden des POST-Upload-Requestes", e);
		}
	}
	
	public static String postUpload(String url, Map<String,String> postData, String filePath, String fileName) {
		return postUpload(url, postData, Collections.buildMap(new KV(fileName, filePath)));
	}
	
	public static String postUpload(String url, Map<String,String> postData, String fileFieldName, String fileName, byte[] data) {
		try {
			if (postData == null) postData = new HashMap<String, String>();
			URL _url = new URL(url);
		    URLConnection conn = _url.openConnection();
		    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + MultiPartFormDataWriter.MULTI_PART_BOUNDARY);
		    conn.setDoOutput(true);
		    OutputStream out = conn.getOutputStream();
		    MultiPartFormDataWriter writer = new MultiPartFormDataWriter(out, MultiPartFormDataWriter.MULTI_PART_BOUNDARY);
		    for (Entry<String,String> entry : postData.entrySet()) {
		    	writer.writeHttpPostField(entry.getKey(), entry.getValue());
			}
		    writer.writeHttpUploadFileData(fileFieldName, fileName, data);
		    out.flush();
		    InputStream is = conn.getInputStream();
		    String res = Streams.toString(is);
		    out.close();
		    is.close();
		    return res;
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim versenden des POST-Upload-Requestes", e);
		}
	}
	
	/**
	 * �berpr�ft ob auf dem angegebenen Port $port ein Tcp-Server l�uft.
	 * 
	 * @param port	TCP-Port Nummer (Zwischen 0 und 65536) 
	 * @return		Ob auf dem Port ein Server lauscht.
	 */
	public static boolean isPortOpen(int port) {
		try {
			Socket socket = new Socket();
			SocketAddress addr = new InetSocketAddress("127.0.0.1", port);
			socket.connect(addr, 500);
			socket.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
