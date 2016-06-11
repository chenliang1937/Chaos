package com.meiya.chaos.ui.activity;

import android.content.Intent;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kennyc.view.MultiStateView;
import com.meiya.chaos.R;
import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.common.base.BaseActivity;
import com.meiya.chaos.model.SogouImage;
import com.meiya.chaos.model.event.SogouImageEvent;
import com.meiya.chaos.ui.adapter.SearchflowView;
import com.meiya.refreshandloadmorelib.loadmore.DefaultFootItem;
import com.meiya.refreshandloadmorelib.loadmore.OnLoadMoreListener;
import com.meiya.refreshandloadmorelib.loadmore.RecyclerViewWithFooter;
import com.meiya.refreshandloadmorelib.refresh.MaterialRefreshLayout;
import com.meiya.refreshandloadmorelib.refresh.MaterialRefreshListener;

import butterknife.BindView;
import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter;
import io.nlopez.smartadapters.utils.ViewEventListener;

/**
 * Created by chenliang3 on 2016/6/4.
 */
public class SearchActivity extends BaseActivity implements ViewEventListener<SogouImage.theItems> {

    @BindView(R.id.search_multiStateView)
    MultiStateView multiStateView;
    @BindView(R.id.search_toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_refresh)
    MaterialRefreshLayout refreshLayout;
    @BindView(R.id.search_recycler_view)
    RecyclerViewWithFooter recyclerView;

    private RecyclerMultiAdapter adapter;
    private int page;
    private String queryStr;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    @Override
    protected void initViews() {
        queryStr = getIntent().getStringExtra("QUERY");

        toolbar.setTitle(queryStr);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = SmartAdapter.empty()
                .map(SogouImage.theItems.class, SearchflowView.class)
                .listener(this)
                .into(recyclerView);

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                page = 1;
                AppService.getInstance().getSogouImg(getTaskId(), "ajax", "result", queryStr, page);
            }
        });
        recyclerView.setFootItem(new DefaultFootItem());
        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                AppService.getInstance().getMoreSogouImg(getTaskId(), "ajax", "result", queryStr, page);
            }
        });

        refreshLayout.autoRefresh();
    }

    public void onEventMainThread(SogouImageEvent event){
        refreshLayout.finishRefresh();
        if (event.getEventResult() == Constant.Result.SUCCESS && event.getSogouImages() != null){
            if (event.getGetWay() == Constant.GetWay.REFRESH){
                if (event.getSogouImages().getItems().size() == 0){
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                }else {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    adapter.clearItems();
                    adapter.addItems(event.getSogouImages().getItems());
                }
            }else if (event.getGetWay() == Constant.GetWay.LOADMORE){
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                adapter.addItems(event.getSogouImages().getItems());
            }
        }else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    @Override
    public void onViewEvent(int actionId, SogouImage.theItems item, int position, View view) {
        if (actionId == Constant.SEARCH_IMAGE_CLICKED){
            Intent intent = new Intent(SearchActivity.this, SpaceImageActivity.class);
            intent.putExtra("imageUrl", item.getPic_url());
            intent.putExtra("imageTitle", item.getTitle());
            startActivity(intent);
        }
    }
}
