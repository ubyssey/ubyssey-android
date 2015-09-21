package ca.ubc.ubyssey.main;

import android.app.Activity;
import android.os.Bundle;


import com.squareup.picasso.Picasso;

import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.customviews.TouchImageView;
import ca.ubc.ubyssey.models.Data;
import de.greenrobot.event.EventBus;

/**
 * Created by Chris Li on 4/30/2015.
 */
public class ImageActivity extends Activity {

    public static final String URL_KEY = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        TouchImageView imageView = (TouchImageView) findViewById(R.id.imageView);

        Data imageData = EventBus.getDefault().removeStickyEvent(Data.class);
        Picasso.with(this).load(imageData.url).into(imageView);
    }
}
