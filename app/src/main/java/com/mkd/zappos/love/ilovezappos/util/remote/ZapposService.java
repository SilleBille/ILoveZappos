package com.mkd.zappos.love.ilovezappos.util.remote;

import android.content.Context;
import android.util.Log;

import com.mkd.zappos.love.ilovezappos.BuildConfig;
import com.mkd.zappos.love.ilovezappos.model.data.JSONResponse;
import com.mkd.zappos.love.ilovezappos.model.data.Product;
import com.mkd.zappos.love.ilovezappos.model.remote.ZapposAPI;
import com.mkd.zappos.love.ilovezappos.util.callback.OnProductResult;
import com.mkd.zappos.love.ilovezappos.util.local.ZapposClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mkdin on 30-01-2017.
 */

public class ZapposService {
    private static final String TAG = ZapposService.class.getSimpleName();
    ZapposAPI apiService;
    Context context;
    OnProductResult listener;

    public ZapposService(Context context) {
        apiService = ZapposClient.getClient().create(ZapposAPI.class);
        this.context = context;
    }

    public void setUpdateListener(OnProductResult listener) {
        this.listener = listener;
    }

    public void getProductList(String query) {
        if (BuildConfig.KEY.isEmpty() || query.isEmpty()) {
            return;
        }

        Call<JSONResponse> call = apiService.getProductDetails(BuildConfig.KEY, query);

        // Log the URL that has been generated
        Log.v(TAG, call.request().url().toString());

        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                List<Product> productList = response.body().getResults();
                Log.v(TAG, "Number of products received: " + productList.size());
                listener.onResultReceived(productList);
            }

                @Override
                public void onFailure (Call < JSONResponse > call, Throwable t){
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }
            }

            );

        }


    }
