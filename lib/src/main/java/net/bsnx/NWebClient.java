package net.bsnx;

import android.util.Log;

import net.bsnx.json.JSONArray;
import net.bsnx.json.JSONObject;
import net.bsnx.nxdroid.GeoItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
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

    // ---------------------------------------------------------------- REST-API w/api (siehe api.json)

    /** Basis der REST-API, z.B. https://bsnx.net/4.0/w/api/ */
    public String apiUrl() {
        return (url.endsWith("/") ? url : url + "/") + "w/api/";
    }

    /** URL fuer einen API-Pfad; Anmeldung laeuft laut API ueber die Query-Parameter username/password. */
    private String apiUrl(String path, Map<String, String> query) throws IOException {
        Map<String, String> q = new LinkedHashMap<>();
        if (query != null) {
            q.putAll(query);
        }
        q.put("username", user);
        q.put("password", password);
        StringBuilder sb = new StringBuilder(apiUrl()).append(path);
        char sep = '?';
        for (Map.Entry<String, String> e : q.entrySet()) {
            if (e.getValue() == null) {
                continue;
            }
            sb.append(sep)
                    .append(URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8.name()))
                    .append('=')
                    .append(URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8.name()));
            sep = '&';
        }
        return sb.toString();
    }

    /** Schreibt den Request-Body. */
    private interface BodyWriter {
        void write(OutputStream out) throws IOException;
    }

    /**
     * HTTP-Anfrage an die API. body (optional) wird gestreamt gesendet, damit auch grosse Dateien
     * nicht komplett in den Speicher muessen. Wirft bei Netzwerk- und HTTP-Fehlern eine IOException.
     */
    private String apiRequest(String method, String url, File body, String contentType) throws IOException {
        if (body == null) {
            return apiRequest(method, url, -1, null, null);
        }
        return apiRequest(method, url, body.length(), contentType, out -> {
            try (InputStream in = new FileInputStream(body)) {
                byte[] buf = new byte[64 * 1024];
                int n;
                while ((n = in.read(buf)) > 0) {
                    out.write(buf, 0, n);
                }
            }
        });
    }

    /** Wie oben, mit kleinem Body aus dem Speicher (z.B. Formulardaten). */
    private String apiRequest(String method, String url, byte[] body, String contentType) throws IOException {
        return apiRequest(method, url, body.length, contentType, out -> out.write(body));
    }

    /** @param length Laenge des Bodys in Bytes, -1 ohne Body */
    private String apiRequest(String method, String url, long length, String contentType, BodyWriter body) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try {
            connection.setRequestMethod(method);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.setRequestProperty("Accept", "application/json");
            if (body != null) {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", contentType);
                connection.setFixedLengthStreamingMode(length);
                try (OutputStream out = connection.getOutputStream()) {
                    body.write(out);
                }
            }
            Log.i(TAG, method + " " + maskUrl(url) + (body != null ? " (" + length + " Bytes)" : ""));
            int code = connection.getResponseCode();
            boolean ok = code >= 200 && code < 300;
            String response = readAll(ok ? connection.getInputStream() : connection.getErrorStream());
            Log.i(TAG, "HTTP " + code + ", " + response.length() + " Zeichen");
            logLong("Antwort: ", response);
            if (!ok) {
                String detail = response.length() > 200 ? response.substring(0, 200) + "…" : response;
                if (code == 404) {
                    throw new FileNotFoundException("HTTP 404: " + maskUrl(url));
                }
                throw new IOException("HTTP " + code + (detail.isEmpty() ? "" : ": " + detail));
            }
            return response;
        } finally {
            connection.disconnect();
        }
    }

    /** GET /me: angemeldeter Benutzer mit id, title, name und doc_ids */
    public JSONObject me() throws IOException {
        return new JSONObject(apiRequest("GET", apiUrl("me", null), (File) null, null));
    }

    /**
     * POST /user/{userId}/doc: legt im Benutzerbereich ein Dokument an und laedt die Datei als
     * Inhalt hoch (roher Body mit contentType, kein Multipart).
     *
     * @param userId      ID des Benutzers, z.B. aus {@link #me()}
     * @param kind        z.B. "arscanner" oder "data"; nur dann wird der Body als Dokumentdaten gespeichert
     * @param title       Titel des Dokuments
     * @param metaJson    optional, {"ns": {"name": value}} fuer document__meta
     * @param file        hochzuladende Datei
     * @param contentType z.B. "text/csv"
     * @return Antwort des Servers, bei Erfolg mit document_id, guid, group_id und data_bytes
     */
    public JSONObject userCreateDocument(String userId, String kind, String title, String metaJson,
                                         File file, String contentType) throws IOException {
        Map<String, String> q = new LinkedHashMap<>();
        q.put("kind", kind);
        q.put("title", title);
        q.put("meta", metaJson);
        String url = apiUrl("user/" + URLEncoder.encode(userId, StandardCharsets.UTF_8.name()) + "/doc", q);
        return new JSONObject(apiRequest("POST", url, file, contentType));
    }

    /**
     * POST /user/{userId}/doc mit Formularfeldern: legt ein Dokument mit content als Inhalt an oder
     * aktualisiert es, wenn guid zu einem eigenen bestehenden Dokument gehoert. Fuer alle kinds ausser
     * data/arscanner/compass (dort ist der rohe Body der Inhalt, siehe userCreateDocument).
     *
     * @param guid null = neues Dokument mit neuer GUID
     * @return bei Neuanlage (HTTP 201) op, document_id, guid, group_id; bei Aktualisierung (200) op, data_bytes, meta
     */
    public JSONObject userSaveDocument(String userId, String kind, String title, String content, String guid)
            throws IOException {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("kind", kind);
        form.put("title", title);
        form.put("content", content);
        if (guid != null && !guid.isEmpty()) {
            form.put("guid", guid);
        }
        StringBuilder body = new StringBuilder();
        for (Map.Entry<String, String> e : form.entrySet()) {
            if (body.length() > 0) {
                body.append('&');
            }
            body.append(URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8.name())).append('=')
                    .append(URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8.name()));
        }
        Map<String, String> q = new LinkedHashMap<>();
        q.put("kind", kind);
        String url = apiUrl("user/" + URLEncoder.encode(userId, StandardCharsets.UTF_8.name()) + "/doc", q);
        return new JSONObject(apiRequest("POST", url, body.toString().getBytes(StandardCharsets.UTF_8),
                "application/x-www-form-urlencoded; charset=UTF-8"));
    }

    // ---------------------------------------------------------------- Metriken (/metrics*)

    /**
     * GET /metrics: Metriken des angemeldeten Benutzers. Admins bekommen alle, mit mine=true nur die eigenen.
     *
     * @return Array aus Objekten mit id, name, title, unit, computed, count, last_value, last_time (Unix-Zeit)
     */
    public JSONArray metrics(boolean mine) throws IOException {
        Map<String, String> q = new LinkedHashMap<>();
        if (mine) {
            q.put("mine", "1");
        }
        return new JSONArray(apiRequest("GET", apiUrl("metrics", q), (File) null, null));
    }

    /** GET /metrics ohne Einschraenkung auf eigene Metriken. */
    public JSONArray metrics() throws IOException {
        return metrics(false);
    }

    /**
     * GET /metrics/{metricId}: eine Metrik.
     *
     * @param metricId Dokument-ID der Metrik (kind=metric)
     * @return Objekt mit id, name, title, unit, computed, count, last_value, last_time
     * @throws FileNotFoundException wenn es die Metrik nicht gibt (HTTP 404)
     * @throws IOException           u.a. HTTP 403, wenn sie einem anderen Benutzer gehoert
     */
    public JSONObject metric(int metricId) throws IOException {
        return new JSONObject(apiRequest("GET", apiUrl("metrics/" + metricId, null), (File) null, null));
    }

    /**
     * GET /metrics/{metricId}/values: die neuesten limit Werte im Zeitraum, aufsteigend nach Zeit sortiert.
     *
     * @param from  Unix-Zeit in Sekunden, inklusive; null = ohne Untergrenze
     * @param to    Unix-Zeit in Sekunden, inklusive; null = ohne Obergrenze
     * @param limit max. Anzahl (Server: Standard 1000, hoechstens 10000); null = Standard
     * @return Array aus Objekten mit value und creation (Unix-Zeit)
     */
    public JSONArray metricValues(int metricId, Long from, Long to, Integer limit) throws IOException {
        Map<String, String> q = new LinkedHashMap<>();
        q.put("from", from == null ? null : String.valueOf(from));
        q.put("to", to == null ? null : String.valueOf(to));
        q.put("limit", limit == null ? null : String.valueOf(limit));
        return new JSONArray(apiRequest("GET", apiUrl("metrics/" + metricId + "/values", q), (File) null, null));
    }

    /** GET /metrics/{metricId}/values mit Server-Standardwerten (neueste 1000 Werte). */
    public JSONArray metricValues(int metricId) throws IOException {
        return metricValues(metricId, null, null, null);
    }

    /**
     * POST /metrics/{metricId}/values: fuegt einen Wert hinzu.
     *
     * @param creation Unix-Zeit in Sekunden; null = jetzt (setzt der Server)
     * @return Antwort mit success, metric, value, creation
     * @throws IOException u.a. HTTP 400 (ungueltiger Wert), 403 (nicht Eigentuemer), 409 (per SQL berechnete Metrik)
     */
    public JSONObject addMetricValue(int metricId, double value, Long creation) throws IOException {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("value must be a finite number: " + value);
        }
        StringBuilder form = new StringBuilder("value=")
                .append(URLEncoder.encode(BigDecimal.valueOf(value).toPlainString(), StandardCharsets.UTF_8.name()));
        if (creation != null) {
            form.append("&creation=").append(creation);
        }
        byte[] body = form.toString().getBytes(StandardCharsets.UTF_8);
        return new JSONObject(apiRequest("POST", apiUrl("metrics/" + metricId + "/values", null), body,
                "application/x-www-form-urlencoded; charset=UTF-8"));
    }

    /** POST /metrics/{metricId}/values mit Zeitpunkt jetzt. */
    public JSONObject addMetricValue(int metricId, double value) throws IOException {
        return addMetricValue(metricId, value, null);
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
