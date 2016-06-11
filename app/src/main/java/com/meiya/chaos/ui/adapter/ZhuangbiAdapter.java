package com.meiya.chaos.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.meiya.chaos.R;
import com.meiya.chaos.model.ZhuangbiImage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenliang3 on 2016/5/30.
 */
public class ZhuangbiAdapter extends RecyclerView.Adapter {

    private List<ZhuangbiImage> images;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zhuangbi_item, parent, false);
        return new DebounceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DebounceViewHolder debounceViewHolder = (DebounceViewHolder) holder;
        ZhuangbiImage image = images.get(position);
        Glide.with(holder.itemView.getContext()).load(image.getImage_url()).into(debounceViewHolder.imageView);
        debounceViewHolder.textView.setText(image.getDescription());
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    public void setImages(List<ZhuangbiImage> images){
        this.images = images;
        notifyDataSetChanged();
    }

    class DebounceViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.zhuangbiTv)
        TextView textView;
        @BindView(R.id.zhuangbiIv)
        ImageView imageView;

        public DebounceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
