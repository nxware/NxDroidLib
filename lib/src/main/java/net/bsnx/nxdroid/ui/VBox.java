package net.bsnx.nxdroid.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.function.Consumer;

/**
 * net.bsnx.nxdroid.ui.VBox
 */
public class VBox extends LinearLayout {

    public VBox(Context ctx) {
        super(ctx);
        init();
    }

    public VBox(Context ctx, View...views) {
        this(ctx);
        for (View v: views) {
            addView(v);
        }
    }

    public VBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public View byTag(Object tag) {
        for (int i = 0; i < this.getChildCount(); i++) {
            View v = this.getChildAt(i);
            if (tag.equals(v.getTag())) {
                return v;
            }
        }
        return null;
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
    }

    public VBox addText(String s) {
        TextView txt = new TextView(getContext());
        txt.setText(s);
        addView(txt);
        return this;
    }

    public ImageView createImageView() {
        var img = new ImageView(getContext());
        addView(img);
        return img;
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

    public CheckBox addCheckbox(final String title, final boolean state, final Consumer<Boolean> onChanged) {
        var ctrl = new CheckBox(getContext());
        ctrl.setText(title);
        ctrl.setChecked(state);
        ctrl.setPadding(10,30,10,30);
        ctrl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onChanged.accept(b);
            }
        });
        addView(ctrl);
        return ctrl;
    }

    public Switch addSwitch(final String title, final boolean state, final Consumer<Boolean> onChanged) {
        var ctrl = new Switch(getContext());
        ctrl.setText(title);
        ctrl.setPadding(10,30,10,30);
        ctrl.setChecked(state);
        ctrl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onChanged.accept(b);
            }
        });
        addView(ctrl);
        return ctrl;
    }
}
