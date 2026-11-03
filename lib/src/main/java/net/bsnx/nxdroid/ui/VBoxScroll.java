package net.bsnx.nxdroid.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.function.Consumer;

public class VBoxScroll extends ScrollView {

    private VBox content;

    public VBoxScroll(Context ctx) {
        super(ctx);
        content = new VBox(ctx);
        addView(content);
    }

    @Override
    public void addView(View child) {
        if (this.getChildCount()== 0) {
            super.addView(child);
        } else {
            content.addView(child);
        }
    }

    public VBoxScroll addText(String s) {
        content.addText(s);
        return this;
    }

    public ImageView createImageView() {
        return content.createImageView();
    }

    public TextView createText(String s) {
        return content.createText(s);
    }

    public EditText addEditText(String text) {
        return content.addEditText(text);
    }

    public PrefTextEdit addPrefTextEdit(String ns, String key, String defaultVal) {
        return this.addPrefTextEdit(ns, key, defaultVal);
    }

    public Button addButton(final String text, final Runnable onClick) {
        return content.addButton(text, onClick);
    }

    public CheckBox addCheckbox(final String title, final boolean state, final Consumer<Boolean> onChanged) {
        return content.addCheckbox(title, state, onChanged);
    }

    public Switch addSwitch(final String title, final boolean state, final Consumer<Boolean> onChanged) {
        return content.addSwitch(title, state, onChanged);
    }
}
