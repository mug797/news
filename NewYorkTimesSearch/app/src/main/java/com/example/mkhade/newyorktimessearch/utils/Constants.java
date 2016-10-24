package com.example.mkhade.newyorktimessearch.utils;

/**
 * Created by mkhade on 10/18/2016.
 */

public class Constants {
    private static final String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    private static final String api_key = "2039f7a1618c47ad95bedda4f9bf0201";
    private static final int FRAGMENTS_STATUS_CODE_RESULT = 300;


    public static final int RESULT_COLS = 4;


    public static String getUrl() {
        return url;
    }

    public static String getApi_key() {
        return api_key;
    }

    public static int getFragmentsStatusCodeResult() {
        return FRAGMENTS_STATUS_CODE_RESULT;
    }

}
