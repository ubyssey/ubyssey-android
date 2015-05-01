package ca.ubc.ubyssey.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.squareup.picasso.Picasso;

import ca.ubc.ubyssey.MainActivity;
import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.models.Article;
import ca.ubc.ubyssey.models.Articles;
import ca.ubc.ubyssey.network.GsonRequest;
import ca.ubc.ubyssey.network.RequestManager;
import ca.ubc.ubyssey.view.ViewHelper;

/**
 * Fragment used to display the news list.
 * TODO: Extend class so that it can show different list content
 * <p/>
 * Created by Chris Li on 3/16/2015.
 */
public class FeedFragment extends Fragment implements ObservableScrollViewCallbacks{

    private static final String TAG = FeedFragment.class.getSimpleName();
    private static final String CATEGORY_KEY = "category";

    private View mNewsHeaderView;
    private ImageView mNewsImageView;
    private ObservableListView mNewsListView;
    private NewsFeedAdapter mNewsAdapter;

    private RequestManager mRequestManager;

    public static FeedFragment newInstance(int category) {
        FeedFragment fragment = new FeedFragment();

        Bundle args = new Bundle();
        args.putInt(CATEGORY_KEY, category);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        mNewsHeaderView = inflater.inflate(R.layout.news_list_header, null);
        mNewsImageView = (ImageView) mNewsHeaderView.findViewById(R.id.main_image);

        mNewsListView = (ObservableListView) view.findViewById(R.id.news_listview);
        mNewsListView.setScrollViewCallbacks(this);

        Bundle bundle = getArguments();
        int category = bundle.getInt(CATEGORY_KEY,0);

        GsonRequest<Articles> articlesGsonRequest = new GsonRequest<Articles>(getFeedUrl(category), Articles.class, null, new Response.Listener<Articles>() {
            @Override
            public void onResponse(Articles response) {

                if (isAdded() && getActivity() != null) {
                    Article firstArticle = response.results.get(0);
                    Picasso.with(getActivity()).load(firstArticle.featured_image.image.url).fit().centerCrop().into(mNewsImageView);
                    mNewsAdapter = new NewsFeedAdapter(getActivity(), response.results);
                    mNewsListView.addHeaderView(mNewsHeaderView, null, false);
                    mNewsListView.setAdapter(mNewsAdapter);

                    mNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Article selectedArticle = (Article) mNewsAdapter.getItem(position - 1);
                            Intent articleIntent = new Intent(getActivity(), ArticleActivity.class);
                            articleIntent.putExtra(ArticleActivity.ARTICLE_KEY, selectedArticle);
                            startActivity(articleIntent);
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.toString());
            }
        });

        mRequestManager = RequestManager.getInstance(getActivity());
        mRequestManager.addToRequestQueue(articlesGsonRequest);

        return view;
    }

    private String getFeedUrl(int category) {

        String url = "";
        switch (category) {

            case MainActivity.HOME_ITEM:
                url = "http://dev.ubyssey.ca/api/articles/";
                break;
            case MainActivity.CULTURE_ITEM:
                url = "http://petersiemens.com/dispatch/api/articles.json";
                break;
        }


        return url;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        ViewHelper.setTranslationY(mNewsHeaderView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
