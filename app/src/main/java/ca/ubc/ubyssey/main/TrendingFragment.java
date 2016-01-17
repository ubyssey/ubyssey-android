package ca.ubc.ubyssey.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import ca.ubc.ubyssey.R;
import ca.ubc.ubyssey.models.Trending;
import ca.ubc.ubyssey.network.GsonRequest;
import ca.ubc.ubyssey.network.RequestManager;

/**
 * Created by Chris Li on 7/27/2015.
 */
public class TrendingFragment extends Fragment {

    private static final String TAG = TrendingFragment.class.getSimpleName();

    private ListView mTrendingList;
    private TrendingFeedAdapter mTrendingFeedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trending, container, false);

        mTrendingList = (ListView) view.findViewById(R.id.trending_listview);
        GsonRequest<Trending> trendingGsonRequest = new GsonRequest<Trending>("http://dev.ubyssey.ca/api/trending/", Trending.class, null, new Response.Listener<Trending>() {
            @Override
            public void onResponse(Trending response) {

                if (response != null) {
                    if (response.results.size() > 0) {
                        if (isAdded() && getActivity() != null) {
                            mTrendingFeedAdapter = new TrendingFeedAdapter(getActivity(), response.results);
                            mTrendingList.setAdapter(mTrendingFeedAdapter);
                            mTrendingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                }
                            });
                        }
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        RequestManager requestManager = RequestManager.getInstance(getActivity());
        requestManager.addToRequestQueue(trendingGsonRequest);

        return view;
    }
}
