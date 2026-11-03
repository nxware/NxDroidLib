package net.bsnx.nxdroidlib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Seite zum Bearbeiten des NWeb-Accounts (Benutzer, URL, Passwort/Token) und zum Import
 * der Einstellungen aus der App NxDroid.
 *
 * Nur Framework-Klassen, damit die Seite in Apps mit und ohne AndroidX laeuft.
 * Die Oberflaeche wird im Code aufgebaut, die Bibliothek braucht dadurch keine Layouts.
 */
public class NWebAccountActivity extends Activity {

    private EditText editUser;
    private EditText editUrl;
    private EditText editPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Ab Android 10 hell/dunkel nach Systemeinstellung. Im Code statt als values-v29-Ressource,
        // weil Apps mit compileSdk < 29 (arscanner) das Theme sonst nicht linken koennen.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setTheme(android.R.style.Theme_DeviceDefault_DayNight);
        }
        super.onCreate(savedInstanceState);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        buildUi();
        if (savedInstanceState == null) {
            loadFields();
        }
        handleImport(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleImport(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int dp(int v) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, getResources().getDisplayMetrics());
    }

    private void buildUi() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(20), dp(16), dp(20), dp(16));

        editUser = addField(box, "Benutzername", "z. B. max_mustermann", InputType.TYPE_CLASS_TEXT);
        editUrl = addField(box, "NWeb URL", NWebAccount.DEFAULT_URL,
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        editPass = addField(box, "Passwort/Token", "Passwort oder Token",
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        CheckBox show = new CheckBox(this);
        show.setText("Passwort anzeigen");
        show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton b, boolean checked) {
                int sel = editPass.getSelectionEnd();
                editPass.setInputType(InputType.TYPE_CLASS_TEXT | (checked
                        ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        : InputType.TYPE_TEXT_VARIATION_PASSWORD));
                editPass.setSelection(Math.max(0, sel));
            }
        });
        box.addView(show);

        Button save = new Button(this);
        save.setText("Speichern");
        save.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                saveFields();
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(16);
        box.addView(save, lp);

        Button imp = new Button(this);
        imp.setText("Von NxDroid importieren");
        imp.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                requestImport();
            }
        });
        box.addView(imp, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView hint = new TextView(this);
        hint.setText("Beim Import öffnet sich NxDroid und schickt die dort gespeicherten "
                + "Zugangsdaten zurück. Übernommen werden sie erst nach deiner Bestätigung.");
        hint.setPadding(0, dp(12), 0, 0);
        hint.setAlpha(0.7f);
        box.addView(hint);

        ScrollView scroll = new ScrollView(this);
        scroll.addView(box);
        setContentView(scroll);
    }

    private EditText addField(LinearLayout box, String label, String hint, int inputType) {
        TextView lbl = new TextView(this);
        lbl.setText(label);
        lbl.setPadding(0, dp(12), 0, 0);
        box.addView(lbl);
        EditText e = new EditText(this);
        e.setSingleLine(true);
        e.setInputType(inputType);
        e.setHint(hint);
        box.addView(e);
        return e;
    }

    private void loadFields() {
        editUser.setText(NWebAccount.getUser(this));
        String url = NWebAccount.prefs(this).getString(NWebAccount.KEY_URL, "");
        editUrl.setText(url);
        editPass.setText(NWebAccount.getPass(this));
    }

    private void saveFields() {
        NWebAccount.save(this,
                editUser.getText().toString().trim(),
                editUrl.getText().toString().trim(),
                editPass.getText().toString().trim());
        Toast.makeText(this, "Gespeichert", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void requestImport() {
        try {
            startActivity(NWebAccount.importRequest(this));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "NxDroid ist nicht installiert", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Antwort von NxDroid. Die Activity ist exportiert, daher kann jede App sie mit solchen
     * Extras starten; die Daten werden deshalb nur nach Bestaetigung uebernommen.
     */
    private void handleImport(Intent intent) {
        if (!NWebAccount.isImportResponse(intent)) {
            return;
        }
        final String user = nonNull(intent.getStringExtra(NWebAccount.KEY_USER));
        final String url = nonNull(intent.getStringExtra(NWebAccount.KEY_URL));
        final String pass = nonNull(intent.getStringExtra(NWebAccount.KEY_PASS));
        // Nicht erneut nachfragen, z.B. nach dem Drehen des Bildschirms
        intent.removeExtra(NWebAccount.KEY_USER);

        new AlertDialog.Builder(this)
                .setTitle("Einstellungen übernehmen?")
                .setMessage("Benutzer: " + user + "\nURL: " + (url.isEmpty() ? NWebAccount.DEFAULT_URL : url)
                        + "\nPasswort/Token: " + (pass.isEmpty() ? "leer" : "wird übernommen"))
                .setPositiveButton("Übernehmen", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        NWebAccount.save(NWebAccountActivity.this, user, url, pass);
                        loadFields();
                        Toast.makeText(NWebAccountActivity.this, "Einstellungen übernommen",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Abbrechen", null)
                .show();
    }

    private static String nonNull(String s) {
        return s == null ? "" : s.trim();
    }
}
