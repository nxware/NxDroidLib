package net.bsnx.nxdroid.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CompoundButton;
import android.widget.Switch;

public class PrefBool extends Switch {

    String prefName = "nx";
    String prefKey = "setting";
    boolean defaultValue = false;
    SharedPreferences pref;

    public PrefBool(Context context) {
        super(context);
        init();
    }

    public PrefBool(Context context, String ns, String key) {
        super(context);
        this.prefName = ns;
        this.prefKey = key;
        init();
    }

    private void init() {
        pref = this.getContext().getSharedPreferences(prefName, Context.MODE_PRIVATE);
        setChecked(pref.getBoolean(prefKey, defaultValue));
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveVal();
            }
        });
    }

    private void saveVal() {
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(prefKey, isChecked());
        edit.commit();
    }

}
