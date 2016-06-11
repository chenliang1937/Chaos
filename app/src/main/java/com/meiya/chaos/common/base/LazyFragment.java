package com.meiya.chaos.common.base;

import com.kennyc.view.MultiStateView;

import io.nlopez.smartadapters.adapters.RecyclerMultiAdapter;

/**
 * Created by chenliang3 on 2016/4/22.
 */
public abstract class LazyFragment extends BaseFragment {

    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isVisible = true;
            onVisible();
        }else {
            isVisible = false;
            onInvisible();
        }
    }

    //用以控制同一时间只执行一个动作
    protected boolean canLoadData(MultiStateView multiStateView, RecyclerMultiAdapter adapter){
        int viewState = multiStateView.getViewState();
        if (viewState == MultiStateView.VIEW_STATE_LOADING ||
                (viewState == MultiStateView.VIEW_STATE_CONTENT && adapter.getItemCount() > 0) ||
                viewState == MultiStateView.VIEW_STATE_EMPTY ||
                viewState == MultiStateView.VIEW_STATE_ERROR){
            return false;
        }
        return true;
    }

    protected void onVisible(){
        lazyLoad();
    }

    protected abstract void lazyLoad();

    protected void onInvisible(){}

}
