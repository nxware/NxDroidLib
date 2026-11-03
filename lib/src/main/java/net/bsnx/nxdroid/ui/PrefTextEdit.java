package net.bsnx.nxdroid.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class PrefTextEdit extends EditText {

    String prefName = "nx";
    String prefKey = "setting";
    String defaultValue = "";
    SharedPreferences pref;

    public PrefTextEdit(Context context) {
        super(context);
        init();
    }

    public PrefTextEdit(Context context, String ns, String key, String defVal) {
        super(context);
        this.prefName = ns;
        this.prefKey = key;
        this.defaultValue = defVal;
        init();
    }

    public PrefTextEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        pref = this.getContext().getSharedPreferences(prefName, Context.MODE_PRIVATE);
        setText(pref.getString(prefKey, defaultValue));
        addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override public void afterTextChanged(Editable editable) {
                saveText();
            }
        });
    }

    private void saveText() {
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(prefKey, getText().toString());
        edit.commit();
    }
}
