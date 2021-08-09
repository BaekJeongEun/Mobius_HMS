package com.example.study36_month_calender;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    public ListViewAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_title, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.titleView);
        //TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);

        ListViewItem listViewItem = listViewItemList.get(position);

        titleTextView.setText(listViewItem.getTitle());
        //descTextView.setText(listViewItem.getDesc());

        return convertView;
    }

    @Override
    public long getItemId(int position){

        return position;
    }

    @Override
    public Object getItem(int position) {

        return listViewItemList.get(position);
    }

    public void addItem(String title) {
        ListViewItem item = new ListViewItem();

        item.setTitle(title);
        //item.setDesc(desc);

        listViewItemList.add(item);
    }

    public void addItem2(String desc) {
        ListViewItem item = new ListViewItem();

        item.setTitle(desc);
        //item.setDesc(desc);

        listViewItemList.add(item);
    }



    /*
    public void addItem(String title, String desc) {
        ListViewItem item = new ListViewItem();

        item.setTitle(title);
        //item.setDesc(desc);

        listViewItemList.add(item);
    }
    */

}