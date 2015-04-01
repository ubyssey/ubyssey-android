package ca.ubc.ubyssey.main;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.squareup.picasso.Picasso;

import ca.ubc.ubyssey.DateUtils;
import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.models.Article;
import ca.ubc.ubyssey.view.ViewHelper;


/**
 * Activity used to view a specific article.
 * TODO: Consider using an event bus for data passing
 * <p/>
 * Created by Chris Li on 3/17/2015.
 */
public class ArticleActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {

    public static final String ARTICLE_KEY = "selected_article";

    private Toolbar mToolbar;
    private ImageView mArticleImageView;
    private TextView mArticleImageCaption;
    private TextView mArticleTitle;
    private TextView mArticleAuthor;
    private TextView mArticleDate;
    private TextView mArticleContent;
    private ObservableScrollView mArticleScrollView;
    private int mParallaxImageHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        Article selectedArticle = (Article) intent.getSerializableExtra(ARTICLE_KEY);

        mArticleImageView = (ImageView) findViewById(R.id.article_image);
        Picasso.with(this).load(selectedArticle.featured_image.image.url).fit().centerCrop().into(mArticleImageView);

        mArticleImageCaption = (TextView) findViewById(R.id.article_image_caption);
        mArticleImageCaption.setText(selectedArticle.featured_image.caption);

        mArticleTitle = (TextView) findViewById(R.id.article_title);
        Typeface titleTypeFace = Typeface.createFromAsset(getAssets(), "fonts/LFT_Etica_Semibold.otf");
        mArticleTitle.setTypeface(titleTypeFace);
        mArticleTitle.setText(selectedArticle.long_headline);

        mArticleAuthor = (TextView) findViewById(R.id.article_author);
        Typeface metaTypeFace = Typeface.createFromAsset(getAssets(), "fonts/LFT_Etica_Bold.otf");
        mArticleAuthor.setTypeface(metaTypeFace);
        mArticleAuthor.setText("By " + selectedArticle.authors[0].full_name);

        mArticleDate = (TextView) findViewById(R.id.article_date);
        mArticleDate.setText("·" + DateUtils.getProperDateString(selectedArticle.published_at));

        mArticleContent = (TextView) findViewById(R.id.article_content);
        Typeface contentTypeFace = Typeface.createFromAsset(getAssets(), "fonts/DroidSerif-Regular.ttf");
        mArticleContent.setTypeface(contentTypeFace);
        mArticleContent.setText(Html.fromHtml(selectedArticle.content));

        mArticleScrollView = (ObservableScrollView) findViewById(R.id.article_scrollview);
        mArticleScrollView.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

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
        ViewHelper.setTranslationY(mArticleImageView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
