package com.meiya.chaos.ui.activity;

import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kennyc.view.MultiStateView;
import com.meiya.chaos.R;
import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.common.base.SwipeBackActivity;
import com.meiya.chaos.model.NewsDetail;
import com.meiya.chaos.model.event.InitDetailEvent;
import com.meiya.chaos.model.event.NewsDetailEvent;
import com.meiya.chaos.utils.ChaosUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chenliang3 on 2016/5/5.
 */
public class ContentActivity extends SwipeBackActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.content_iv)
    ImageView imageView;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.content_multiStateView)
    MultiStateView multiStateView;

    public static String INTENT_CONTENT_EXTRA = "intent_content_extra";
    private int newsId;
    private String titleMsg;
    private String shareUrl;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_content;
    }

    @Override
    protected void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(onMenuItemClk);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });

        initWebView();

        newsId = getIntent().getIntExtra(INTENT_CONTENT_EXTRA, -1);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        AppService.getInstance().getNewsDetailFromDb(getTaskId(), newsId);
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        webView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        webView.getSettings().setAppCacheEnabled(true);
    }

    public void onEventMainThread(InitDetailEvent event){
        if (event.getEventResult() == Constant.Result.SUCCESS && event.getNewsDetail() != null){
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            show(event.getNewsDetail());
        }else {
            AppService.getInstance().getNewsDetail(getTaskId(), newsId);
        }
    }

    public void onEventMainThread(NewsDetailEvent event){
        if (event.getEventResult() == Constant.Result.SUCCESS && event.getNewsDetail() != null){
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            show(event.getNewsDetail());
        }else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    private void show(NewsDetail detail){
        collapsingToolbarLayout.setTitle(detail.getTitle());
        titleMsg = detail.getTitle();
        shareUrl = detail.getShare_url();

        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + detail.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        webView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);

        Glide.with(this)
                .load(detail.getImage())
                .placeholder(R.mipmap.ic_boy)
                .error(R.mipmap.ic_boy)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClk = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_favorite:

                    break;
                case R.id.action_share:
                    ChaosUtils.shareMsg(ContentActivity.this, "分享", titleMsg, shareUrl, null);
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_menu, menu);
        return true;
    }

    @OnClick(R.id.retry)
    public void retry(){
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        AppService.getInstance().getNewsDetailFromDb(getTaskId(), newsId);
    }
}
