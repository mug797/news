package com.example.mkhade.newyorktimessearch.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

import static cz.msebera.android.httpclient.HttpHeaders.IF;

/**
 * Created by mkhade on 10/18/2016.
 */

//@Parcel
public class Article {
    private String webUrl;
    private String headline;
    private String thumbNail;

    public Article (JSONObject jsonObject){
        try {
            this.webUrl = jsonObject.getString("web_url");

            JSONObject headline_json = jsonObject.getJSONObject("headline");

            if(headline_json != null) {
                this.headline = headline_json.getString("main");
            } else {
                this.headline = "Read More";
            }

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if(multimedia.length() > 0) {
                JSONObject multimediaJSON = multimedia.getJSONObject(0);
                this.thumbNail = "http://www.nytimes.com/" + multimediaJSON.getString("url");
            } else {
                this.thumbNail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<Article> fromJSONArray (JSONArray array) {
        ArrayList<Article> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++){
            try {
                results.add(new Article(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    public String getWeburl() {
        return webUrl;
    }

    public void setWeburl(String weburl) {
        this.webUrl = weburl;
    }

    public String getThumbnail() {
        return thumbNail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbNail = thumbnail;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }
}
