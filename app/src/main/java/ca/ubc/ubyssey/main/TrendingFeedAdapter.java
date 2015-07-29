package ca.ubc.ubyssey.main;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ca.ubc.ubyssey.DateUtils;
import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.models.Trending;

/**
 * Created by Chris Li on 7/27/2015.
 */
public class TrendingFeedAdapter extends BaseAdapter {

    private static final String TAG = TrendingFeedAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mLayoutInflater = null;
    private Trending.TrendingItem[] mTrendingItems;
    private Typeface mHeadlineTypeface;
    private Typeface mMetaTypeface;

    public TrendingFeedAdapter (Context context, Trending.TrendingItem[] trendingItems) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mTrendingItems = trendingItems;

        mHeadlineTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/LFT_Etica_Book.otf");
        mMetaTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/LFT_Etica_Light.otf");
    }

    @Override
    public int getCount() {
        return mTrendingItems.length;
    }

    @Override
    public Object getItem(int position) {
        return mTrendingItems[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TrendingItemViewHolder trendingItemViewHolder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.trending_list_item, parent, false);
            trendingItemViewHolder = new TrendingItemViewHolder();
            trendingItemViewHolder.postImage = (ImageView) convertView.findViewById(R.id.trending_image);
            trendingItemViewHolder.posterText = (TextView) convertView.findViewById(R.id.poster_label);
            trendingItemViewHolder.postText = (TextView) convertView.findViewById(R.id.post_label);
            trendingItemViewHolder.timeText = (TextView) convertView.findViewById(R.id.time_label);
            trendingItemViewHolder.atText = (TextView) convertView.findViewById(R.id.at_label);
            trendingItemViewHolder.sourceImage = (ImageView) convertView.findViewById(R.id.source_icon);
            convertView.setTag(trendingItemViewHolder);
        } else {
            trendingItemViewHolder = (TrendingItemViewHolder) convertView.getTag();
        }

        Trending.TrendingItem trendingItem = mTrendingItems[position];
        if (trendingItem.name != null) {
            trendingItemViewHolder.posterText.setText(trendingItem.name);
        } else if (trendingItem.title != null) {
            trendingItemViewHolder.posterText.setText(trendingItem.title);
        }
        trendingItemViewHolder.posterText.setTypeface(mHeadlineTypeface);
        trendingItemViewHolder.postText.setText(trendingItem.content);
        trendingItemViewHolder.postText.setTypeface(mMetaTypeface);
        trendingItemViewHolder.timeText.setText(DateUtils.getProperDateString(trendingItem.timestamp));
        trendingItemViewHolder.atText.setText("@" + trendingItem.handle);

        if (trendingItem.image != null) {
            trendingItemViewHolder.postImage.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(trendingItem.image).fit().centerCrop().into(trendingItemViewHolder.postImage);
        } else {
            trendingItemViewHolder.postImage.setVisibility(View.GONE);
        }

        if (trendingItem.source.equals("instagram")) {
            trendingItemViewHolder.sourceImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.instagram));
        } else if (trendingItem.source.equals("twitter")) {
            trendingItemViewHolder.sourceImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.twitter));
        } else if (trendingItem.source.equals("reddit")) {
            trendingItemViewHolder.sourceImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.reddit));
        } else if (trendingItem.source.equals("facebook")) {
            trendingItemViewHolder.sourceImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.facebook));
        } else {
            trendingItemViewHolder.sourceImage.setImageDrawable(null);
        }

        return convertView;
    }

    public static class TrendingItemViewHolder {
        TextView posterText;
        TextView postText;
        TextView timeText;
        TextView atText;
        ImageView sourceImage;
        ImageView postImage;
    }
}
