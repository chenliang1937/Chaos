package com.meiya.chaos.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.meiya.chaos.R;
import com.meiya.chaos.common.base.BaseAdapterItemView;
import com.meiya.chaos.model.NewsLatest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by chenliang3 on 2016/5/6.
 */
public class DateItemView extends BaseAdapterItemView<NewsLatest> {

    @BindView(R.id.date_item_tv)
    TextView textView;

    public DateItemView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.date_item;
    }

    @Override
    public void bind(NewsLatest newsLatest) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String today = dateFormat.format(new Date());
        if (today.equals(newsLatest.getDate())){
            textView.setText("最新消息");
        }else {
            try {
                Date date = new SimpleDateFormat("yyyyMMdd").parse(newsLatest.getDate());
                String dateStr = new SimpleDateFormat("yyyy年MM月dd日").format(date);
                textView.setText(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
