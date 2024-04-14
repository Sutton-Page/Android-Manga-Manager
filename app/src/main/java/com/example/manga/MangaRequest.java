package com.example.manga;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MangaRequest {


    private String baseUrl = "https://api.mangadex.org/manga?title=%s&limit=4&contentRating[]=safe&contentRating[]=suggestive&contentRating[]=erotica&includes[]=cover_art&order[relevance]=desc";
    private String mangaTitle;

    private String imageBaseUrl = "https://mangadex.org/covers/%s/%s";
    private String data;

    private ArrayList<String> imageUrls;



    private int maxResults;



    private String coverAPI = "https://api.mangadex.org/cover?order[volume]=asc&manga[]=%s&limit=100&offset=0";





    private String getFirstId(String content) throws JSONException {

        JSONObject base = new JSONObject(content);

        JSONArray data = base.getJSONArray("data");

        JSONObject inner = data.getJSONObject(0);

        return inner.getString("id");


    }

    private ArrayList<String> parseComicCovers(String mangaId, String content) throws JSONException {

        JSONObject base = new JSONObject(content);

        JSONArray data = base.getJSONArray("data");

        ArrayList<String> coverUrls = new ArrayList<>();

        for(int i = 0; i < data.length(); i++){

            String imageStub = data.getJSONObject(i).getJSONObject("attributes").getString("fileName");

            coverUrls.add(String.format(this.imageBaseUrl,mangaId,imageStub));

        }

        return coverUrls;



    }


    private String pullData(String url) throws Exception{

        //String url = String.format(baseUrl,this.mangaTitle);

        URL request = new URL(url);
        HttpURLConnection con = (HttpURLConnection) request.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:81.0) Gecko/20100101 Firefox/81.0");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        String inputLine;

        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        return content.toString();




    }


    public ArrayList<String> parseData(String content) throws JSONException {

        ArrayList<String> holder = new ArrayList<>();

        JSONObject base = new JSONObject(content);

        JSONArray data = base.getJSONArray("data");

        for(int i = 0; i < data.length(); i++){

            JSONObject inner = data.getJSONObject(i);

            String mangaId = inner.getString("id");

            JSONArray rel = inner.getJSONArray("relationships");

            for(int j = 0; j < rel.length(); j++){

                JSONObject item = rel.getJSONObject(j);

                String type = item.getString("type");

                if(type.equals("cover_art")){



                    String imageStub = item.getJSONObject("attributes").getString("fileName");

                    //Log.d("retrieve", String.format(this.imageBaseUrl, mangaId, imageStub));

                    holder.add(String.format(this.imageBaseUrl,mangaId,imageStub));

                    break;


                }
            }



        }

        return holder;


    }

    private void searchListCovers() throws Exception {

        String result = this.pullData(String.format(baseUrl,this.mangaTitle));

        String mangaId = this.getFirstId(result);


        String coverResult = this.pullData(String.format(this.coverAPI,mangaId));

        this.imageUrls  = this.parseComicCovers(mangaId,coverResult);



    }



    public MangaRequest (String mangaTitle, int maxResults) throws Exception {

        this.mangaTitle = mangaTitle;

        String result =this.pullData(String.format(baseUrl,this.mangaTitle));

        this.imageUrls = this.parseData(result);

        this.maxResults = maxResults;




    }

    public ArrayList<String> getMangaUrl(){

        return this.imageUrls;
    }



}


