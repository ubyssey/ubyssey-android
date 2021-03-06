package ca.ubc.ubyssey.main;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.List;

import ca.ubc.ubyssey.DateUtils;
import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.Utils;
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
    private List<Articles.Article> mArticles;
    private LayoutInflater mLayoutInflater = null;
    private Typeface mHeadlineTypeface;
    private Typeface mMetaTypeface;


    public NewsFeedAdapter(Context context, List<Articles.Article> articles) {
        mContext = context;
        mArticles = articles;
        mLayoutInflater = LayoutInflater.from(context);

        mHeadlineTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/LFT_Etica_Book.otf");
        mMetaTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/LFT_Etica_Light.otf");
    }

    @Override
    public int getCount() {
        return mArticles.size();
    }

    @Override
    public Object getItem(int position) {
        return mArticles.get(position);
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
            viewHolder.newsPageNumberTextView = (TextView) convertView.findViewById(R.id.page_number);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NewsItemViewHolder) convertView.getTag();
        }

        Articles.Article article = mArticles.get(position);
        try {
            String encodedString = new String(article.headline.getBytes("ISO-8859-1"), "UTF-8");
            viewHolder.newsHeadline.setText(Html.fromHtml(encodedString));
        } catch (UnsupportedEncodingException e) {
            viewHolder.newsHeadline.setText(article.headline);
            e.printStackTrace();
        }

        viewHolder.newsHeadline.setTypeface(mHeadlineTypeface);
        viewHolder.newsSectionTextView.setText(article.section.name);
        viewHolder.newsSectionTextView.setTypeface(mMetaTypeface);
        viewHolder.newsTimestampTextView.setText(DateUtils.getProperDateString(article.published_at));
        viewHolder.newsTimestampTextView.setTypeface(mMetaTypeface);
        viewHolder.newsReadTimeTextView.setTypeface(mMetaTypeface);
        viewHolder.newsReadTimeTextView.setText(article.est_reading_time + " minute read");

        if (article.importance > 3 && position != 0) {
            viewHolder.newsImageView.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(RequestBuilder.ROOT_URL + article.featured_image.url).fit().centerCrop().into(viewHolder.newsImageView);
        } else {
            viewHolder.newsImageView.setVisibility(View.GONE);
        }

        if (article.getNextArticle() == null) {
            viewHolder.newsPageNumberTextView.setVisibility(View.VISIBLE);
            viewHolder.newsPageNumberTextView.setText("p. " + article.getPageNumber());
        } else {
            viewHolder.newsPageNumberTextView.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void reload(List<Articles.Article> newArticles){
        mArticles.addAll(newArticles);
        notifyDataSetChanged();
    }

    public static class NewsItemViewHolder {
        ImageView newsImageView;
        TextView newsHeadline;
        TextView newsTimestampTextView;
        TextView newsSectionTextView;
        TextView newsReadTimeTextView;
        TextView newsPageNumberTextView;
    }

}
