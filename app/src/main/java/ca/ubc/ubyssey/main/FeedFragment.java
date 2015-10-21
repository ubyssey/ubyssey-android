package ca.ubc.ubyssey.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import ca.ubc.ubyssey.MainActivity;
import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.Utils;
import ca.ubc.ubyssey.events.NextPageEvent;
import ca.ubc.ubyssey.models.Articles;
import ca.ubc.ubyssey.models.Data;
import ca.ubc.ubyssey.models.DataTypeAdapter;
import ca.ubc.ubyssey.network.GsonRequest;
import ca.ubc.ubyssey.network.RequestBuilder;
import ca.ubc.ubyssey.network.RequestManager;
import ca.ubc.ubyssey.view.ViewHelper;
import de.greenrobot.event.EventBus;

/**
 * Fragment used to display the news list.
 * <p/>
 * Created by Chris Li on 3/16/2015.
 */
public class FeedFragment extends Fragment implements ObservableScrollViewCallbacks, AbsListView.OnScrollListener {

    private static final String TAG = FeedFragment.class.getSimpleName();
    private static final String ID_KEY = "id";
    private static final String IS_TOPIC_KEY = "topic";
    private static final int ARTICLE_REQUEST_CODE = 101;

    private static final String NEXT_PAGE_URL = "http://dev.ubyssey.ca/api/articles/?page=";

    private View mNewsHeaderView;
    private ImageView mNewsImageView;
    private ObservableListView mNewsListView;
    private NewsFeedAdapter mNewsAdapter;

    private RequestManager mRequestManager;
    private ErrorCallback mErrorCallback;

    private boolean mLoadingMore = true;
    private String mNextArticlesUrl = null;
    private String mUrl;

    private int mCurrentPageCount = 1;
    private boolean mIsNextPageEvent = false;

    private SwipeRefreshLayout mSwipeContainer;

    public static FeedFragment newInstance(int id, boolean isTopic) {
        FeedFragment fragment = new FeedFragment();

        Bundle args = new Bundle();
        args.putInt(ID_KEY, id);
        args.putBoolean(IS_TOPIC_KEY, isTopic);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mErrorCallback = (ErrorCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ErrorCallbacks.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ARTICLE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            NextPageEvent nextPageEvent = EventBus.getDefault().removeStickyEvent(NextPageEvent.class);
            if (nextPageEvent.nextPage > mCurrentPageCount) {
                mIsNextPageEvent = true;
                Thread thread = new Thread(loadMoreArticles);
                thread.start();
            } else { // retrieve the articles from the cache since we already loaded them
                if (mRequestManager.getCache().get(NEXT_PAGE_URL + nextPageEvent.nextPage) != null) {
                    String cachedData = new String(mRequestManager.getCache().get(NEXT_PAGE_URL + nextPageEvent.nextPage).data);
                    Gson gson = new GsonBuilder().registerTypeAdapter(Data.class, new DataTypeAdapter().nullSafe()).create();
                    Articles articles = gson.fromJson(cachedData, Articles.class);
                    articles.setupNextArticles();
                    articles.setPageNumbers(nextPageEvent.nextPage);
                    Intent articleIntent = new Intent(getActivity(), ArticleActivity.class);
                    EventBus.getDefault().postSticky(articles.results[0]);
                    startActivityForResult(articleIntent, ARTICLE_REQUEST_CODE);
                }
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        mNewsHeaderView = inflater.inflate(R.layout.news_list_header, null);
        mNewsImageView = (ImageView) mNewsHeaderView.findViewById(R.id.main_image);
        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mSwipeContainer.setProgressViewOffset(false, 0, 200);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                performArticlesRequest();
            }
        });
        mSwipeContainer.setColorSchemeColors(getActivity().getResources().getColor(R.color.primary_blue),
                                             getActivity().getResources().getColor(R.color.secondary_blue));
        mSwipeContainer.post(new Runnable() {
            @Override
            public void run() {
                mSwipeContainer.setRefreshing(true);
            }
        });

        mNewsListView = (ObservableListView) view.findViewById(R.id.news_listview);
        mNewsListView.setScrollViewCallbacks(this);
        mNewsListView.setOnScrollListener(this);
        mNewsListView.addHeaderView(mNewsHeaderView, null, false);

        Bundle bundle = getArguments();
        int id = bundle.getInt(ID_KEY, 0);
        boolean isTopic = bundle.getBoolean(IS_TOPIC_KEY, false);

        if (isTopic) {
            mUrl = RequestBuilder.getTopicUrl(id);
        } else {
            mUrl = getFeedUrl(id);
        }

        mRequestManager = RequestManager.getInstance(getActivity());
        performArticlesRequest();

