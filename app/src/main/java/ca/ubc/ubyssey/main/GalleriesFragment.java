package ca.ubc.ubyssey.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.List;

import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.models.Galleries;
import ca.ubc.ubyssey.network.GsonRequest;
import ca.ubc.ubyssey.network.RequestBuilder;
import ca.ubc.ubyssey.network.RequestManager;
import de.greenrobot.event.EventBus;

/**
 * Created by Chris Li on 10/20/2015.
 */
public class GalleriesFragment extends Fragment{

    private static final String TAG = GalleriesFragment.class.getSimpleName();

    private RecyclerView mGalleriesRecyclerView;
    private GalleriesListAdapter mGalleriesAdapter;

    private RequestManager mRequestManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_galleries, container, false);

        mGalleriesRecyclerView = (RecyclerView) view.findViewById(R.id.galleries_recyclerview);
        mGalleriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        GsonRequest<Galleries> galleriesGsonRequest = new GsonRequest<>(RequestBuilder.GALLERIES_URL, Galleries.class, null, new Response.Listener<Galleries>() {
            @Override
            public void onResponse(Galleries galleries) {
                mGalleriesAdapter = new GalleriesListAdapter(getActivity(), galleries.results);
                mGalleriesRecyclerView.setAdapter(mGalleriesAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        mRequestManager = RequestManager.getInstance(getActivity());
        mRequestManager.addToRequestQueue(galleriesGsonRequest);

        return view;
    }

    private class GalleriesListAdapter extends RecyclerView.Adapter<GalleriesListAdapter.ViewHolder> {

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private List<Galleries.Gallery> mGalleryList;

        public GalleriesListAdapter (Context context, List<Galleries.Gallery> galleries) {

            this.mContext = context;
            this.mLayoutInflater = LayoutInflater.from(context);
            this.mGalleryList = galleries;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.gallery_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Galleries.Gallery gallery = mGalleryList.get(position);
            holder.titleTextView.setText(gallery.title);
            GalleryListAdapter adapter = new GalleryListAdapter(mContext, gallery.images, gallery.title);
            holder.galleryRecyclerView.setAdapter(adapter);
            holder.galleryRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            holder.galleryRecyclerView.setLayoutManager(layoutManager);
        }

        @Override
        public int getItemCount() {
            return mGalleryList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            RecyclerView galleryRecyclerView;

            public ViewHolder(View v) {
                super(v);
                titleTextView = (TextView) v.findViewById(R.id.gallery_title);
                galleryRecyclerView = (RecyclerView) v.findViewById(R.id.gallery_recycler_view);
            }
        }

    }

    private static class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.ViewHolder> {

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private List<Galleries.Image> mGalleryPhotoList;
        private String mGalleryTitle;

        public GalleryListAdapter(Context context, List<Galleries.Image> galleryImages, String title) {
            this.mContext = context;
            this.mLayoutInflater = LayoutInflater.from(context);
            this.mGalleryPhotoList = galleryImages;
            this.mGalleryTitle = title;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.gallery_image_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            Galleries.Image thumbnail = mGalleryPhotoList.get(position);
            Picasso.with(mContext)
                    .load(thumbnail.thumb)
                    .fit()
                    .centerCrop()
                    .into(holder.galleryImage);

            holder.galleryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, ImageActivity.class);
                    intent.putExtra(ImageActivity.TOOLBAR_TITLE_KEY, mGalleryTitle);
                    intent.putExtra(ImageActivity.IS_PAGER_KEY, true);
                    intent.putExtra(ImageActivity.GALLERY_ITEM_POSITION_KEY, position);
                    Galleries.Image[] imageArray = mGalleryPhotoList.toArray(new Galleries.Image[mGalleryPhotoList.size()]);
                    EventBus.getDefault().postSticky(imageArray);
                    mContext.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mGalleryPhotoList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView galleryImage;

            public ViewHolder(View v) {
                super(v);
                galleryImage = (ImageView) v.findViewById(R.id.gallery_image_thumbnail);
            }
        }

    }

}
