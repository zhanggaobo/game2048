package com.zhanggb.game2048;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

/**
 * @author zhanggaobo
 * @since 02/09/2015
 */
public abstract class BaseActivity extends Activity {

    protected Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = this;
        doOnCreate();
    }

    protected abstract void doOnCreate();
}