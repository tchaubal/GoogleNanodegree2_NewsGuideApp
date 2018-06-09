package com.example.tanushreechaubal.mynewsguide;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by TanushreeChaubal on 5/15/18.
 */

public class NewsAdapter extends ArrayAdapter<NewsItem> {

    public NewsAdapter(Activity context, ArrayList<NewsItem> info) {
        super(context, 0, info);
    }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View listItemView = convertView;
            if(listItemView == null){
                listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_list_item, parent,false);
            }

            NewsItem currentInfo = getItem(position);

            TextView title = listItemView.findViewById(R.id.title_text_view);
            title.setText(currentInfo.getArticleTitle());

            TextView section = listItemView.findViewById(R.id.section_text_view);
            section.setText(currentInfo.getSectionName());

            TextView dateOfPublication = listItemView.findViewById(R.id.date_text_view);
            dateOfPublication.setText(currentInfo.getPublicationDate());

            TextView authorName = listItemView.findViewById(R.id.author_text_view);
            authorName.setText(currentInfo.getAuthorName());

            return listItemView;
    }
}
