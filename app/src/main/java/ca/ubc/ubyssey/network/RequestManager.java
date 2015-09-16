package ca.ubc.ubyssey.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Request Manager singleton class
 * <p/>
 * Created by Chris Li on 3/16/2015.
 */
public class RequestManager {

    private static RequestManager mInstance = null;
    private RequestQueue mRequestQueue;

    private static Context mContext;

    private RequestManager(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;

    }

    public static synchronized RequestManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestManager(context);
        }

        return mInstance;
    }


    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public com.android.volley.Cache getCache(){
        return mRequestQueue.getCache();
    }
}
