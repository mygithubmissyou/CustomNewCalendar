package com.haveatry.customcalendar;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NewCalendar extends LinearLayout {
    LayoutInflater inflater;
    ImageView btnPre;
    ImageView btnNext;
    TextView tvDateCenter;
    GridView gridView;
    private Calendar calendar;

    public NewCalendar(Context context) {
        this(context, null);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
        renderCalendar();
    }

    /**
     * 初始化布局
     * @param context
     */
    private void initData(Context context) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.canlendar_new, this, true);
        tvDateCenter = view.findViewById(R.id.tv_date_center);
        btnPre = view.findViewById(R.id.btn_pre);
        btnNext = view.findViewById(R.id.btn_next);
        gridView = view.findViewById(R.id.gv_date);
        calendar = Calendar.getInstance();
        btnPre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, -1);
                renderCalendar();
            }
        });
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, 1);
                renderCalendar();
            }
        });
    }

    /**
     * 渲染布局
     */
    private void renderCalendar() {

        tvDateCenter.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
        Calendar dateCalendar = (Calendar) calendar.clone();
        dateCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int preDays = dateCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        dateCalendar.add(Calendar.DAY_OF_MONTH, -preDays);
        int maxDays = preDays+ countShowDays();
        ArrayList<Date> list = new ArrayList<Date>();
        while (list.size() < maxDays) {
            list.add(dateCalendar.getTime());
            dateCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        DateAdapter dateAdapter = new DateAdapter(list);
        gridView.setAdapter(dateAdapter);
    }

    /**
     * 计算天数
     * @return
     */
    private int countShowDays() {
        Calendar calendarNew = (Calendar) calendar.clone();
        int thisMonthDays = calendarNew.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendarNew.set(Calendar.DAY_OF_MONTH,thisMonthDays);
        int lastDays=7-calendarNew.get(Calendar.DAY_OF_WEEK);
        return lastDays+thisMonthDays;
    }

    /**
     * GridView适配器
     */
    private class DateAdapter extends BaseAdapter {

        private final ArrayList<Date> mList;

        public DateAdapter(ArrayList<Date> list) {
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_calendar_item, viewGroup, false);
            }
            TextView textView = view.findViewById(R.id.tv_date_item);
            textView.setText(mList.get(i).getDate() + "");
            if (calendar.get(Calendar.MONTH) != mList.get(i).getMonth()) {
                textView.setTextColor(Color.GRAY);
            }
            if (calendar.get(Calendar.MONTH) == mList.get(i).getMonth() && calendar.get(Calendar.DAY_OF_MONTH) == mList.get(i).getDate()) {
                textView.setTextColor(Color.RED);
            }
            return view;
        }
    }
}
