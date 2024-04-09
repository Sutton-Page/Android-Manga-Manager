package com.example.manga;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MangaRequest {


    private String baseUrl = "https://api.mangadex.org/manga?title=%s&limit=1&contentRating[]=safe&contentRating[]=suggestive&contentRating[]=erotica&includes[]=cover_art&order[relevance]=desc";
    private String mangaTitle;

    private String imageBaseUrl = "https://mangadex.org/covers/%s/%s";
    private String data;

    private String imageUrl;


    private void pullData() throws Exception{

        String url = String.format(baseUrl,this.mangaTitle);

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

        this.data = content.toString();




    }


    private String parseData() throws JSONException {

        JSONObject base = new JSONObject(this.data);

        JSONArray data = base.getJSONArray("data");

        String mangaId;

        String imageStub = "";

        if(data.length() > 0){

            JSONObject inner = data.getJSONObject(0);

            mangaId = inner.getString("id");

            JSONArray rel = inner.getJSONArray("relationships");

            for(int i = 0; i < rel.length(); i++){

                JSONObject item = rel.getJSONObject(i);

                String type = item.getString("type");

                if(type.equals("cover_art")){

                    imageStub = item.getJSONObject("attributes").getString("fileName");


                }
            }

            return String.format(this.imageBaseUrl,mangaId,imageStub);




        } else{

            return "";
        }

    }


    public MangaRequest (String mangaTitle) throws Exception {

        this.mangaTitle = mangaTitle;

        this.pullData();

        this.imageUrl = this.parseData();




    }

    public String getMangaUrl(){

        return this.imageUrl;
    }



}


