package com.example.tank.mytrimetpro.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import javax.inject.Singleton;

/**
 * Created by tank on 8/31/16.
 */

@Singleton
public class DataRequestQueue {

    public final String TAG = DataRequestQueue.class.getSimpleName();

    private RequestQueue mRequestQueue;

    public DataRequestQueue(Context context){
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        mRequestQueue.add(req);
    }

    /* This method is left here in the case that a request might
     * need to be cancelled. */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
