package ca.ubc.ubyssey.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.customviews.TouchImageView;
import ca.ubc.ubyssey.models.Data;
import ca.ubc.ubyssey.models.Galleries;
import ca.ubc.ubyssey.network.RequestBuilder;
import de.greenrobot.event.EventBus;

/**
 * Activity to either show a view pager of images from a gallery
 * or an individual image from an article
 *
 * Created by Chris Li on 4/30/2015.
 */
public class ImageActivity extends ActionBarActivity {

    public static final String URL_KEY = "url";

    public static final String IS_PAGER_KEY = "is_pager_key";
    public static final String GALLERY_ITEM_POSITION_KEY = "position_key";
    public static final String TOOLBAR_TITLE_KEY = "title_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TouchImageView imageView = (TouchImageView) findViewById(R.id.imageView);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        String toolbarTitle = getIntent().getStringExtra(TOOLBAR_TITLE_KEY);
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(toolbarTitle);

        boolean isPagerEnabled = getIntent().getBooleanExtra(IS_PAGER_KEY, false);

        if (isPagerEnabled) {
            imageView.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            Galleries.Image[] galleryImages = EventBus.getDefault().removeStickyEvent(Galleries.Image[].class);

            ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), galleryImages);
            viewPager.setAdapter(imagePagerAdapter);

            int selectedPosition = getIntent().getIntExtra(GALLERY_ITEM_POSITION_KEY, 0);
            viewPager.setCurrentItem(selectedPosition);
        } else {
            imageView.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            Data imageData = EventBus.getDefault().removeStickyEvent(Data.class);
            Picasso.with(this).load(RequestBuilder.ROOT_URL + imageData.url).into(imageView);
        }
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        private Galleries.Image[] mGalleryImages;

        public ImagePagerAdapter(FragmentManager fm, Galleries.Image[] galleryImages) {
            super(fm);
            this.mGalleryImages = galleryImages;
        }



        @Override
        public Fragment getItem(int position) {

            Galleries.Image image = mGalleryImages[position];
            return ImageFragment.newInstance(RequestBuilder.ROOT_URL + image.url);
        }

        @Override
        public int getCount() {
            return mGalleryImages.length;
        }
    }

    public static class ImageFragment extends Fragment {

        public static final String IMG_URL_KEY = "image_url";

        public static ImageFragment newInstance(String imgUrl) {

            ImageFragment imageFragment = new ImageFragment();
            Bundle bundle = new Bundle();
            bundle.putString(IMG_URL_KEY, imgUrl);
            imageFragment.setArguments(bundle);
            return imageFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_image, container, false);

            TouchImageView imageView = (TouchImageView) view.findViewById(R.id.imageView);

            String imageUrl = getArguments().getString(IMG_URL_KEY);
            Picasso.with(getActivity()).load(imageUrl).into(imageView);

            return view;
        }
    }

}
