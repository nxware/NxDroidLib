package net.bsnx.nxdroid.ui;

import android.content.Context;
import android.view.View;
import android.widget.ScrollView;

public class ScrollViewEx extends ScrollView {

    public ScrollViewEx(Context context, View content) {
        super(context);
        addView(content);
    }
}
