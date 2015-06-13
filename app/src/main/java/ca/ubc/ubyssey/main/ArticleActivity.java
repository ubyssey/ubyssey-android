package ca.ubc.ubyssey.main;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ca.ubc.ubyssey.DateUtils;
import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.Utils;
import ca.ubc.ubyssey.customviews.AuthorTextView;
import ca.ubc.ubyssey.customviews.ParagraphTextView;
import ca.ubc.ubyssey.customviews.TitleTextView;
import ca.ubc.ubyssey.models.Articles;
import ca.ubc.ubyssey.network.RequestBuilder;
import ca.ubc.ubyssey.view.ViewHelper;
import de.greenrobot.event.EventBus;


/**
 * Activity used to view a specific article.
 * TODO: Consider using an event bus for data passing
 * <p/>
 * Created by Chris Li on 3/17/2015.
 */
public class ArticleActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {

    private static final String TAG = ArticleActivity.class.getSimpleName();

    public static final String ARTICLE_KEY = "selected_article";

    private Toolbar mToolbar;
    private ImageView mArticleImageView;
    private TextView mArticleImageCaption;
    private TextView mArticleTitle;
    private TextView mArticleAuthor;
    private TextView mArticleDate;
    private LinearLayout mArticleContent;
    private ObservableScrollView mArticleScrollView;
    private Articles.Article mSelectedArticle;

    private List<ImageView> mFeaturedImages = new ArrayList<>();
    private int mFeaturedImageHeight;
    private int mLastScrollHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mFeaturedImageHeight = (int) getResources().getDimension(R.dimen.parallax_image_height);
        mSelectedArticle = EventBus.getDefault().removeStickyEvent(Articles.Article.class);

        mArticleImageView = (ImageView) findViewById(R.id.article_image);
        Picasso.with(this).load(RequestBuilder.URL_PREFIX + mSelectedArticle.featured_image.url).fit().centerCrop().into(mArticleImageView);
        mFeaturedImages.add(mArticleImageView);

        mArticleImageCaption = (TextView) findViewById(R.id.article_image_caption);
        mArticleImageCaption.setText(mSelectedArticle.featured_image.caption);

        mArticleTitle = (TextView) findViewById(R.id.article_title);
        Typeface titleTypeFace = Typeface.createFromAsset(getAssets(), "fonts/LFT_Etica_Semibold.otf");
        mArticleTitle.setTypeface(titleTypeFace);
        mArticleTitle.setText(mSelectedArticle.long_headline);

        mArticleAuthor = (TextView) findViewById(R.id.article_author);
        Typeface metaTypeFace = Typeface.createFromAsset(getAssets(), "fonts/LFT_Etica_Bold.otf");
        mArticleAuthor.setTypeface(metaTypeFace);
        mArticleAuthor.setText("By " + mSelectedArticle.authors[0].full_name);

        mArticleDate = (TextView) findViewById(R.id.article_date);
        mArticleDate.setText("·" + DateUtils.getProperDateString(mSelectedArticle.published_at));

        mArticleContent = (LinearLayout) findViewById(R.id.article_content);
        buildArticleView();

        mArticleScrollView = (ObservableScrollView) findViewById(R.id.article_scrollview);
        mArticleScrollView.setScrollViewCallbacks(this);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mArticleScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        Log.i(TAG, String.valueOf(mLastScrollHeight));
        for (int i = 0; i < mFeaturedImages.size(); i++) {
            Log.i(TAG, "totalScrollViewHeight: " + mArticleScrollView.getChildAt(0).getHeight());
            Log.i(TAG, "scrollY: " + scrollY + "firstScroll: " + firstScroll );
            Log.i(TAG +": " + i, "imageY: " + mFeaturedImages.get(i).getY() +  "imageScrollY: " + mFeaturedImages.get(i).getScrollY() +  "imageTranslationY: " + mFeaturedImages.get(i).getTranslationY() + "mFeaturedImageHeight: "+ mFeaturedImageHeight);

            if (i == 0 ){
                ViewHelper.setTranslationY(mFeaturedImages.get(i), (scrollY ) / 2 );
            } else {
                ViewHelper.setTranslationY(mFeaturedImages.get(i), (scrollY - mLastScrollHeight + (2*mFeaturedImageHeight))/ 2 );
            }
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    private void buildArticleView(){

        Articles.Article.Content[] contents = mSelectedArticle.content;

        LinearLayout.LayoutParams paragraphLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paragraphLayoutParams.setMargins(0,(int) getResources().getDimension(R.dimen.extra_padding),0,(int) getResources().getDimension(R.dimen.extra_padding));
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.parallax_image_height));
        imageLayoutParams.setMargins(0,(int) getResources().getDimension(R.dimen.extra_padding),0,(int) getResources().getDimension(R.dimen.extra_padding));

