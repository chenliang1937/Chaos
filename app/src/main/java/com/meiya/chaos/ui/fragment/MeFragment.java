package com.meiya.chaos.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.andexert.library.RippleView;
import com.baoyz.actionsheet.ActionSheet;
import com.bumptech.glide.Glide;
import com.meiya.chaos.R;
import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.common.base.BaseFragment;
import com.meiya.chaos.model.event.Event;
import com.meiya.chaos.others.GlideCircleTransform;
import com.meiya.chaos.rxmethod.RxPicFlow;
import com.meiya.chaos.rxmethod.RxVideoList;
import com.meiya.chaos.ui.activity.NavigationActivity;
import com.meiya.chaos.ui.activity.RxJavaSampleActivity;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by chenliang3 on 2016/4/11.
 */
public class MeFragment extends BaseFragment{

    @BindView(R.id.me_btn_collect)
    RippleView btn_collect;
    @BindView(R.id.me_btn_map)
    RippleView btn_map;
    @BindView(R.id.me_btn_set)
    RippleView btn_set;
    @BindView(R.id.me_clear)
    RippleView btn_clear;
    @BindView(R.id.me_photo)
    ImageView photo;
    @BindView(R.id.me_rxjava)
    RippleView btn_rxjava;
    @BindView(R.id.me_mvp)
    RippleView btn_mvp;

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyRippleCompleteLis myRippleCompleteLis = new MyRippleCompleteLis();
        btn_map.setOnRippleCompleteListener(myRippleCompleteLis);
        btn_clear.setOnRippleCompleteListener(myRippleCompleteLis);
        btn_rxjava.setOnRippleCompleteListener(myRippleCompleteLis);

        String imgPath = AppService.getPrefser().get(Constant.PRE_PHOTO_PATH, String.class, "");
        File file = new File(imgPath);
        if (!TextUtils.isEmpty(imgPath) && file != null){
            Glide.with(getActivity())
                    .load(file)
                    .transform(new GlideCircleTransform(getActivity()))
                    .error(R.mipmap.ic_boy)
                    .into(photo);
        }else {
            Glide.with(getActivity())
                    .load("http://img4.cache.netease.com/photo/0096/2014-05-04/400x400_9RDLN7A654GN0096.jpg")
                    .transform(new GlideCircleTransform(getActivity()))
                    .error(R.mipmap.ic_boy)
                    .placeholder(R.mipmap.ic_boy)
                    .crossFade()
                    .into(photo);
        }

        final FunctionConfig config = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(1)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setEnableCamera(true)
                .setCropSquare(true)
                .setForceCrop(true)
                .setForceCropEdit(true)
                .setEnablePreview(true)
                .build();
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionSheet.createBuilder(getActivity(), getActivity().getSupportFragmentManager())
                        .setCancelButtonTitle("取消")
                        .setOtherButtonTitles("拍照","打开相册")
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                switch (index){
                                    case 0:
                                        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, mOnHanlderResultCallback);
                                        break;
                                    case 1:
                                        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, config, mOnHanlderResultCallback);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private class MyRippleCompleteLis implements RippleView.OnRippleCompleteListener{

        @Override
        public void onComplete(RippleView rippleView) {
            switch (rippleView.getId()){
                case R.id.me_btn_map:
                    Intent intent = new Intent(getActivity(), NavigationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.me_clear:
                    new MaterialDialog.Builder(getActivity())
                            .content("确定清空缓存？")
                            .negativeText("取消")
                            .negativeColor(getResources().getColor(R.color.text_color))
                            .positiveText("确定")
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    RxVideoList.deleteAll();
                                    RxPicFlow.deleteAll();
                                    Snackbar.make(btn_clear, "缓存已清空", Snackbar.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                    break;
                case R.id.me_rxjava:
                    Intent rxIntent = new Intent(getActivity(), RxJavaSampleActivity.class);
                    startActivity(rxIntent);
                    break;
                case R.id.me_mvp:

                    break;
                default:
                    break;
            }
        }
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null){
                AppService.getPrefser().put(Constant.PRE_PHOTO_PATH, resultList.get(0).getPhotoPath());
                File file = new File(resultList.get(0).getPhotoPath());
                Glide.with(getActivity())
                        .load(file)
                        .transform(new GlideCircleTransform(getActivity()))
                        .error(R.mipmap.ic_boy)
                        .into(photo);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Snackbar.make(photo, errorMsg, Snackbar.LENGTH_SHORT).show();
        }
    };

    public void onEventMainThread(Event event){

    }
}
