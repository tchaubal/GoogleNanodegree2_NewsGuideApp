package com.example.tanushreechaubal.mynewsguide;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TanushreeChaubal on 5/15/18.
 */

public class NewsSearchListUtils {

        public static final String LOG_TAG = MainActivity.class.getName();


        private NewsSearchListUtils(){

        }

        public static List<NewsItem> fetchNewsData(String requestURL){

            URL url = createUrl(requestURL);
            Log.d(LOG_TAG, "TEST: fetchNewsData............"+ requestURL);

            String jsonResponse = null;
            try{
                jsonResponse = makeHttpRequest(url);
            }catch(IOException e){
                Log.e(LOG_TAG, "Problem making HTTP request", e);
            }

            List<NewsItem> newsItems = extractNewsDetailsFromJSON(jsonResponse);
            return newsItems;
        }

        public static List<NewsItem> extractNewsDetailsFromJSON(String NewsJSON){

            if (TextUtils.isEmpty(NewsJSON)){
                return null;
            }

            List<NewsItem> newsItems = new ArrayList<>();

            try{
                Log.e(LOG_TAG, "TEST: This is NEWSJSON:  "+ NewsJSON);
                JSONObject root = new JSONObject(NewsJSON);
                JSONObject news = root.getJSONObject("response");
                JSONArray news_array = news.getJSONArray("results");

                for(int i=0; i<news_array.length();i++){
                    JSONObject newsObject = news_array.getJSONObject(i);
                    String title = newsObject.getString("webTitle");
                    String section = newsObject.getString("sectionName");
                    String dateAndTime = newsObject.getString("webPublicationDate");
                    String url = newsObject.getString("webUrl");

                    Log.e(LOG_TAG, "TEST: Title of article:  "+ title);
                    Log.e(LOG_TAG, "TEST: Section of article:  "+ section);

                    Log.e(LOG_TAG, "TEST: date of article:  "+ dateAndTime);

                    Log.e(LOG_TAG, "TEST: url of article:  "+ url);


                    if(news_array.getJSONObject(i).has("tags")){
                        JSONArray authorArray = newsObject.getJSONArray("tags");
                        for(int j=0;j<authorArray.length();j++){
                            JSONObject authorObject = authorArray.getJSONObject(j);
                            String authorName = authorObject.getString("webTitle");
                            Log.e(LOG_TAG, "TEST: Author of article:  "+ authorName);

                            NewsItem newsList = new NewsItem(title, section, dateAndTime, authorName, url);
                            newsItems.add(newsList);
                        }
                    } else {
                        NewsItem newsList = new NewsItem(title, section, dateAndTime, url);
                        newsItems.add(newsList);
                    }
                }

            } catch(JSONException e){
                Log.e("NewsSearchListUtils", "Problem parsing the news JSON results", e);
            }
            return newsItems;
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private static URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Problem building the URL ", e);
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private static String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            // If the URL is null, then return early.
            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                //Log.e(LOG_TAG, "TEST: this is the URL:    "+url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // Closing the input stream could throw an IOException, which is why
                    // the makeHttpRequest(URL url) method signature specifies than an IOException
                    // could be thrown.
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private static String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }
}
