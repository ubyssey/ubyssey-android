package ca.ubc.ubyssey.main;


import android.content.Intent;
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
        mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary_blue)));
        mToolbar.setTitleTextColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.white)));

        Intent intent = getIntent();
        Article selectedArticle = (Article) intent.getSerializableExtra(ARTICLE_KEY);

        mArticleImageView = (ImageView) findViewById(R.id.article_image);
        Picasso.with(this).load(selectedArticle.featured_image.image.url).fit().centerCrop().into(mArticleImageView);

        mArticleImageCaption = (TextView) findViewById(R.id.article_image_caption);
        mArticleImageCaption.setText(selectedArticle.featured_image.caption);

        mArticleTitle = (TextView) findViewById(R.id.article_title);
        mArticleTitle.setText(selectedArticle.long_headline);

        mArticleAuthor = (TextView) findViewById(R.id.article_author);
        mArticleAuthor.setText("By " + selectedArticle.authors[0].full_name);

        mArticleContent = (TextView) findViewById(R.id.article_content);
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
        int primaryColor = getResources().getColor(R.color.primary_blue);
        int whiteColor = getResources().getColor(R.color.white);
        float alpha = 1 - (float) Math.max(0, mParallaxImageHeight - scrollY) / mParallaxImageHeight;
        mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, primaryColor));
        mToolbar.setTitleTextColor(ScrollUtils.getColorWithAlpha(alpha, whiteColor));
        ViewHelper.setTranslationY(mArticleImageView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
