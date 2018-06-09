package com.example.tanushreechaubal.mynewsguide;

/**
 * Created by TanushreeChaubal on 5/15/18.
 */

public class NewsItem {

    private String articleTitle;
    private String sectionName;
    private String publicationDate;
    private String authorName;
    private String webURL;

    public NewsItem(String title, String section, String publicationDt, String author, String url){
        articleTitle = title;
        sectionName = section;
        publicationDate = publicationDt;
        authorName = author;
        webURL = url;
    }

    public NewsItem(String title, String section, String publicationDt, String url){
        articleTitle = title;
        sectionName = section;
        publicationDate = publicationDt;
        webURL = url;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getWebURL() {
        return webURL;
    }

    public String getSectionName() {
        return sectionName;
    }


    public String getPublicationDate() {
        return publicationDate;
    }

    public String getAuthorName() {
        return authorName;
    }

}
