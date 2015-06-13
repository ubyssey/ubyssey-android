package ca.ubc.ubyssey.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.models.Data;
import ca.ubc.ubyssey.network.RequestBuilder;
import de.greenrobot.event.EventBus;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Chris Li on 4/30/2015.
 */
public class ImageActivity extends ActionBarActivity {

    public static final String URL_KEY = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Data imageData = EventBus.getDefault().removeStickyEvent(Data.class);
        Picasso.with(this).load(RequestBuilder.URL_PREFIX + imageData.url).fit().centerCrop().into(imageView);

        PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
        attacher.setZoomable(true);

    }
}
