package com.meiya.chaos.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andexert.library.RippleView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meiya.chaos.R;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.common.base.BaseAdapterItemView;
import com.meiya.chaos.model.SogouImage;

import butterknife.BindView;

/**
 * Created by chenliang3 on 2016/6/4.
 */
public class SearchflowView extends BaseAdapterItemView<SogouImage.theItems> {

    @BindView(R.id.search_item_iv)
    ImageView imageView;
    @BindView(R.id.search_item_rv)
    RippleView rippleView;

    private Context context;

    public SearchflowView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.searchflow_item;
    }

    @Override
    public void bind(SogouImage.theItems theItems) {
        Glide.with(context)
                .load(theItems.getThumbUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = Integer.parseInt(theItems.getThumb_height()) * 2;
        imageView.setLayoutParams(params);

        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                notifyItemAction(Constant.SEARCH_IMAGE_CLICKED);
            }
        });
    }

}
