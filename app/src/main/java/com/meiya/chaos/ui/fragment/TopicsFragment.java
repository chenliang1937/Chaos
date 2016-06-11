package com.meiya.chaos.ui.fragment;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kennyc.view.MultiStateView;
import com.meiya.chaos.R;
import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.common.base.BaseFragment;
import com.meiya.chaos.model.VideoList;
import com.meiya.chaos.model.event.VideoEvent;
import com.meiya.chaos.model.event.VideoInitEvent;
import com.meiya.chaos.ui.adapter.VideoItemView;
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
public class TopicsFragment extends BaseFragment implements ViewEventListener<VideoList.Videos> {

    @BindView(R.id.multiStateView)
    MultiStateView multiStateView;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerViewWithFooter recyclerView;

    private RecyclerMultiAdapter adapter;
    private int cursor = 1;
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
                .map(VideoList.Videos.class, VideoItemView.class)
                .listener(this)
                .into(recyclerView);

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                cursor = 1;
                AppService.getInstance().getVideoList(getActivity().getTaskId(), cursor+"");
            }
        });
        recyclerView.setFootItem(new DefaultFootItem());
        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                cursor += 10;
                AppService.getInstance().getMoreVideos(getActivity().getTaskId(), cursor+"");
            }
        });

        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        AppService.getInstance().getVideosFromDb(getActivity().getTaskId());
        if (isFirst && NetWorkUtils.getCurrentNetworkState(getActivity()) == NetworkInfo.State.CONNECTED && NetWorkUtils.isWifiByType(getActivity())){
            isFirst = false;
//            refreshLayout.autoRefresh();
        }
    }

    @Override
    public void onViewEvent(int actionId, VideoList.Videos videos, int position, View view) {
        switch (actionId){
            case Constant.VIDEO_RIPPLE_CLICKED:

                break;
            case Constant.VIDEO_FAVO_CLICKED:

                break;
        }
    }

    public void onEventMainThread(VideoEvent event){
        refreshLayout.finishRefresh();
        if (event.getEventResult() == Constant.Result.SUCCESS && event.getVideoList() != null){
            if (event.getGetWay() == Constant.GetWay.REFRESH){
                if (event.getVideoList().getV9LG4B3A0().size() == 0){
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                }else {
                    adapter.clearItems();
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    adapter.addItems(event.getVideoList().getV9LG4B3A0());
                }
            }else if (event.getGetWay() == Constant.GetWay.LOADMORE){
                refreshLayout.finishRefreshLoadMore();
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                adapter.addItems(event.getVideoList().getV9LG4B3A0());
            }
        }else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    public void onEventMainThread(VideoInitEvent event){
        if (event.getEventResult() == Constant.Result.SUCCESS && event.getVideosList() != null && event.getVideosList().size() > 0){
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            adapter.addItems(event.getVideosList());
        }else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            refreshLayout.autoRefresh();
        }
    }

    @OnClick(R.id.retry)
    public void retry(){
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        refreshLayout.autoRefresh();
    }

}
