package net.bsnx.nxdroid.ui;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PrefsEdit extends LinearLayout {

    private String ns = "nx";

    public PrefsEdit(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        setPadding(10,10,10,10);
        init();
    }

    public void setPrefControl(String name, String value) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (name.equals(v.getTag())) {
                if (v instanceof PrefTextEdit) {
                    ((PrefTextEdit)v).setText(value);
                } else if (v instanceof PrefTextEdit) {
                    ((PrefTextEdit)v).setText(value);
                }
            }
        }
    }

    private void init() {

    }

    public PrefsEdit ns(String ns) {
        this.ns = ns;
        return this;
    }

    public PrefsEdit addString(String title, String name) {
        return addString(title, name, "");
    }

    public PrefsEdit addString(String title, String name, String defaultValue) {
        PrefTextEdit edit = new PrefTextEdit(getContext(), ns, name, defaultValue);
        edit.setTag(name);
        TextView lbl = new TextView(getContext());
        lbl.setText(title);
        addView(lbl);
        addView(edit);
        return this;
    }

    public PrefsEdit addPassword(String title, String name) {
        PrefPassword edit = new PrefPassword(getContext(), ns, name);
        edit.setTag(name);
        TextView lbl = new TextView(getContext());
        lbl.setText(title);
        addView(lbl);
        addView(edit);
        return this;
    }

    public PrefsEdit addBool(String title, String name) {
        PrefBool edit = new PrefBool(getContext(), ns, name);
        TextView lbl = new TextView(getContext());
        lbl.setText(title);
        addView(lbl);
        addView(edit);
        return this;
    }

}
