package com.nagareddy.tabbedactivity.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nagareddy.tabbedactivity.R;

import java.util.ArrayList;

/**
 * Created by keerthichandra on 3/3/2016.
 */
public class ScanListAdapter extends BaseAdapter   {

    private final ArrayList<String> texts;
    private final ArrayList<Integer> images;

    private LayoutInflater mInflater;

    public ScanListAdapter(Context photosFragment, ArrayList<String> texts, ArrayList<Integer> images) {
        mInflater = LayoutInflater.from(photosFragment);
        this.texts = texts;
        this.images = images;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return texts.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return texts.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.scan_list, null);
            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.lv_contact_item_name);
            holder.txtphone = (TextView) convertView.findViewById(R.id.lv_contact_item_phone);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtname.setText(texts.get(position));
        holder.txtphone.setText("ABCD");
        holder.imageView.setImageResource(images.get(position));

        return convertView;
    }


    static class ViewHolder {
        TextView txtname, txtphone;
        ImageView imageView;
        public ImageView deleteView;
    }




}


