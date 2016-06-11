package com.meiya.chaos.ui.activity;

import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.meiya.chaos.R;
import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.common.base.BaseActivity;
import com.meiya.chaos.model.event.Event;

import butterknife.BindView;

/**
 * Created by chenliang3 on 2016/4/20.
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.splash_image)
    ImageView imageView;

    private ScaleAnimation scaleAnim;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews() {
        String url = AppService.getPrefser().get(Constant.PRE_SPLASH_IMAGEURL, String.class, "");
        loadImg(url);
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.FADE;
    }

    public void onEventMainThread(Event event){

    }

    private void loadImg(String url){
        Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.ic_joker)
                .error(R.mipmap.ic_joker)
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        imgAnim(2000);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        imgAnim(3000);
                        return false;
                    }
                })
                .into(imageView);
    }

    private void imgAnim(int duration){
        scaleAnim =  new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(duration);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(scaleAnim);
    }

}
