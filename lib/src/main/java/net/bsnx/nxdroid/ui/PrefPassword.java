package net.bsnx.nxdroid.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class PrefPassword extends EditText {

    String prefName = "nx";
    String prefKey = "setting";
    String defaultValue = "";
    SharedPreferences pref;

    public PrefPassword(Context context) {
        super(context);
        init();
    }

    public PrefPassword(Context context, String ns, String key) {
        super(context);
        this.prefName = ns;
        this.prefKey = key;
        init();
    }

    public PrefPassword(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
