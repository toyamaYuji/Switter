package com.excercise.nns.androidex.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.excercise.nns.androidex.R;
import com.excercise.nns.androidex.contract.HomeContract;
import com.excercise.nns.androidex.contract.TimelineFragmentCallback;
import com.excercise.nns.androidex.databinding.ActivityHomeBinding;
import com.excercise.nns.androidex.view.fragment.TimelineFragment;
import com.excercise.nns.androidex.viewmodel.HomeViewModel;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by nns on 2017/06/12.
 */

public class HomeActivity extends BaseActivity implements HomeContract, TimelineFragmentCallback {
    private ActivityHomeBinding binding;

    public static void start(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlowManager.init(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        HomeViewModel viewModel = new HomeViewModel(this);
        binding.setViewmodel(viewModel);
        // toolbar setup
        setupToolbar(binding.toolbar);
        // replace fragment
        TimelineFragment fragment = TimelineFragment.newInstance();
        replaceFragment(R.id.container, fragment);
    }

    @Override
    public void onStartOAuth() {
        OAuthActivity.start(this);
        finish();
    }

    @Override
    public void onStartTweet() {
        TweetActivity.start(this, null, 0);
    }

    @Override
    public void onStartAbout() {
        AboutAppActivity.start(this);
    }

    @Override
    public void loadingTimeline() {
        binding.setIsloaded(true);
    }

    @Override
    public void finishedTimeline() {
        binding.setIsloaded(false);
    }
}
