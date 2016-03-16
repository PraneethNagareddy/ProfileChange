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
public class CustomAdapter extends BaseAdapter   {


    //private static ArrayList<ListviewContactItem> listContact;
    private final ArrayList<String> texts;
    private final ArrayList<Integer> images;

    private LayoutInflater mInflater;

  public CustomAdapter(Context photosFragment, ArrayList<String> texts, ArrayList<Integer> images) {
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


     public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.contact_list, null);
            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.lv_contact_item_name);
            holder.txtphone = (TextView) convertView.findViewById(R.id.lv_contact_item_phone);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.deleteView = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
       }

        else {
          holder = (ViewHolder) convertView.getTag();
       }
        holder.txtname.setText(texts.get(position));
        holder.txtphone.setText("ABCD");
        holder.imageView.setImageResource(images.get(position));
        if (!holder.txtname.getText().equals("Default")) {
            holder.deleteView.setImageResource(R.drawable.delete);

            holder.deleteView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "you Clicked " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
         return convertView;
     }



    static class ViewHolder {
        TextView txtname, txtphone;

        ImageView imageView;


        public ImageView deleteView;

    }




}


