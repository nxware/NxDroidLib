package net.bsnx.nxdroid.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Button;

@SuppressLint("AppCompatCustomView")
public class ButtonEx extends Button {
    public ButtonEx(Context context, String title, final Runnable onClick) {
        super(context);
        setText(title);
        if (onClick != null) {
            setOnClickListener(new OnClickListener() {
                @Override public void onClick(View view) {
                    onClick.run();
                }
            });
        }
    }
}
