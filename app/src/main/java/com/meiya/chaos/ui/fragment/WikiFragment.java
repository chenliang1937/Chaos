package com.meiya.chaos.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kennyc.view.MultiStateView;
import com.meiya.chaos.R;
import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.common.base.BaseFragment;
import com.meiya.chaos.model.PictureFlow;
import com.meiya.chaos.model.event.PicflowEvent;
import com.meiya.chaos.ui.activity.PicDetailsActivity;
import com.meiya.chaos.ui.adapter.PicflowView;
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
public class WikiFragment extends BaseFragment implements ViewEventListener<PictureFlow> {

    @BindView(R.id.wiki_multiStateView)
    MultiStateView multiStateView;
    @BindView(R.id.wiki_refresh)
    MaterialRefreshLayout refreshLayout;
    @BindView(R.id.wiki_recycler_view)
    RecyclerViewWithFooter recyclerView;

    private RecyclerMultiAdapter adapter;
    private boolean isFirst = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wiki, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//设置瀑布流 2列垂直方向
        adapter = SmartAdapter.empty()
                .map(PictureFlow.class, PicflowView.class)
                .listener(this)
                .into(recyclerView);
        SpacesItemDecoration decoration=new SpacesItemDecoration(8);
        recyclerView.addItemDecoration(decoration);

        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                int index = Integer.MAX_VALUE;
                AppService.getInstance().getPicFlow(getActivity().getTaskId(), index + "");
            }
        });
        recyclerView.setFootItem(new DefaultFootItem());
        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                String index = AppService.getPrefser().get(Constant.PRE_PIC_FLOW_INDEX, String.class, "");
                AppService.getInstance().getMorePic(getActivity().getTaskId(), index);
            }
        });

        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        AppService.getInstance().getPicsFromDb(getActivity().getTaskId());
        if (isFirst && NetWorkUtils.getCurrentNetworkState(getActivity()) == NetworkInfo.State.CONNECTED && NetWorkUtils.isWifiByType(getActivity())){
            isFirst = false;
//            refreshLayout.autoRefresh();
        }
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration(int space) {
            this.space=space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left=space;
            outRect.right=space;
            outRect.bottom=space;
            if(parent.getChildAdapterPosition(view)==0){
                outRect.top=space;
            }
        }
    }

    public void onEventMainThread(PicflowEvent event){
        refreshLayout.finishRefresh();
        if (event.getGetWay() == Constant.GetWay.REFRESH){
            if (event.getEventResult() == Constant.Result.SUCCESS && event.getPictureFlow() != null){
                if (event.getPictureFlow().size() == 0){
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                }else {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    adapter.clearItems();
                    adapter.addItems(event.getPictureFlow());

                    int size = event.getPictureFlow().size();
                    AppService.getPrefser().put(Constant.PRE_PIC_FLOW_INDEX, event.getPictureFlow().get(size - 1).getSetid());
                }
            }else {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
            }
        }else if (event.getGetWay() == Constant.GetWay.INIT){
            if (event.getEventResult() == Constant.Result.SUCCESS && event.getPictureFlow() != null && event.getPictureFlow().size() > 0){
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                adapter.addItems(event.getPictureFlow());
            }else {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                refreshLayout.autoRefresh();
            }
        }else if (event.getGetWay() == Constant.GetWay.LOADMORE){
            if (event.getEventResult() == Constant.Result.SUCCESS && event.getPictureFlow() != null && event.getPictureFlow().size() > 0){
                refreshLayout.finishRefreshLoadMore();
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                adapter.addItems(event.getPictureFlow());

                int size = event.getPictureFlow().size();
                AppService.getPrefser().put(Constant.PRE_PIC_FLOW_INDEX, event.getPictureFlow().get(size - 1).getSetid());
            }else {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
            }
        }
    }

    @OnClick(R.id.retry)
    public void retry(){
        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onViewEvent(int actionId, PictureFlow pictureFlow, int position, View view) {
        if (actionId == Constant.PIC_FLOW_CLICKED){
            Intent intent = new Intent(getActivity(), PicDetailsActivity.class);
            intent.putExtra("PICTUREFLOW", pictureFlow);
            startActivity(intent);
        }
    }
}
