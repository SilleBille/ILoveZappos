package com.mkd.zappos.love.ilovezappos.ui;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mkd.zappos.love.ilovezappos.BuildConfig;
import com.mkd.zappos.love.ilovezappos.R;
import com.mkd.zappos.love.ilovezappos.databinding.ActivityMainBinding;
import com.mkd.zappos.love.ilovezappos.model.data.JSONResponse;
import com.mkd.zappos.love.ilovezappos.model.data.Product;
import com.mkd.zappos.love.ilovezappos.model.remote.ZapposAPI;
import com.mkd.zappos.love.ilovezappos.util.callback.OnProductResult;
import com.mkd.zappos.love.ilovezappos.util.local.ZapposClient;
import com.mkd.zappos.love.ilovezappos.util.remote.ZapposService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText etSearchBox;
    private ImageButton btSearch;
    ZapposService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etSearchBox = (EditText) findViewById(R.id.et_search);
        btSearch = (ImageButton) findViewById(R.id.bt_search);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        service = new ZapposService(getApplicationContext());
        service.setUpdateListener(new OnProductResult() {
            @Override
            public void onResultReceived(List<Product> searchResponse) {
                if (searchResponse.size() > 0) {
                    // Show only the first result! We can use adapters to fill RecyclerView or ListView here instead!
                    binding.mainContent.setProduct(searchResponse.get(0));
                    if (!searchResponse.get(0).getPercentOff().equals("0%"))
                        binding.mainContent.txtOriginalPrice.setPaintFlags(
                                binding.mainContent.txtOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else
                    Toast.makeText(getApplicationContext(), "No results found! Try again", Toast.LENGTH_SHORT).show();
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator sla = AnimatorInflater.loadStateListAnimator(
                    getApplicationContext(), R.anim.card_state_list_anim);
            binding.mainContent.cv.setStateListAnimator(sla);
        }


    }

    public void searchProduct(View view) {
        if (service != null) {
            service.getProductList(etSearchBox.getText().toString());
            etSearchBox.setText("");
        }
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
