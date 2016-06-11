package com.meiya.chaos.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.bumptech.glide.Glide;
import com.meiya.chaos.R;
import com.meiya.chaos.animator.AnimatorHelp;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.common.base.BaseAdapterItemView;
import com.meiya.chaos.model.VideoList;
import com.meiya.chaos.widget.JCVideoPlayerStandard;

import butterknife.BindView;
/**
 * Created by chenliang3 on 2016/5/17.
 */
public class VideoItemView extends BaseAdapterItemView<VideoList.Videos> {

    @BindView(R.id.my_video_player)
    JCVideoPlayerStandard jcVideoPlayerStandard;
    @BindView(R.id.vi_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.vi_textview)
    TextView tv_source;
    @BindView(R.id.vi_comment)
    TextView tv_comment;
    @BindView(R.id.vi_favo)
    ImageView iv_favo;
    @BindView(R.id.vi_rippleview)
    RippleView rippleView;

    private Context context;
    private boolean isTrue = true;

    public VideoItemView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_item;
    }

    @Override
    public void bind(VideoList.Videos videos) {
        jcVideoPlayerStandard.setUp(videos.getMp4_url(), videos.getTitle());
        Glide.with(context)
                .load(videos.getCover())
                .centerCrop()
                .placeholder(R.mipmap.ic_boy)
                .error(R.mipmap.ic_boy)
                .crossFade()
                .into(jcVideoPlayerStandard.ivThumb);
        tv_source.setText(videos.getTopicName());
        tv_comment.setText(videos.getReplyCount());

        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                notifyItemAction(Constant.VIDEO_RIPPLE_CLICKED);
            }
        });

        iv_favo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemAction(Constant.VIDEO_FAVO_CLICKED);
                if (isTrue){
                    isTrue = false;
                    iv_favo.setColorFilter(getResources().getColor(R.color.pretty_red));
                    AnimatorHelp.btnClickAnim(iv_favo, 300);
                }else {
                    isTrue = true;
                    iv_favo.clearColorFilter();
                    AnimatorHelp.btnClickAnim(iv_favo, 300);
                }
            }
        });
    }
}
