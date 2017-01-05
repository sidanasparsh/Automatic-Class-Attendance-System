package com.example.attendancesystem.smartattend;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class ProductListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Rows> mList;

    public ProductListAdapter(Context mContext, List<Rows> mList){
        this.mContext=mContext;
        this.mList=mList;
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

        View v = View.inflate(mContext, R.layout.attendance_layout, null);

        TextView tvDate = (TextView) v.findViewById(R.id.date);
        TextView checkInTime = (TextView) v.findViewById(R.id.checkInTime);
        TextView checkOutTime = (TextView) v.findViewById(R.id.checkOutTime);
        TextView tvMark = (TextView) v.findViewById(R.id.mark);

        Log.d("INSIDE PRODUCT ADAPTER","DATE IS "+String.valueOf(mList.get(i).getDate()));
        Log.d("INSIDE PRODUCT ADAPTER","IN TIME IS "+String.valueOf(mList.get(i).getCheckInTime()));
        Log.d("INSIDE PRODUCT ADAPTER","OUT TIME IS "+String.valueOf(mList.get(i).getCheckOutTime()));
        Log.d("INSIDE PRODUCT ADAPTER","MARKED IS "+String.valueOf(mList.get(i).getMark()));

        tvDate.setText(String.valueOf(mList.get(i).getDate()));
        checkInTime.setText(String.valueOf(mList.get(i).getCheckInTime()));
        checkOutTime.setText(String.valueOf(mList.get(i).getCheckOutTime()));
        tvMark.setText(String.valueOf(mList.get(i).getMark()));

        return v;
    }
}