        return view;
    }

    private Runnable loadMoreArticles = new Runnable() {
        @Override
        public void run() {
            if (mNextArticlesUrl != null)
            {
                GsonRequest<Articles> articlesGsonRequest = new GsonRequest<Articles>(mNextArticlesUrl, Articles.class, null, new Response.Listener<Articles>() {
                    @Override
                    public void onResponse(Articles response) {
                        if (response != null) {
                            if (response.results.length > 0) {
                                if (isAdded() && getActivity() != null) {
                                    mNextArticlesUrl = response.next;
                                    response.setupNextArticles();

                                    mCurrentPageCount++;
                                    response.setPageNumbers(mCurrentPageCount);

                                    int currentArticleCount = mNewsAdapter.getCount();
                                    mNewsAdapter.reload(response.results);

                                    if (mIsNextPageEvent) {
                                        Articles.Article selectedArticle = (Articles.Article) mNewsAdapter.getItem(currentArticleCount);
                                        Intent articleIntent = new Intent(getActivity(), ArticleActivity.class);
                                        EventBus.getDefault().postSticky(selectedArticle);
                                        startActivityForResult(articleIntent, ARTICLE_REQUEST_CODE);
                                        mIsNextPageEvent = false;
                                    }
                                }
                            }
                            mLoadingMore = false;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, error.toString());
                    }
                });

                mRequestManager.addToRequestQueue(articlesGsonRequest);
            }
        }
    };

    private void performArticlesRequest(){
        if (Utils.isNetworkAvailable(getActivity())) {
            mRequestManager.addToRequestQueue(getArticlesRequest());
        } else {

            if (mRequestManager.getCache().get(mUrl) != null) {
                String cachedData = new String(mRequestManager.getCache().get(mUrl).data);
                Gson gson = new GsonBuilder().registerTypeAdapter(Data.class, new DataTypeAdapter().nullSafe()).create();
                Articles articles = gson.fromJson(cachedData, Articles.class);
                mNewsAdapter = new NewsFeedAdapter(getActivity(), articles.results);
                mNewsListView.setAdapter(mNewsAdapter);
                mErrorCallback.OnInternetNotAvailable("Offline mode, network is unavailable.");
            } else {
                mErrorCallback.OnInternetNotAvailable("Please connect to a network first.");
            }
            mSwipeContainer.setRefreshing(false);
        }
    }

    private GsonRequest<Articles> getArticlesRequest(){

        return new GsonRequest<Articles>(mUrl, Articles.class, null, new Response.Listener<Articles>() {
            @Override
            public void onResponse(Articles response) {

                if (response != null) {
                    if (response.results.length > 0) {
                        if (isAdded() && getActivity() != null) {
                            mNextArticlesUrl = response.next;
                            response.setupNextArticles();
                            response.setPageNumbers(mCurrentPageCount);
                            Articles.Article firstArticle = response.results[0];
                            if (firstArticle.featured_image != null) {
                                Picasso.with(getActivity()).load(firstArticle.featured_image.url).fit().centerCrop().into(mNewsImageView);
                            }
                            mNewsAdapter = new NewsFeedAdapter(getActivity(), response.results);
                            mNewsListView.setAdapter(mNewsAdapter);

                            mNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Articles.Article selectedArticle = (Articles.Article) mNewsAdapter.getItem(position - 1);
                                    Intent articleIntent = new Intent(getActivity(), ArticleActivity.class);
                                    EventBus.getDefault().postSticky(selectedArticle);
                                    startActivityForResult(articleIntent, ARTICLE_REQUEST_CODE);
                                }
                            });
                        }
                    }
                    mLoadingMore = false;
                    mErrorCallback.HideErrorMessage();
                    mSwipeContainer.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.toString());
                mErrorCallback.OnUnableToLoad("Unable to load page.");
                mSwipeContainer.setRefreshing(false);
            }
        });
    }

    private String getFeedUrl(int category) {

        String url = "";
        switch (category) {

            case MainActivity.HOME_ITEM:
                url = RequestBuilder.HOME_URL;
                break;
            case MainActivity.CULTURE_ITEM:
                url = RequestBuilder.CULTURE_URL;
                break;
            case MainActivity.OPINION_ITEM:
                url = RequestBuilder.OPINION_URL;
                break;
            case MainActivity.FEATURES_ITEM:
                url = RequestBuilder.FEATURES_URL;
                break;
            case MainActivity.DATA_ITEM:
                url = RequestBuilder.DATA_URL;
                break;
            case MainActivity.SPORTS_ITEM:
                url = RequestBuilder.SPORTS_URL;
                break;
            case MainActivity.VIDEO_ITEM:
                url = RequestBuilder.VIDEO_URL;
                break;
            case MainActivity.BLOG_ITEM:
                url = RequestBuilder.BLOG_URL;
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

        if (loadMore && !mLoadingMore) {
            mLoadingMore = true;
            Thread thread = new Thread(loadMoreArticles);
            thread.start();

        }
    }

    public interface ErrorCallback{
        void OnInternetNotAvailable(String message);
        void OnUnableToLoad(String message);
        void HideErrorMessage();
    }

}
