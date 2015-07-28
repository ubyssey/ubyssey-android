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
public class TitleTextView extends TextView {

    private static Typeface mTypeface;


    public TitleTextView(Context context) {
        super(context);
        init(context);
    }

    public TitleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {

        int sidePadding = (int) getResources().getDimension(R.dimen.text_padding);
        int bottomPadding = (int) getResources().getDimension(R.dimen.extra_padding);
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/LFT_Etica_Bold.otf");
        }
        setTypeface(mTypeface);
        setBackgroundColor(Color.WHITE);
        setTextColor(getResources().getColor(R.color.dark_gray));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        setPadding(sidePadding, sidePadding, sidePadding, bottomPadding);
        setId(Utils.generateViewId());
    }
}
