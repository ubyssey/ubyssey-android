package ca.ubc.ubyssey.main;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ca.ubc.ubyssey.DateUtils;
import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.models.Articles;
import ca.ubc.ubyssey.network.RequestBuilder;

/**
 * Adapter class to show the news feed items
 * <p/>
 * Created by Chris Li on 3/17/2015.
 */
public class NewsFeedAdapter extends BaseAdapter {

    private static final String TAG = NewsFeedAdapter.class.getSimpleName();

    private Context mContext;
    private Articles.Article[] mArticles;
    private LayoutInflater mLayoutInflater = null;
    private Typeface mHeadlineTypeface;
    private Typeface mMetaTypeface;

    public NewsFeedAdapter(Context context, Articles.Article[] articles) {
        mContext = context;
        mArticles = articles;
        mLayoutInflater = LayoutInflater.from(context);

        mHeadlineTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/LFT_Etica_Book.otf");
        mMetaTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/LFT_Etica_Light.otf");
    }

    @Override
    public int getCount() {
        return mArticles.length;
    }

    @Override
    public Object getItem(int position) {
        return mArticles[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NewsItemViewHolder viewHolder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.news_list_item, parent, false);
            viewHolder = new NewsItemViewHolder();
            viewHolder.newsImageView = (ImageView) convertView.findViewById(R.id.news_image);
            viewHolder.newsHeadline = (TextView) convertView.findViewById(R.id.headline_label);
            viewHolder.newsSectionTextView = (TextView) convertView.findViewById(R.id.section_label);
            viewHolder.newsTimestampTextView = (TextView) convertView.findViewById(R.id.time_label);
            viewHolder.newsReadTimeTextView = (TextView) convertView.findViewById(R.id.read_label);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NewsItemViewHolder) convertView.getTag();
        }

        Articles.Article article = mArticles[position];
        viewHolder.newsHeadline.setText(article.long_headline);
        viewHolder.newsHeadline.setTypeface(mHeadlineTypeface);
        viewHolder.newsSectionTextView.setText(article.section.name);
        viewHolder.newsSectionTextView.setTypeface(mMetaTypeface);
        viewHolder.newsTimestampTextView.setText(DateUtils.getProperDateString(article.published_at));
        viewHolder.newsTimestampTextView.setTypeface(mMetaTypeface);
        viewHolder.newsReadTimeTextView.setTypeface(mMetaTypeface);

        if (article.importance > 3 && position != 0) {
            viewHolder.newsImageView.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(RequestBuilder.URL_PREFIX + article.featured_image.url).fit().centerCrop().into(viewHolder.newsImageView);
        } else {
            viewHolder.newsImageView.setVisibility(View.GONE);
        }


        return convertView;
    }

    public class NewsItemViewHolder {
        ImageView newsImageView;
        TextView newsHeadline;
        TextView newsTimestampTextView;
        TextView newsSectionTextView;
        TextView newsReadTimeTextView;
    }

}
