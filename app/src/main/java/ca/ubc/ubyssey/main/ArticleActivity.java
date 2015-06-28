package ca.ubc.ubyssey.main;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class ArticleActivity extends ActionBarActivity implements ObservableScrollViewCallbacks, View.OnClickListener {

    private static final String TAG = ArticleActivity.class.getSimpleName();

    public static final String ARTICLE_KEY = "selected_article";

    private Toolbar mToolbar;
    private ImageView mArticleImageView;
    private TextView mArticleImageCaption;
    private TextView mArticleTitle;
    private TextView mArticleAuthor;
    private TextView mArticleDate;
    private Button mPreviousButton;
    private LinearLayout mArticleContent;
    private ObservableScrollView mArticleScrollView;
    private Articles.Article mSelectedArticle;
    private Articles.Article mNextArticle;

    private LayoutInflater mLayoutInflater;

    private List<View> mFeaturedImages = new ArrayList<>();
    private List<Integer> mTops = new ArrayList<>();
    private int mFeaturedImageHeight;
    private int mScrollViewHeight = 0;
    private int mArticlePosition = 0;

    private boolean loadingMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mFeaturedImageHeight = (int) getResources().getDimension(R.dimen.parallax_image_height);
        mLayoutInflater = getLayoutInflater();
        mSelectedArticle = EventBus.getDefault().removeStickyEvent(Articles.Article.class);
        mNextArticle = mSelectedArticle.getNextArticle();

        mArticleImageView = (ImageView) findViewById(R.id.article_image);
        Picasso.with(this).load(mSelectedArticle.featured_image.url).fit().centerCrop().into(mArticleImageView);
        mFeaturedImages.add(mArticleImageView);
        mTops.add(0);

        mArticleImageCaption = (TextView) findViewById(R.id.article_image_caption);
        mArticleImageCaption.setText(mSelectedArticle.featured_image.caption);

        mArticleTitle = (TextView) findViewById(R.id.article_title);
        Typeface titleTypeFace = Typeface.createFromAsset(getAssets(), "fonts/LFT_Etica_Semibold.otf");
        mArticleTitle.setTypeface(titleTypeFace);
        mArticleTitle.setText(mSelectedArticle.long_headline);

        mArticleAuthor = (TextView) findViewById(R.id.article_author);
        Typeface metaTypeFace = Typeface.createFromAsset(getAssets(), "fonts/LFT_Etica_Bold.otf");

        mArticleAuthor.setTypeface(metaTypeFace);
        if (mSelectedArticle.authors.length > 0) {
            mArticleAuthor.setText("By " + mSelectedArticle.authors[0].full_name);
        }

        mArticleDate = (TextView) findViewById(R.id.article_date);
        mArticleDate.setText("·" + DateUtils.getProperDateString(mSelectedArticle.published_at));

        mArticleContent = (LinearLayout) findViewById(R.id.article_content);
        buildArticleView();

        mPreviousButton = (Button) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(this);

        mArticleScrollView = (ObservableScrollView) findViewById(R.id.article_scrollview);
        mArticleScrollView.setScrollViewCallbacks(this);

        addNewHeight();

    }

    private void addNewHeight(){
        final ViewTreeObserver observer = mArticleScrollView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mScrollViewHeight = mArticleScrollView.getChildAt(0).getHeight();

                /*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mArticleScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mArticleScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }*/
            }
        });
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

    /**
     * This method contains the logic for the parallax effect for each featured image
     *
     * @param scrollY
     * @param firstScroll
     * @param dragging
     */
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        if (scrollY > (0.75f * mScrollViewHeight) && !loadingMore) {
            if (mNextArticle != null) {
                loadingMore = true;
                loadNextArticle(mNextArticle);
            }
        }

        Rect scrollBounds = new Rect();
        mArticleScrollView.getHitRect(scrollBounds);

        for (int i = 0; i < mFeaturedImages.size(); i++) {

            if (mFeaturedImages.get(i).getLocalVisibleRect(scrollBounds)) {
                mArticlePosition = i;

                if (i == 0) {
                    mPreviousButton.setText("top");
                    ViewHelper.setTranslationY(mFeaturedImages.get(i), scrollY/2);
                } else {
                    mPreviousButton.setText(String.valueOf(i+1));
                    Display display = getWindowManager().getDefaultDisplay();
                    DisplayMetrics outMetrics = new DisplayMetrics();
                    display.getMetrics(outMetrics);

                    float screenDensity = getResources().getDisplayMetrics().density;
                    float dpScreenHeight = outMetrics.heightPixels/screenDensity;
                    int pixelsScreenHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpScreenHeight, getResources().getDisplayMetrics());
                    int halfPixelsScreenHeight = pixelsScreenHeight/2;

                    int center = halfPixelsScreenHeight - (mFeaturedImageHeight/2);

                    int[] screenLocation = {0,0};
                    mFeaturedImages.get(i).getLocationOnScreen(screenLocation);

                    int finalLocation = ((screenLocation[1] - center) * 100) / halfPixelsScreenHeight;

                    ViewHelper.setTranslationY(mFeaturedImages.get(i).findViewById(R.id.article_image), -finalLocation * 0.9f * screenDensity);
                }

            }

        }

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        /*if (scrollState == ScrollState.DOWN) {
            mPreviousButton.animate().alpha(1.0f).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mPreviousButton.setVisibility(View.VISIBLE);

                }
            });
        }

        if (scrollState == ScrollState.UP) {
            mPreviousButton.animate().alpha(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mPreviousButton.setVisibility(View.GONE);
                }
            });
        }*/

    }

    /**
     * Creates view of the main article, i.e. the selected article
     *
     */
    private void buildArticleView(){

        Articles.Article.Content[] contents = mSelectedArticle.content;

        LinearLayout.LayoutParams paragraphLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paragraphLayoutParams.setMargins(0,(int) getResources().getDimension(R.dimen.extra_padding),0,(int) getResources().getDimension(R.dimen.extra_padding));
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.parallax_image_height));
        imageLayoutParams.setMargins(0,(int) getResources().getDimension(R.dimen.extra_padding),0,(int) getResources().getDimension(R.dimen.extra_padding));

        buildArticleContent(contents);

        View separator = mLayoutInflater.inflate(R.layout.custom_separator, null);
        separator.setLayoutParams(paragraphLayoutParams);
        mArticleContent.addView(separator);

    }

    /**
     * Builds the article body depending on the "types" of each content
     *
     * @param contents
     */
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
                Picasso.with(this).load(content.data.url).fit().centerCrop().into(image);
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


    /**
     * Loads the next article that the current article is pointing to
     *
     * @param nextArticle
     */
    private void loadNextArticle(Articles.Article nextArticle){

        View view = mLayoutInflater.inflate(R.layout.article_layout, null);
        mArticleContent.addView(view);
        FrameLayout imageContainer = (FrameLayout) view.findViewById(R.id.article_image_container);
        ImageView articleImageView = (ImageView) view.findViewById(R.id.article_image);
        Picasso.with(this).load(nextArticle.featured_image.url).fit().centerCrop().into(articleImageView);
        mFeaturedImages.add(imageContainer);

        TextView articleImageCaption = (TextView) view.findViewById(R.id.article_image_caption);
        articleImageCaption.setText(nextArticle.featured_image.caption);

        TextView articleTitle = (TextView) view.findViewById(R.id.article_title);
        Typeface titleTypeFace = Typeface.createFromAsset(getAssets(), "fonts/LFT_Etica_Semibold.otf");
        articleTitle.setTypeface(titleTypeFace);
        articleTitle.setText(nextArticle.long_headline);

        TextView articleAuthor = (TextView) view.findViewById(R.id.article_author);
        Typeface metaTypeFace = Typeface.createFromAsset(getAssets(), "fonts/LFT_Etica_Bold.otf");
        articleAuthor.setTypeface(metaTypeFace);
        if (nextArticle.authors.length > 0) {
            articleAuthor.setText("By " + nextArticle.authors[0].full_name);
        }
        TextView articleDate = (TextView) view.findViewById(R.id.article_date);
        articleDate.setText("·" + DateUtils.getProperDateString(nextArticle.published_at));

        buildArticleContent(nextArticle.content);

        LinearLayout.LayoutParams paragraphLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paragraphLayoutParams.setMargins(0,(int) getResources().getDimension(R.dimen.extra_padding),0,(int) getResources().getDimension(R.dimen.extra_padding));

        View separator = mLayoutInflater.inflate(R.layout.custom_separator, null);
        separator.setLayoutParams(paragraphLayoutParams);
        mArticleContent.addView(separator);

        mNextArticle = mNextArticle.getNextArticle();
        loadingMore = false;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.previous_button:
                /*new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mArticleScrollView.smoothScrollTo(0, mTops.get(mArticlePosition));
                    }
                });*/

                break;

        }

    }

    /*
    public void setNextUpLayout(final Articles.Article nextUpArticle) {
        //sets up the preview of the next article at the bottom of the article
        if (nextUpArticle != null) {

            final RelativeLayout nextArticleImageLayout = (RelativeLayout) mLayoutInflater.inflate(R.layout.next_image_layout, null);
            ImageView nextArticleImage = (ImageView) nextArticleImageLayout.findViewById(R.id.next_article_image);
            TextView nextArticleTitleText = (TextView) nextArticleImageLayout.findViewById(R.id.next_article_title);

            Picasso.with(this).load(RequestBuilder.URL_PREFIX + nextUpArticle.featured_image.url).fit().centerCrop().into(nextArticleImage);
            Typeface titleTypeFace = Typeface.createFromAsset(getAssets(), "fonts/LFT_Etica_Semibold.otf");
            nextArticleTitleText.setTypeface(titleTypeFace);
            nextArticleTitleText.setText(nextUpArticle.long_headline);
            mArticleContent.addView(nextArticleImageLayout);

            nextArticleImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextArticleImageLayout.setVisibility(View.GONE);
                    mArticleContent.removeView(nextArticleImageLayout);
                    loadNextArticle(nextUpArticle.getNextArticle());
                }
            });

        }
    }*/

}
