package net.bsnx.nxdroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.core.content.FileProvider;
import androidx.core.util.Consumer;

import net.bsnx.NWebClient;
import net.bsnx.json.JSONObject;
import net.bsnx.nxdroidlib.NWebAccount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NxDroid {

    public static final String NS = "nxdroid";

    public static final String PREF_MEMO = "memo";

    private static int threadCount = 0;

    public static SharedPreferences getPreference(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(NS, Context.MODE_PRIVATE);
        return prefs;
    }

    public static boolean getPrefBool(Context ctx, String name, boolean def) {
        return getPreference(ctx).getBoolean(name, def);
    }

    public static void setPrefBool(Context ctx, String name, boolean val) {
        SharedPreferences.Editor edit = getPreference(ctx).edit();
        edit.putBoolean(name, val);
        edit.commit();
    }

    public static String getPrefString(Context ctx, String name, String def) {
        return getPreference(ctx).getString(name, def);
    }

    public static void setPrefString(Context ctx, String name, String val) {
        SharedPreferences.Editor edit = getPreference(ctx).edit();
        edit.putString(name, val);
        edit.commit();
    }

    public static String getNxName(Context ctx) {
        return getPrefString(ctx, "nxdroid", "nxdroid");
    }

    public static Thread wget(final Context ctx, final String url) {
        Thread t = new Thread(new Runnable() {
            @Override public void run() {
                try {
                    threadCount++;
                    URL _url = new URL(url);
                    Log.i("url", ""+url);
                    HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
                    connection.setRequestMethod("GET");
                    //connection.setDoOutput(true);
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String content = "", line;
                    while ((line = rd.readLine()) != null) {
                        content += line + "\n";
                    }

                } catch (Exception e) {
                    Log.e("url", ""+e.getMessage(), e);
                    //return "Error";
                } finally {
                    threadCount--;
                }
            }
        });
        t.start();
        return t;
    }

    public static Thread wget(final Context ctx, final String url, final Consumer<String> onSuccess) {
        Thread t = new Thread(new Runnable() {
            @Override public void run() {
                try {
                    threadCount++;
                    URL _url = new URL(url);
                    Log.i("url", ""+url);
                    HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
                    connection.setRequestMethod("GET");
                    //connection.setDoOutput(true);
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String content = "", line;
                    while ((line = rd.readLine()) != null) {
                        content += line + "\n";
                    }
                    final String c = content;
                    if (ctx instanceof Activity && onSuccess != null) {
                        ((Activity)ctx).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onSuccess.accept(c);
                            }
                        });
                    }
                    //return content;
                } catch (Exception e) {
                    Log.e("url", ""+e.getMessage(), e);
                    //return "Error";
                } finally {
                    threadCount--;
                }
            }
        });
        t.start();
        return t;
    }

    public static Thread wget(final Activity ctx, final String url, final String filePath) {
        Thread t = new Thread(new Runnable() {
            @Override public void run() {
                try {
                    threadCount++;
                    URL _url = new URL(url);
                    Log.i("url", ""+url);
                    HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();
                    InputStream is = connection.getInputStream();
                    FileOutputStream os = new FileOutputStream(filePath);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                } catch (Exception e) {
                    Log.e("url", ""+e.getMessage(), e);
                    //return "Error";
                } finally {
                    threadCount--;
                }
            }
        });
        t.start();
        return t;
    }

    public static int getThreadCount() {
        return threadCount;
    }

    public static void openUrl(Context ctx, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        ctx.startActivity(browserIntent);
    }

    public static View.OnClickListener openUrlAction(final Context ctx, final String url) {
        return new View.OnClickListener() {
            @Override public void onClick(View view) {
                openUrl(ctx, url);
            }
        };

    }

    public static View.OnClickListener startActivityAction(final Context ctx, final Class cls) {
        return new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(ctx, cls);
                ctx.startActivity(intent);
            }
        };
    }

    public static void shareFile(Context ctx, String path) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("application/data");
        Uri uri = FileProvider.getUriForFile(ctx, "net.bsnx.nxdroid2.provider", new File(path));
        share.putExtra(Intent.EXTRA_STREAM, uri);
        ctx.startActivity(Intent.createChooser(share, "Share Data"));

    }

    public static void shareImage(Context ctx, String path) {
       // Bitmap icon = BitmapFactory.decodeFile(path);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        //MediaStore.Images.Media.DESCRIPTION

        //ContentValues values = new ContentValues();
        //values.put(MediaStore.Images.Media.TITLE, "title");
        //values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        //Uri uri = ctx.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


//        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(ctx.getContentResolver(), icon,"title", null));
        Uri uri = FileProvider.getUriForFile(ctx, "net.bsnx.nxdroid2.provider", new File(path));
        // Uri.parse("file://"+path)

/*
        OutputStream outstream;
        try {
            outstream = ctx.getContentResolver().openOutputStream(uri);
            icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
*/
        share.putExtra(Intent.EXTRA_STREAM, uri);
        ctx.startActivity(Intent.createChooser(share, "Share Image"));
    }

    /**
     * Sendet über Port 4242
     *
     * @param message
     */
    public static void sendUdp(final String message) {
        Thread thread = new Thread(new Runnable() {
            @Override public void run() {
                DatagramSocket ds = null;
                try {
                    ds = new DatagramSocket();
                    InetAddress serverAddr = InetAddress.getByName("0.0.0.0");
                    DatagramPacket dp;
                    dp = new DatagramPacket(message.getBytes(), message.length(), serverAddr, 4242);
                    ds.send(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (ds != null) {
                        ds.close();
                    }
                }
            }
        });
        thread.start();
    }

    public static void showMap(Context ctx, String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri geoLocation = Uri.parse(uri);
        intent.setData(geoLocation);
        //if (intent.resolveActivity(getPackageManager()) != null) {
            ctx.startActivity(intent);
        //}
    }

    public static String currentTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static int time() {
        return (int)new Date().getTime()/1000;
    }

    public static NWebClient client(Context ctx) {
        // Zugangsdaten aus NxDroidLib; leere URL ergibt die Standard-URL
        return new NWebClient(NWebAccount.getUrl(ctx), NWebAccount.getUser(ctx), NWebAccount.getPass(ctx));
    }

    /**
     * Legt mit dem gespeicherten NWeb-Konto ein Dokument an (POST /user/{userId}/doc) und laedt
     * die Datei als Inhalt hoch. Blockiert bis zur Antwort, daher nicht im UI-Thread aufrufen.
     *
     * @param userId      ID des Benutzers; null = ueber GET /me ermitteln
     * @param kind        z.B. "arscanner" oder "data"
     * @param title       Titel des Dokuments
     * @param metaJson    optional, {"ns": {"name": value}}
     * @param file        hochzuladende Datei
     * @param contentType z.B. "text/csv"
     * @return Antwort des Servers mit document_id, guid, group_id, data_bytes
     * @throws IOException ohne eingerichtetes Konto, bei Netzwerk- oder HTTP-Fehlern
     */
    public static JSONObject userCreateDocument(Context ctx, String userId, String kind, String title,
                                                String metaJson, File file, String contentType) throws IOException {
        if (!NWebAccount.isConfigured(ctx)) {
            throw new IOException("NWeb-Konto ist nicht eingerichtet");
        }
        NWebClient client = client(ctx);
        if (userId == null || userId.isEmpty()) {
            userId = client.me().optString("id", "");
            if (userId.isEmpty()) {
                throw new IOException("Benutzer-ID konnte nicht ermittelt werden (/me)");
            }
        }
        return client.userCreateDocument(userId, kind, title, metaJson, file, contentType);
    }

    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress() {
        boolean useIPv4 = true;
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "127.0.0.8";
    }

    public static List<ActivityInfo> getAllActivities(Context context) {
        List<ActivityInfo> activities = new ArrayList<>();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_ACTIVITIES
            );
            if (packageInfo.activities != null) {
                for (ActivityInfo activityInfo : packageInfo.activities) {
                    activities.add(activityInfo);
                    Log.d("Activities", "Activity: " + activityInfo.name);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return activities;
    }

}
