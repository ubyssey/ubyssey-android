package ca.ubc.ubyssey.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.models.Articles;
import ca.ubc.ubyssey.network.GsonRequest;
import ca.ubc.ubyssey.network.RequestManager;
import de.greenrobot.event.EventBus;

/**
 * Created by Chris Li on 6/24/2015.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = SearchFragment.class.getSimpleName();

    private TextView mSearchEmptyText;
    private ListView mSearchListView;
    private NewsFeedAdapter mSearchResultsAdapter;

    private RequestManager mRequestManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mSearchEmptyText = (TextView) view.findViewById(R.id.search_error_text);
        mSearchListView = (ListView) view.findViewById(R.id.search_listview);
        mRequestManager = RequestManager.getInstance(getActivity());

        return view;
    }

    public void makeSearchRequest(final String searchTerm) {

        GsonRequest<Articles> articlesGsonRequest = new GsonRequest<Articles>(getSearchUrl(searchTerm), Articles.class, null, new Response.Listener<Articles>() {
            @Override
            public void onResponse(Articles response) {

                if (response != null) {
                    if (response.results.length > 0) {
                        if (isAdded() && getActivity() != null) {
                            response.setupNextArticles();
                            mSearchResultsAdapter = new NewsFeedAdapter(getActivity(), response.results);
                            mSearchListView.setAdapter(mSearchResultsAdapter);
                            mSearchEmptyText.setVisibility(View.GONE);

                            mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Articles.Article selectedArticle = (Articles.Article) mSearchResultsAdapter.getItem(position); //
                                    Intent articleIntent = new Intent(getActivity(), ArticleActivity.class);
                                    EventBus.getDefault().postSticky(selectedArticle);
                                    startActivity(articleIntent);
                                }
                            });
                        }
                    } else {
                        mSearchListView.setAdapter(null);
                        mSearchEmptyText.setVisibility(View.VISIBLE);
                        mSearchEmptyText.setText("No search results for \"" + searchTerm + "\".");
                    }
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

    public void showEmptyText() {
        mSearchListView.setAdapter(null);
        mSearchEmptyText.setVisibility(View.VISIBLE);
        mSearchEmptyText.setText("Please enter a search term.");
    }

    private String getSearchUrl(String searchTerm) {
        return "http://dev.ubyssey.ca/api/articles/?q=" + searchTerm;
    }

}
