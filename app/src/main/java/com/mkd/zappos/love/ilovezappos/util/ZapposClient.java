package com.mkd.zappos.love.ilovezappos.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mkdin on 29-01-2017.
 */

public class ZapposClient {
    private static final String BASE_URL = "https://api.zappos.com/";
    private static Retrofit retroClient = null;


    public static Retrofit getClient() {
        if (retroClient==null) {
            retroClient = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retroClient;
    }
}
