package com.example.manga;

public class MangaItem {

    String title;
    String imageUrl;
    int id;

    public MangaItem(){


    }

    public MangaItem(int id,String title, String imageUrl){

        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }


}
