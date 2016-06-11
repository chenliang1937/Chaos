package com.meiya.chaos.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.artjimlop.altex.AltexImageDownloader;
import com.bumptech.glide.Glide;
import com.meiya.chaos.R;
import com.meiya.chaos.common.base.BaseActivity;
import com.meiya.chaos.model.event.Event;
import com.meiya.chaos.widget.PinchImageView;

import butterknife.BindView;

/**
 * Created by chenliang3 on 2016/6/4.
 */
public class SpaceImageActivity extends BaseActivity {

    @BindView(R.id.spaceImage_iv)
    PinchImageView imageView;
    @BindView(R.id.spaceImage_toolbar)
    Toolbar toolbar;

    private String imageUrl;
    private String title;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_spaceimage;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    @Override
    protected void initViews() {
        imageUrl = getIntent().getStringExtra("imageUrl");
        title = getIntent().getStringExtra("imageTitle");

        toolbar.setTitle(title);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(onMenuItemClk);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(this)
                .load(imageUrl)
                .error(R.mipmap.ic_boy)
                .crossFade()
                .into(imageView);
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClk = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.sm_download:
                    new MaterialDialog.Builder(SpaceImageActivity.this)
                            .title("下载")
                            .content("确定下载图片？")
                            .positiveText("确定")
                            .negativeText("取消")
                            .negativeColor(getResources().getColor(R.color.text_color))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    AltexImageDownloader.writeToDisk(SpaceImageActivity.this, imageUrl, "Chaos");//"Chaos"表示被保存文件夹
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.space_menu, menu);
        return true;
    }

    public void onEventMainThread(Event event){

    }
}
