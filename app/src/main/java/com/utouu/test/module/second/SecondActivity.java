package com.utouu.test.module.second;

import android.graphics.Color;
import android.os.Bundle;

import com.marno.mbasiclib.basic.activity.MBasicSwipeActivity;
import com.utouu.test.R;

/**
 * Created by marno on 2016/8/23/15:20.
 * 目标页面
 */
public class SecondActivity extends MBasicSwipeActivity  {

    @Override
    protected int getLayout() {
        return R.layout.activity_second;
    }

    @Override
    protected int getStatusBarColor() {
        return Color.BLACK;
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

}
