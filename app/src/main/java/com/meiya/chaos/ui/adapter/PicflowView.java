package com.meiya.chaos.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meiya.chaos.R;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.common.base.BaseAdapterItemView;
import com.meiya.chaos.model.PictureFlow;
import com.meiya.chaos.widget.DynamicHeightImageView;

import java.util.Random;

import butterknife.BindView;

/**
 * Created by chenliang3 on 2016/5/20.
 */
public class PicflowView extends BaseAdapterItemView<PictureFlow> {

    @BindView(R.id.pf_dynamic_iv)
    DynamicHeightImageView imageView;
    @BindView(R.id.pf_textview)
    TextView textView;
    @BindView(R.id.pf_recoitem_rv)
    RippleView rippleView;

    private Context context;
    private Random random;
    private static final SparseArray<Double> positionHeightRatios = new SparseArray<Double>();

    public PicflowView(Context context) {
        super(context);
        this.context = context;

        random = new Random();
    }

    @Override
    public int getLayoutId() {
        return R.layout.picflow_item;
    }

    @Override
    public void bind(PictureFlow pictureFlow) {
        textView.setText(pictureFlow.getSetname());
//        double positionHeight = getPositionRatio(position);
//        imageView.setHeightRatio(positionHeight);

        Glide.with(context)
                .load(pictureFlow.getCover())
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                notifyItemAction(Constant.PIC_FLOW_CLICKED);
            }
        });
    }

    //动态设置imageview的高度
    private double getPositionRatio(int position){
        double ratio = positionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0){
            ratio = getRandomHeightRatio();
            positionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio(){
        return (random.nextDouble() / 2.0) + 1.0;//height will be 1.0 - 1.5 the width
    }
}
