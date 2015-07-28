package ca.ubc.ubyssey.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.Utils;

/**
 * Created by Chris Li on 6/12/2015.
 */
public class AuthorTextView extends TextView {

    private static Typeface mTypeface;

    public AuthorTextView(Context context) {
        super(context);
        init(context);
    }

    public AuthorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AuthorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        int sidePadding = (int) getResources().getDimension(R.dimen.text_padding);
        int bottomPadding = (int) getResources().getDimension(R.dimen.extra_padding);
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/LFT_Etica_Semibold.otf");
        }
        setTypeface(mTypeface);
        setBackgroundColor(Color.WHITE);
        setTextColor(Color.BLACK);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        setAllCaps(true);
        setPadding(sidePadding, bottomPadding, 0, bottomPadding);
        setId(Utils.generateViewId());
    }
}
