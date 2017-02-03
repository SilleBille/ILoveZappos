package com.mkd.zappos.love.ilovezappos.ui;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mkd.zappos.love.ilovezappos.R;
import com.mkd.zappos.love.ilovezappos.databinding.ActivityMainBinding;
import com.mkd.zappos.love.ilovezappos.model.data.Product;
import com.mkd.zappos.love.ilovezappos.ui.customwidget.CustomSearchButton;
import com.mkd.zappos.love.ilovezappos.util.callback.OnProductResult;
import com.mkd.zappos.love.ilovezappos.util.local.UtilityMethods;
import com.mkd.zappos.love.ilovezappos.util.remote.ZapposService;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText etSearchBox;
    private CustomSearchButton btSearch;
    static final String STATE_PRODUCT = "product";
    static final String ADDED_TO_CART = "addedToCart";
    Product productLoaded;
    boolean addedToCart = false;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etSearchBox = binding.mainContent.etSearch;
        btSearch = binding.mainContent.btSearch;
        fab = binding.fab;
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(binding.fab);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                    if (addedToCart) {
                        AnimatedVectorDrawable tickToAdd =
                                (AnimatedVectorDrawable) getDrawable(R.drawable.avd_tick_to_add);
                        binding.fab.setImageDrawable(tickToAdd);
                        tickToAdd.start();


                        // When product is removed from cart, do corresponding action!
                    } else {
                        AnimatedVectorDrawable addToTick =
                                (AnimatedVectorDrawable) getDrawable(R.drawable.avd_add_to_tick);
                        binding.fab.setImageDrawable(addToTick);
                        addToTick.start();

                        // When product is added in card, do corresponding action!
                    }
                } else {
                    if (addedToCart) {
                        binding.fab.setImageResource(R.drawable.ic_add_white_24dp);
                    } else {
                        binding.fab.setImageResource(R.drawable.ic_check_white_24dp);
                    }
                }
                if (addedToCart) {
                    addedToCart = false;
                    Snackbar.make(view, "Removed from cart!", Snackbar.LENGTH_SHORT).show();
                } else{
                    addedToCart = true;
                    Snackbar.make(view, "Added to cart!", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
        final ZapposService service = new ZapposService(getApplicationContext());
        service.setUpdateListener(new OnProductResult() {
            @Override
            public void onResultReceived(List<Product> searchResponse) {
                if (searchResponse.size() > 0) {
                    // Since a product is found, we display the ADD to CARD button!
                    // We can monitor the product ID here and then use it for checking out!
                    fab.setVisibility(View.VISIBLE);
                    updateProductDetails(searchResponse.get(0), binding);
                } else {
                    // Since we don't find the product, remove the card and Add to cart FAB!
                    fab.setVisibility(View.INVISIBLE);
                    binding.mainContent.setProduct(null);
                    productLoaded = null;
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.err_no_results), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator sla = AnimatorInflater.loadStateListAnimator(
                    getApplicationContext(), R.animator.card_state_list_anim);
            binding.mainContent.cv.setStateListAnimator(sla);
        }
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilityMethods.isNetworkAvailable(getApplicationContext()))
                    performSearch(service);
                else
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.err_no_internet), Toast.LENGTH_SHORT).show();
            }
        });
        etSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(service);

                    return true;
                }
                return false;
            }
        });

        /* Restore the product if a saved instance is found! */
        if (savedInstanceState != null) {
            updateProductDetails((Product) savedInstanceState.get(STATE_PRODUCT),
                    binding);
            addedToCart = savedInstanceState.getBoolean(ADDED_TO_CART);

        }
        showAddedToCart();

    }

    private void showAddedToCart() {
        if (productLoaded != null) {
            int d = addedToCart ? R.drawable.ic_check_white_24dp
                    : R.drawable.ic_add_white_24dp;
            fab.setImageResource(d);
            fab.setVisibility(View.VISIBLE);
        } else
            fab.setVisibility(View.INVISIBLE);
    }

    private void performSearch(ZapposService service) {
        String searchQuery = etSearchBox.getText().toString().trim();
        if (service != null && !searchQuery.isEmpty()) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(etSearchBox.getWindowToken(), 0);
            service.getProductList(searchQuery);
        }
    }

    /* Save the product object before destroying*/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (productLoaded != null) {
            outState.putParcelable(STATE_PRODUCT, productLoaded);
            outState.putBoolean(ADDED_TO_CART, addedToCart);
        }
        super.onSaveInstanceState(outState);
    }

    private void updateProductDetails(final Product product, ActivityMainBinding binding) {
        if (product != null) {
            productLoaded = product;
            // Show only the first result! We can use adapters to fill RecyclerView or ListView here instead!
            binding.mainContent.setProduct(product);
            binding.mainContent.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!product.getProductUrl().isEmpty()) {
                        Uri uri = Uri.parse(product.getProductUrl()); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }
            });
            if (!product.getPercentOff().equals("0%"))
                binding.mainContent.txtOriginalPrice.setPaintFlags(
                        binding.mainContent.txtOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }
    }
}
