package ca.ubc.ubyssey.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.Utils;

/**
 * Created by Chris Li on 6/12/2015.
 */
public class ParagraphTextView extends TextView {

    private static Typeface mTypeface;

    public ParagraphTextView(Context context) {
        super(context);
        init(context);
    }

    public ParagraphTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ParagraphTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        int sidePadding = (int) getResources().getDimension(R.dimen.text_padding);
        int bottomPadding = (int) getResources().getDimension(R.dimen.extra_padding);
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/DroidSerif-Regular.ttf");
        }
        setTypeface(mTypeface);
        setBackgroundColor(Color.WHITE);
        setTextColor(Color.BLACK);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f, getResources().getDisplayMetrics()), 1.0f);
        setPadding(sidePadding, 0, sidePadding, 0);
        setId(Utils.generateViewId());

        LinearLayout.LayoutParams paragraphLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paragraphLayoutParams.setMargins(0, bottomPadding,0,bottomPadding);
        setLayoutParams(paragraphLayoutParams);
    }
}
