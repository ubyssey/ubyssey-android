package ca.ubc.ubyssey.main;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.squareup.picasso.Picasso;

import ca.ubc.ubyssey.DateUtils;
import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.models.Article;
import ca.ubc.ubyssey.models.Content;
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
    private LinearLayout mArticleContent;
    private ObservableScrollView mArticleScrollView;

    private Article mSelectedArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();

        if (intent != null) {
            mSelectedArticle = (Article) intent.getSerializableExtra(ARTICLE_KEY);
        }

        mArticleImageView = (ImageView) findViewById(R.id.article_image);
        Picasso.with(this).load(mSelectedArticle.featured_image.image.url).fit().centerCrop().into(mArticleImageView);

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
        buildArticle();

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
        ViewHelper.setTranslationY(mArticleImageView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    private void buildArticle(){

        Content[] contents = mSelectedArticle.content;
        Typeface contentTypeFace = Typeface.createFromAsset(getAssets(), "fonts/DroidSerif-Regular.ttf");
        LinearLayout.LayoutParams paragraphLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paragraphLayoutParams.setMargins(0,(int) getResources().getDimension(R.dimen.extra_padding),0,(int) getResources().getDimension(R.dimen.extra_padding));
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.article_image_height));
        imageLayoutParams.setMargins(0,(int) getResources().getDimension(R.dimen.extra_padding),0,(int) getResources().getDimension(R.dimen.extra_padding));

        for (final Content content : contents) {

            if (content.type.equals("paragraph")) {
                TextView paragraph = new TextView(this);
                paragraph.setLayoutParams(paragraphLayoutParams);
                paragraph.setTextColor(Color.BLACK);
                paragraph.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                paragraph.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f,  getResources().getDisplayMetrics()), 1.0f);
                paragraph.setTypeface(contentTypeFace);
                paragraph.setText(Html.fromHtml(content.data.paragraph));
                mArticleContent.addView(paragraph);
            } else if (content.type.equals("image")) {
                final ImageView image = new ImageView(this);
                image.setLayoutParams(imageLayoutParams);
                mArticleContent.addView(image);
                Picasso.with(this).load(content.data.url).fit().centerCrop().into(image);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ArticleActivity.this, ImageActivity.class);
                        intent.putExtra(ImageActivity.URL_KEY,content.data.url);
                        startActivity(intent);
                    }
                });
            }

        }
    }

}
