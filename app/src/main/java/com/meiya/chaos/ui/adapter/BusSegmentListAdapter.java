package com.meiya.chaos.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.meiya.chaos.R;
import com.meiya.chaos.utils.AMapUtil;

import java.util.List;

/**
 * Created by chenliang3 on 2016/5/27.
 */
public class BusSegmentListAdapter extends BaseAdapter {

    private Context context;
    private List<BusPath> busPathList;
    private BusRouteResult busRouteResult;

    public BusSegmentListAdapter(Context context, BusRouteResult busRouteResult){
        this.context = context;
        this.busRouteResult = busRouteResult;
        this.busPathList = busRouteResult.getPaths();
    }

    @Override
    public int getCount() {
        return busPathList.size();
    }

    @Override
    public Object getItem(int position) {
        return busPathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.navig_item2, null);
            holder.title = (TextView) convertView.findViewById(R.id.navitem2_text1);
            holder.desc = (TextView) convertView.findViewById(R.id.navitem2_text2);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        BusPath item = busPathList.get(position);
        holder.title.setText(AMapUtil.getBusPathTitle(item));
        holder.desc.setText(AMapUtil.getBusPathDes(item));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    private class ViewHolder{
        TextView title;
        TextView desc;
    }
}
