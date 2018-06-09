package com.example.tanushreechaubal.mynewsguide;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>>{

    private TextView emptyStateTextView;
    private static final String LOG_TAG = MainActivity.class.getName();
    private NewsAdapter newsAdapter;
    private static final int NEWS_LOADER_ID = 1;
    private ProgressBar spinner;
    private SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyStateTextView = findViewById(R.id.emptyState_TextView);

            ListView listView = findViewById(R.id.list_item);
            newsAdapter = new NewsAdapter(this, new ArrayList<NewsItem>());
            listView.setAdapter(newsAdapter);

            listView.setEmptyView(emptyStateTextView);
            refresh = findViewById(R.id.swiperefresh);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NewsItem  newsItem = newsAdapter.getItem(position);
                    Uri uri = Uri.parse(newsItem.getWebURL());
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(websiteIntent);
                }
            });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, MainActivity.this);

            refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(NEWS_LOADER_ID, null, MainActivity.this);
                    refresh.setRefreshing(false);
                }
            });

        } else {
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText("No internet connection.");
        }
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int i, Bundle bundle) {

        Uri baseUri = Uri.parse("https://content.guardianapis.com/search?");
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", "sports");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "test");

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> news) {
        spinner = findViewById(R.id.loading_spinner);
        spinner.setVisibility(View.GONE);

        newsAdapter.clear();

        Log.d(LOG_TAG, "TEST: onLoadFinished............");

        if(news != null && !news.isEmpty()){
            Log.d(LOG_TAG, "TEST: News not null............");
            newsAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        Log.d(LOG_TAG, "TEST: onLoaderReset............");
        newsAdapter.clear();
    }
}

