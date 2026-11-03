package net.bsnx.nxdroidlib;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * Zugangsdaten fuer einen NWeb-Account.
 *
 * Gespeichert in den SharedPreferences "nxdroid" unter nweb.user, nweb.url und nweb.pass,
 * also kompatibel zu NxDroid.getPrefString(ctx, "nweb.user", ...) in bestehenden Apps.
 *
 * Bearbeiten und Import aus der App NxDroid: {@link NWebAccountActivity}, starten mit
 * {@link #openSettings(Context)}.
 */
public final class NWebAccount {

    public static final String PREFS = "nxdroid";
    public static final String KEY_USER = "nweb.user";
    public static final String KEY_URL = "nweb.url";
    public static final String KEY_PASS = "nweb.pass";
    public static final String DEFAULT_URL = "https://bsnx.net/4.0/";

    /** App, aus der die Einstellungen importiert werden */
    static final String NXDROID_PACKAGE = "net.bsnx.nxdroid2";
    static final String NXDROID_SETTINGS = "net.bsnx.nxdroid.SettingActivity";

    private NWebAccount() {
    }

    public static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static String getUser(Context ctx) {
        return prefs(ctx).getString(KEY_USER, "");
    }

    /** NWeb-URL, ohne Eintrag {@link #DEFAULT_URL} */
    public static String getUrl(Context ctx) {
        String url = prefs(ctx).getString(KEY_URL, "");
        return url == null || url.trim().isEmpty() ? DEFAULT_URL : url;
    }

    public static String getPass(Context ctx) {
        return prefs(ctx).getString(KEY_PASS, "");
    }

    public static void save(Context ctx, String user, String url, String pass) {
        prefs(ctx).edit()
                .putString(KEY_USER, user)
                .putString(KEY_URL, url)
                .putString(KEY_PASS, pass)
                .apply();
    }

    /** Benutzer und Passwort/Token sind gesetzt */
    public static boolean isConfigured(Context ctx) {
        return !getUser(ctx).trim().isEmpty() && !getPass(ctx).trim().isEmpty();
    }

    /** Kurzbeschreibung fuer Einstellungslisten, z.B. "max @ bsnx.net" */
    public static String summary(Context ctx) {
        if (!isConfigured(ctx)) {
            return "Nicht eingerichtet";
        }
        String host = Uri.parse(getUrl(ctx)).getHost();
        return getUser(ctx) + " @ " + (host != null ? host : getUrl(ctx));
    }

    /** Intent fuer die Seite zum Bearbeiten und Importieren */
    public static Intent openSettings(Context ctx) {
        return new Intent(ctx, NWebAccountActivity.class);
    }

    /**
     * Fragt die Einstellungen bei NxDroid an. NxDroid startet danach die angegebene
     * Antwort-Activity mit den Extras nweb.user, nweb.url und nweb.pass.
     */
    static Intent importRequest(Context ctx) {
        Intent intent = new Intent();
        intent.setClassName(NXDROID_PACKAGE, NXDROID_SETTINGS);
        intent.putExtra("response_class", NWebAccountActivity.class.getName());
        intent.putExtra("response_package", ctx.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /** Enthaelt der Intent eine Antwort von NxDroid? */
    static boolean isImportResponse(Intent intent) {
        return intent != null && intent.hasExtra(KEY_USER);
    }
}