        buildArticleContent(contents);

        View separator = getLayoutInflater().inflate(R.layout.custom_separator, null);
        separator.setLayoutParams(paragraphLayoutParams);
        mArticleContent.addView(separator);

        if (mSelectedArticle.getNextArticle() != null) {
            final Articles.Article nextArticle = mSelectedArticle.getNextArticle();
            final ImageView nextArticleImage = new ImageView(this);
            nextArticleImage.setLayoutParams(imageLayoutParams);
            Picasso.with(this).load(RequestBuilder.URL_PREFIX + nextArticle.featured_image.url).fit().centerCrop().into(nextArticleImage);
            mArticleContent.addView(nextArticleImage);

            nextArticleImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextArticleImage.setVisibility(View.GONE);
                    mArticleContent.removeView(nextArticleImage);
                    loadNextArticle(nextArticle);
                }
            });

        }

    }


    private void buildArticleContent(Articles.Article.Content[] contents) {

        LinearLayout.LayoutParams paragraphLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paragraphLayoutParams.setMargins(0,(int) getResources().getDimension(R.dimen.extra_padding),0,(int) getResources().getDimension(R.dimen.extra_padding));
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.parallax_image_height));
        imageLayoutParams.setMargins(0,(int) getResources().getDimension(R.dimen.extra_padding),0,(int) getResources().getDimension(R.dimen.extra_padding));

        Typeface contentTypeFace = Typeface.createFromAsset(getAssets(), "fonts/DroidSerif-Regular.ttf");

        int sidePadding = (int) getResources().getDimension(R.dimen.text_padding);

        for (final Articles.Article.Content content : contents) {

            if (content.type.equals("paragraph")) {
                TextView paragraph = new TextView(this);
                paragraph.setLayoutParams(paragraphLayoutParams);
                paragraph.setTextColor(Color.BLACK);
                paragraph.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                paragraph.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f,  getResources().getDisplayMetrics()), 1.0f);
                paragraph.setTypeface(contentTypeFace);
                paragraph.setPadding(sidePadding, 0, sidePadding, 0);

                try {
                    String encodedString = new String(content.data.paragraph.getBytes("ISO-8859-1"), "UTF-8");
                    paragraph.setText(Html.fromHtml(encodedString));
                    mArticleContent.addView(paragraph);
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, e.toString());
                }

            } else if (content.type.equals("image")) {
                final ImageView image = new ImageView(this);
                image.setLayoutParams(imageLayoutParams);
                mArticleContent.addView(image);
                Picasso.with(this).load(RequestBuilder.URL_PREFIX + content.data.url).fit().centerCrop().into(image);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ArticleActivity.this, ImageActivity.class);
                        EventBus.getDefault().postSticky(content.data);
                        startActivity(intent);
                    }
                });
            }

        }
    }



    private void loadNextArticle(Articles.Article nextArticle){

        View view = getLayoutInflater().inflate(R.layout.article_layout, null);
        mArticleContent.addView(view);
        ImageView articleImageView = (ImageView) view.findViewById(R.id.article_image);
        Picasso.with(this).load(RequestBuilder.URL_PREFIX + nextArticle.featured_image.url).fit().centerCrop().into(articleImageView);
        mFeaturedImages.add(articleImageView);

        TextView articleImageCaption = (TextView) view.findViewById(R.id.article_image_caption);
        articleImageCaption.setText(nextArticle.featured_image.caption);

        TextView articleTitle = (TextView) view.findViewById(R.id.article_title);
        Typeface titleTypeFace = Typeface.createFromAsset(getAssets(), "fonts/LFT_Etica_Semibold.otf");
        articleTitle.setTypeface(titleTypeFace);
        articleTitle.setText(nextArticle.long_headline);

        TextView articleAuthor = (TextView) view.findViewById(R.id.article_author);
        Typeface metaTypeFace = Typeface.createFromAsset(getAssets(), "fonts/LFT_Etica_Bold.otf");
        articleAuthor.setTypeface(metaTypeFace);
        articleAuthor.setText("By " +nextArticle.authors[0].full_name);

        TextView articleDate = (TextView) view.findViewById(R.id.article_date);
        articleDate.setText("·" + DateUtils.getProperDateString(nextArticle.published_at));

        buildArticleContent(nextArticle.content);

    }


}
