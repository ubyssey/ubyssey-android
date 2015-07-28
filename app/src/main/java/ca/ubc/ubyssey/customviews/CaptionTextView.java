package ca.ubc.ubyssey.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Chris Li on 6/12/2015.
 */
public class CaptionTextView extends TextView {

    private static Typeface mTypeface;

        /*
    android:fontFamily="sans-serif-thin"
    android:gravity="center_vertical"
    android:layout_width="match_parent"
    android:paddingLeft="10dp"
    android:paddingRight="10dp"
    android:paddingTop="15dp"
    android:layout_height="75dp"
    android:textColor="@color/white"*/

    public CaptionTextView(Context context) {
        super(context);
    }

    public CaptionTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CaptionTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {

    }
}
