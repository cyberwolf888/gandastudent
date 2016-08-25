package com.gandaedukasi.gandaedukasi.models;

/**
 * Created by Karen on 7/31/2016.
 */

public class Article {
    public String id_article;
    public String title;
    public String content;
    public String cover;
    public String created_at;

    public Article(String id_article, String title, String content, String cover, String created_at){
        this.id_article = id_article;
        this.title = title;
        this.content = content;
        this.cover = cover;
        this.created_at = created_at;
    }
}
