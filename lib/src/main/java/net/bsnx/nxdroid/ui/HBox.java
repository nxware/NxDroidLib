package net.bsnx.nxdroid.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * net.bsnx.nxdroid.ui.VBox
 */
public class HBox extends LinearLayout {

    public HBox(Context ctx) {
        super(ctx);
        init();
    }

    public HBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public HBox addText(String s) {
        TextView txt = new TextView(getContext());
        txt.setText(s);
        addView(txt);
        return this;
    }

    public TextView createText(String s) {
        TextView txt = new TextView(getContext());
        txt.setText(s);
        addView(txt);
        return txt;
    }

    public EditText addEditText(String text) {
        EditText edit = new EditText(getContext());
        edit.setText(text);
        addView(edit);
        return edit;
    }

    public PrefTextEdit addPrefTextEdit(String ns, String key, String defaultVal) {
        PrefTextEdit  v = new PrefTextEdit(getContext(), ns, key, defaultVal);
        addView(v);
        return v;
    }

    public Button addButton(final String text, final Runnable onClick) {
        Button btn = new Button(getContext());
        btn.setText(text);
        btn.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                onClick.run();
            }
        });
        addView(btn);
        return btn;
    }
}
