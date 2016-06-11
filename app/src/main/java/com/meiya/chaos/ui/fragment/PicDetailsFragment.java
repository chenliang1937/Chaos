package com.meiya.chaos.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.meiya.chaos.R;
import com.meiya.chaos.common.base.BaseFragment;
import com.meiya.chaos.model.event.Event;

import butterknife.BindView;

/**
 * Created by chenliang3 on 2016/5/21.
 */
public class PicDetailsFragment extends BaseFragment {

    @BindView(R.id.pdi_imageview)
    ImageView pdi_imageview;
    @BindView(R.id.pdi_layout1)
    RelativeLayout pdi_layout1;
    @BindView(R.id.pdi_layout2)
    RelativeLayout pdi_layout2;
    @BindView(R.id.pdi_count)
    TextView pdi_count;
    @BindView(R.id.pdi_back)
    ImageView pdi_back;
    @BindView(R.id.pdi_title)
    TextView pdi_title;
    @BindView(R.id.pdi_content)
    TextView pdi_content;

    private String title;
    private String imgUrl;
    private String content;
    private String count;
    private AlphaAnimation animation;
    private boolean isTrue = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.picdetails_item, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = getArguments().getString("TITLE");
        imgUrl = getArguments().getString("IMGURL");
        content = getArguments().getString("CONTENT");
        count = getArguments().getString("COUNT");

        pdi_title.setText(title);
        pdi_content.setText(content);
        pdi_count.setText(count);
        Glide.with(getActivity())
                .load(imgUrl)
                .crossFade()
                .into(pdi_imageview);

        pdi_imageview.setOnClickListener(new MyOnclickListener());
        pdi_back.setOnClickListener(new MyOnclickListener());
    }

    private class MyOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.pdi_imageview:
                    if (isTrue){
                        isTrue = false;
                        setHideAnimation(pdi_layout1, 1000);
                        setHideAnimation(pdi_layout2, 1000);
                    }else {
                        isTrue = true;
                        setShowAnimation(pdi_layout1, 1000);
                        setShowAnimation(pdi_layout2, 1000);
                    }
                    break;
                case R.id.pdi_back:
                    getActivity().finish();
                    break;
            }
        }
    }

    private void setHideAnimation(View view, int duration){
        if (null == view || duration < 0){
            return;
        }
        animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    private void setShowAnimation(View view, int duration){
        if (null == view || duration < 0){
            return;
        }
        animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public void onEventMainThread(Event event){

    }
}
