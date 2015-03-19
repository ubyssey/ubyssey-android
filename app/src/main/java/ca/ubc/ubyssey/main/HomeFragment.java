package ca.ubc.ubyssey.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.models.Article;
import ca.ubc.ubyssey.models.Articles;
import ca.ubc.ubyssey.network.GsonRequest;
import ca.ubc.ubyssey.network.RequestManager;

/**
 * Fragment used to display the news list.
 * TODO: Extend class so that it can show different list content
 * <p/>
 * Created by Chris Li on 3/16/2015.
 */
public class HomeFragment extends Fragment {

    private static final String TEST_URL = "http://petersiemens.com/dispatch/api/articles.json";


    private View mNewsHeaderView;
    private ImageView mNewsImageView;
    private ListView mNewsListView;
    private NewsFeedAdapter mNewsAdapter;

    private RequestManager mRequestManager;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mNewsHeaderView = inflater.inflate(R.layout.news_list_header, null);
        mNewsImageView = (ImageView) mNewsHeaderView.findViewById(R.id.main_image);
        mNewsListView = (ListView) view.findViewById(R.id.news_listview);
        GsonRequest<Articles> articlesGsonRequest = new GsonRequest<Articles>(TEST_URL, Articles.class, null, new Response.Listener<Articles>() {
            @Override
            public void onResponse(Articles response) {
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mRequestManager = RequestManager.getInstance(getActivity());
        mRequestManager.addToRequestQueue(articlesGsonRequest);

        return view;
    }

}
