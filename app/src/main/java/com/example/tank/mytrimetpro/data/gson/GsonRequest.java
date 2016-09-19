package com.example.tank.mytrimetpro.data.gson;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by jmiller on 8/1/2016.
 */

public class GsonRequest<T> extends Request<T> {

    private final Gson mGson;
    private final Class<T> mClazz;
    private final Map<String, String> mHeaders;
    private final Response.Listener mListeners;


    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener){
        super(Method.GET, url, errorListener);
        this.mClazz = clazz;
        this.mHeaders = headers;
        this.mListeners = listener;

        mGson = new Gson();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if(mHeaders != null){
            return mHeaders;
        }else{
            return super.getHeaders();
        }
    }

    @Override
    protected void deliverResponse(T response){
        mListeners.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response){
        try{
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(json, mClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        }catch (UnsupportedEncodingException e){
            return Response.error(new ParseError(e));
        }catch (JsonSyntaxException e){
            return Response.error(new ParseError(e));
        }
    }
}
