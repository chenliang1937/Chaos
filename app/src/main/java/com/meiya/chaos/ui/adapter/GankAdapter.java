package com.meiya.chaos.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.meiya.chaos.R;
import com.meiya.chaos.model.GankItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenliang3 on 2016/5/31.
 */
public class GankAdapter extends RecyclerView.Adapter {

    private List<GankItem> images;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zhuangbi_item, parent, false);
        return new DebounceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DebounceViewHolder debounceViewHolder = (DebounceViewHolder) holder;
        GankItem item = images.get(position);
        Glide.with(holder.itemView.getContext()).load(item.getImageUrl()).into(debounceViewHolder.imageView);
        debounceViewHolder.textView.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    public void setImages(List<GankItem> items){
        this.images = items;
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
