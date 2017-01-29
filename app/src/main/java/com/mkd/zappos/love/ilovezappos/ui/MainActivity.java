package com.mkd.zappos.love.ilovezappos.ui;

import android.databinding.DataBindingUtil;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mkd.zappos.love.ilovezappos.BuildConfig;
import com.mkd.zappos.love.ilovezappos.R;
import com.mkd.zappos.love.ilovezappos.model.data.JSONResponse;
import com.mkd.zappos.love.ilovezappos.model.data.Product;
import com.mkd.zappos.love.ilovezappos.model.remote.ZapposAPI;
import com.mkd.zappos.love.ilovezappos.util.ZapposClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (BuildConfig.KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from Zappos!", Toast.LENGTH_LONG).show();
            return;
        }
        ZapposAPI apiService =
                ZapposClient.getClient().create(ZapposAPI.class);

        Call<JSONResponse> call = apiService.getProductDetails(BuildConfig.KEY, "nike");
        Log.v(TAG, call.request().url().toString());
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse>call, Response<JSONResponse> response) {
                // Log.v(TAG, response.message());
                List<Product> productList = response.body().getResults();
                Log.d(TAG, "Number of movies received: " + productList.size());
                for(int i=0; i<productList.size(); i++) {
                    Log.d(TAG, productList.get(i).getProductName());
                }
            }

            @Override
            public void onFailure(Call<JSONResponse>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
