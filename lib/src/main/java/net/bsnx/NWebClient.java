package net.bsnx;

import android.util.Log;

import net.bsnx.json.JSONArray;
import net.bsnx.json.JSONObject;
import net.bsnx.nxdroid.GeoItem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

public class NWebClient {

    private final String url;
    private final String user;
    private final String password;

    public NWebClient(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private static final String TAG = "NWebClient";

    /** URL fürs Log – Passwort/Token wird maskiert. */
    public static String maskUrl(String url) {
        return url.replaceAll("(?i)(password=)[^&]*", "$1***");
    }

    /** Logcat schneidet lange Zeilen ab (~4000 Zeichen) → in Stücken loggen. */
    private static void logLong(String prefix, String text) {
        int chunk = 3000;
        if (text.length() <= chunk) {
            Log.i(TAG, prefix + text);
            return;
        }
        for (int i = 0; i < text.length(); i += chunk) {
            Log.i(TAG, prefix + "[" + (i / chunk + 1) + "] " + text.substring(i, Math.min(text.length(), i + chunk)));
        }
    }

    private static String readAll(InputStream in) throws IOException {
        if (in == null) return "";
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    public String get(String url) {
        try {
            URL _url  = new URL(url);
            Log.i(TAG, "GET " + maskUrl(url));
            HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int code = connection.getResponseCode();
            if (code >= 400) {
                String body = readAll(connection.getErrorStream());
                Log.w(TAG, "HTTP " + code + " " + maskUrl(url));
                logLong("Antwort: ", body);
                if (code == 404) throw new FileNotFoundException("HTTP 404: " + maskUrl(url));
                throw new IOException("HTTP " + code);
            }
            String s = readAll(connection.getInputStream());
            Log.i(TAG, "HTTP " + code + ", " + s.length() + " Zeichen");
            logLong("Antwort: ", s);
            return s;
        } catch (IOException e) {
            Log.e(TAG, "GET fehlgeschlagen " + maskUrl(url) + ": " + e);
            throw new RuntimeException(e);
        }
    }

    public static String post(String url, Map<String, Object> params) {
        try {
            return postChecked(url, params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Wie post(), wirft aber bei Netzwerk- und HTTP-Fehlern eine IOException und loggt Request/Antwort. */
    public static String postChecked(String url, Map<String, Object> params) throws IOException {
        {
            StringBuilder formBody = new StringBuilder();
            Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                formBody.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()));
                formBody.append("=");
                formBody.append(URLEncoder.encode(String.valueOf(entry.getValue()), StandardCharsets.UTF_8.name()));
                if (it.hasNext()) {
                    formBody.append("&");
                }
            }

            byte[] postData = formBody.toString().getBytes(StandardCharsets.UTF_8);

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            try {
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                connection.setRequestProperty("Content-Length", String.valueOf(postData.length));
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(postData);
                }

                Log.i(TAG, "POST " + maskUrl(url));
                int responseCode = connection.getResponseCode();
                boolean ok = responseCode >= 200 && responseCode < 300;
                String response = readAll(ok ? connection.getInputStream() : connection.getErrorStream());
                Log.i(TAG, "HTTP " + responseCode + ", " + response.length() + " Zeichen");
                logLong("Antwort: ", response);
                if (!ok) {
                    if (responseCode == 404) throw new FileNotFoundException("HTTP 404: " + maskUrl(url));
                    throw new IOException("HTTP " + responseCode);
                }
                return response;

            } finally {
                connection.disconnect();
            }
        }
    }

    public void createDocument(String name, String group_id,String content, Map<String,Object> params) {
        if (group_id == null) {
            group_id = this.user;
        }
        String url = this.url + "w/group/"+group_id+"/create";
        try {
            URL _url  = new URL(url);
            Log.i("url", ""+url);
            HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("POST");
            if (params==null) {
                params = new LinkedHashMap<>();
            }
            params.put("username", this.user);
            params.put("password", this.password);
            params.put("title", name);
            params.put("name", name);
            params.put("kind", "txt");
            params.put("content", content);
            params.put("response", "json");
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            connection.setDoOutput(true);
            OutputStream outputPost = connection.getOutputStream();
            outputPost.write(postDataBytes);
            outputPost.flush();
            outputPost.close();
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void asyncCreateDocument(final String name, final String group_id, final String content, Map<String,Object> params) {
        new Thread(new Runnable() {
            @Override public void run() {
                createDocument(name, group_id, content, params);
            }
        }).start();
    }

    public GeoItem[] readMarker(String docId) {
        String url = this.url + "w/d/"+docId+"/layerjson?";
        url += "?nweb_userrequire=1&username="+this.user+"&password="+this.password;
        return transformJson(get(url));
    }

    public static GeoItem[] transformJson(String s) {
        JSONArray a = new JSONArray(s);
        List<JSONObject> objs = a.objects();
        GeoItem[] array = new GeoItem[objs.size()];
        int i = 0;
        for (JSONObject obj : objs) {
            // {"id":"1","lat":"51.0","lng":"7.0","document_id":null,"title":"","description":"Beispiel",
            // "user_id":null,"layer_id":"1","marker":"markerDefault"},
            GeoItem item = new GeoItem(obj.getInt("id"),obj.str("title"), obj.str("description") , obj.str("marker"), obj.getDouble("lat"), obj.getDouble("lng"));
            item.document_id = obj.optInt("document_id", 0);
            array[i] = item;
            i++;
        }
        return array;
    }

    /** Legt einen Marker auf dem Server an. Wirft bei Fehlern eine IOException. */
    public String createMarker(String docId, double lat, double lng, String title, String description) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("username", this.user);
        params.put("password", this.password);
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("title", title != null ? title : "");
        params.put("description", description != null ? description : "");
        String url = this.url + "w/d/"+docId+"/addmarker?";
        return postChecked(url, params);
    }
}
