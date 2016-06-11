package com.meiya.chaos.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.kennyc.view.MultiStateView;
import com.meiya.chaos.R;
import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.base.BaseActivity;
import com.meiya.chaos.model.PicDetails;
import com.meiya.chaos.model.PictureFlow;
import com.meiya.chaos.model.event.Event;
import com.meiya.chaos.ui.fragment.PicDetailsFragment;
import com.meiya.chaos.utils.SplitNetStringUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by chenliang3 on 2016/5/21.
 */
public class PicDetailsActivity extends BaseActivity {

    @BindView(R.id.pd_viewpager)
    ViewPager viewPager;
    @BindView(R.id.pd_multiStateView)
    MultiStateView multiStateView;

    private List<PicDetails> picDetailsList = new ArrayList<>();
    private List<Fragment> picFragmentList = new ArrayList<>();
    private Call call;
    private PictureFlow pictureFlow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_picdetails;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    @Override
    protected void initViews() {
        pictureFlow = (PictureFlow) getIntent().getSerializableExtra("PICTUREFLOW");

        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        Request request = new Request.Builder()
                .url(pictureFlow.getSeturl())
                .build();
        call = AppService.getInstance().getOkhttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result = response.body().string();
                try{
                    picDetailsList = SplitNetStringUtils.getPicDetailsList(result);
                }catch (Exception e){
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        takeControl(picDetailsList);
                    }
                });

            }
        });
    }

    private void takeControl(List<PicDetails> picDetailsList) {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);

        if (picDetailsList != null && picDetailsList.size() > 0){
            int size = picDetailsList.size();
            for (int i = 0; i<size; i++){
                PicDetailsFragment fragment = new PicDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("TITLE", picDetailsList.get(i).getTitle());
                bundle.putString("IMGURL", picDetailsList.get(i).getImgUrl());
                bundle.putString("CONTENT", picDetailsList.get(i).getContent());
                bundle.putString("COUNT", (i+1)+"/"+size);
                fragment.setArguments(bundle);
                picFragmentList.add(fragment);
            }
        }else {
            int size = pictureFlow.getPics().size();
            for (int i = 0; i < size; i++) {
                PicDetailsFragment fragment1 = new PicDetailsFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("TITLE", pictureFlow.getSetname());
                bundle1.putString("IMGURL", pictureFlow.getPics().get(i));
                bundle1.putString("CONTENT", pictureFlow.getDesc());
                bundle1.putString("COUNT", (i + 1) + "/" + size);
                fragment1.setArguments(bundle1);
                picFragmentList.add(fragment1);
            }
        }
        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter{

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return picFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return picFragmentList.size();
        }
    }

    public void onEventMainThread(Event event){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        call.cancel();
    }
}
