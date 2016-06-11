package com.meiya.chaos.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.bumptech.glide.Glide;
import com.meiya.chaos.R;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.common.base.BaseAdapterItemView;
import com.meiya.chaos.model.NewsLatest;

import butterknife.BindView;

/**
 * Created by chenliang3 on 2016/5/5.
 */
public class LastNewsItemView extends BaseAdapterItemView<NewsLatest.Stories>{

    @BindView(R.id.recoitem_iv)
    ImageView imageView;
    @BindView(R.id.recoitem_tv)
    TextView textView;
    @BindView(R.id.recoitem_rv)
    RippleView rippleView;

    private Context context;

    public LastNewsItemView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.recommend_item;
    }

    @Override
    public void bind(final NewsLatest.Stories stories) {
        textView.setText(stories.getTitle());
        String url = stories.getImages().get(0);
        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.ic_boy)
                .error(R.mipmap.ic_boy)
                .crossFade()
                .into(imageView);

        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                notifyItemAction(Constant.CONTENT_CLICKED);
            }
        });
    }
}
