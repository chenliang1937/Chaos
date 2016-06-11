package com.meiya.chaos.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.route.WalkStep;
import com.meiya.chaos.R;
import com.meiya.chaos.utils.AMapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenliang3 on 2016/5/27.
 */
public class WalkSegmentListAdapter extends BaseAdapter {

    private Context context;
    private List<WalkStep> walkStepList = new ArrayList<>();

    public WalkSegmentListAdapter(Context context, List<WalkStep> steps){
        this.context = context;
        walkStepList.add(new WalkStep());
        for (WalkStep walkStep : steps){
            walkStepList.add(walkStep);
        }
        walkStepList.add(new WalkStep());
    }

    @Override
    public int getCount() {
        return walkStepList.size();
    }

    @Override
    public Object getItem(int position) {
        return walkStepList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.navig_item1, null);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.navitem1_icon);
            viewHolder.dir = (ImageView) convertView.findViewById(R.id.navitem1_dir);
            viewHolder.content = (TextView) convertView.findViewById(R.id.navitem2_text1);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        WalkStep item = walkStepList.get(position);
        viewHolder.icon.setImageResource(R.mipmap.ic_car_purple);
        if (position == 0){
            viewHolder.dir.setImageResource(R.mipmap.dir_start);
            viewHolder.content.setText("出发");
        }else if (position == walkStepList.size()-1){
            viewHolder.dir.setImageResource(R.mipmap.dir_end);
            viewHolder.content.setText("到达终点");
        }else {
            String actionName = item.getAction();
            int resID = AMapUtil.getDriveActionID(actionName);
            viewHolder.dir.setImageResource(resID);
            viewHolder.content.setText(item.getInstruction());
        }

        return convertView;
    }

    private class ViewHolder{
        ImageView icon;
        ImageView dir;
        TextView content;
    }
}
