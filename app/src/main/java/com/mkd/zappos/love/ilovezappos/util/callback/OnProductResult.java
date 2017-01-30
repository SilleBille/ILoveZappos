package com.mkd.zappos.love.ilovezappos.util.callback;

import com.mkd.zappos.love.ilovezappos.model.data.JSONResponse;
import com.mkd.zappos.love.ilovezappos.model.data.Product;

import java.util.List;

/**
 * Created by mkdin on 30-01-2017.
 */

public interface OnProductResult {
    public void onResultReceived(List<Product> searchResponse);

}
