package com.meiya.chaos.ui.fragment;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kennyc.view.MultiStateView;
import com.meiya.chaos.R;
import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.common.base.BaseFragment;
import com.meiya.chaos.model.NewsLatest;
import com.meiya.chaos.model.event.BeforeNewsEvent;
import com.meiya.chaos.model.event.InitNewsEvent;
import com.meiya.chaos.model.event.LastNewsEvent;
import com.meiya.chaos.ui.activity.ContentActivity;
import com.meiya.chaos.ui.adapter.DateItemView;
import com.meiya.chaos.ui.adapter.LastNewsItemView;
import com.meiya.chaos.utils.NetWorkUtils;
import com.meiya.refreshandloadmorelib.loadmore.DefaultFootItem;
import com.meiya.refreshandloadmorelib.loadmore.OnLoadMoreListener;
import com.meiya.refreshandloadmorelib.loadmore.RecyclerViewWithFooter;
import com.meiya.refreshandloadmorelib.refresh.MaterialRefreshLayout;
import com.meiya.refreshandloadmorelib.refresh.MaterialRefreshListener;

import butterknife.BindView;
import butterknife.OnClick;
import io.nlopez.smartadapters.SmartAdapter;
import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter;
import io.nlopez.smartadapters.utils.ViewEventListener;

/**
 * Created by chenliang3 on 2016/4/11.
 */
public class RecommendFragment extends BaseFragment implements ViewEventListener<NewsLatest.Stories> {

    @BindView(R.id.multiStateView)
    MultiStateView multiStateView;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerViewWithFooter recyclerView;

    private RecyclerMultiAdapter adapter;
    private boolean isPrepared;
    private String date;
    private int tmpDate;
    private boolean isFirst = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = SmartAdapter.empty()
                .map(NewsLatest.class, DateItemView.class)
                .map(NewsLatest.Stories.class, LastNewsItemView.class)
                .listener(this)
                .into(recyclerView);

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //刷新
                AppService.getInstance().getLastNews(getActivity().getTaskId());
            }
        });
        recyclerView.setFootItem(new DefaultFootItem());
        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //加载
                AppService.getInstance().getBeforeNews(getActivity().getTaskId(), String.valueOf(tmpDate--));
            }
        });

        isPrepared = true;
//        lazyLoad();
        AppService.getInstance().getNewFromDb(getActivity().getTaskId());

        if (isFirst && NetWorkUtils.getCurrentNetworkState(getActivity()) == NetworkInfo.State.CONNECTED && NetWorkUtils.isWifiByType(getActivity())){
            isFirst = false;
            refreshLayout.autoRefresh();
        }
    }

//    @Override
//    protected void lazyLoad() {
//        if (!isPrepared || !isVisible){
//            return;
//        }
//
//        if (!canLoadData(multiStateView, adapter)){
//            return;
//        }
//
//        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
//        refreshLayout.autoRefresh();
//    }

    @Override
    public void onViewEvent(int actionId, NewsLatest.Stories stories, int position, View view) {
        if (actionId == Constant.CONTENT_CLICKED){
            Intent intent = new Intent(getActivity(), ContentActivity.class);
            intent.putExtra(ContentActivity.INTENT_CONTENT_EXTRA, stories.getId());
            startActivity(intent);
        }
    }

    public void onEventMainThread(LastNewsEvent event){
        refreshLayout.finishRefresh();
        if (event != null && event.getEventResult() == Constant.Result.SUCCESS){
            if (event.getNewsLatest().getStories().size() == 0){
                multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }else {
                adapter.clearItems();
                date = event.getNewsLatest().getDate();
                if (!TextUtils.isEmpty(date)){
                    tmpDate = Integer.parseInt(date);
                }
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                adapter.addItem(event.getNewsLatest());
                adapter.addItems(event.getNewsLatest().getStories());
            }
        }else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    public void onEventMainThread(InitNewsEvent event){
        if(event.getEventResult() == Constant.Result.SUCCESS && event.getNewsLatest() != null && event.getNewsLatest().getStories().size() > 0){
            date = event.getNewsLatest().getDate();
            if (!TextUtils.isEmpty(date)){
                tmpDate = Integer.parseInt(date);
            }
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            adapter.addItem(event.getNewsLatest());
            adapter.addItems(event.getNewsLatest().getStories());
        }else {
//            lazyLoad();
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            refreshLayout.autoRefresh();
        }
    }

    public void onEventMainThread(BeforeNewsEvent event){
        refreshLayout.finishRefreshLoadMore();
        if (event != null && event.getEventResult() == Constant.Result.SUCCESS){
            adapter.addItem(event.getNewsLatest());
            adapter.addItems(event.getNewsLatest().getStories());
        }
    }

    @OnClick(R.id.retry)
    public void retry(){
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        refreshLayout.autoRefresh();
    }

}
