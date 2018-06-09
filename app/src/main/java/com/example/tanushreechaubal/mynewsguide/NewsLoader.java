package com.example.tanushreechaubal.mynewsguide;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by TanushreeChaubal on 5/15/18.
 */

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>>{

    private static final String LOG_TAG = NewsLoader.class.getName();

    private String mURL;

    public NewsLoader(Context context, String url){
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG, "TEST: onStartLoading............");
        forceLoad();
    }

    @Override
    public List<NewsItem> loadInBackground() {
        if(mURL == null){
            return null;
        }

        List<NewsItem> news = NewsSearchListUtils.fetchNewsData(mURL);

        Log.d(LOG_TAG, "TEST: loadInBackground............");
        return news;
    }
}
