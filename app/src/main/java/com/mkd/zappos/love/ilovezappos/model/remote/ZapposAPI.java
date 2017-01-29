package com.mkd.zappos.love.ilovezappos.model.remote;

import com.mkd.zappos.love.ilovezappos.model.data.JSONResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mkdin on 29-01-2017.
 */

public interface ZapposAPI {
    @GET("Search")
    Call<JSONResponse> getProductDetails(@Query("key") String apiKey, @Query("term") String searchKey);
}
