package com.enshire.techutils.profiler.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.enshire.techutils.profiler.R;
import com.enshire.techutils.profiler.dao.PreferencesDAO;

import java.util.ArrayList;

/**
 * Created by keerthichandra on 3/3/2016.
 */
public class CustomAdapter1 extends BaseAdapter   {

    //private static ArrayList<ListviewContactItem> listContact;
    private final ArrayList<String> texts;
    private final ArrayList<Integer> images;

    private LayoutInflater mInflater;

  public CustomAdapter1(Context photosFragment, ArrayList<String> texts, ArrayList<Integer> images) {
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
        //    holder.txtphone = (TextView) convertView.findViewById(R.id.lv_contact_item_phone);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.deleteView = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
       }

        else {
          holder = (ViewHolder) convertView.getTag();
       }
        holder.txtname.setText(texts.get(position));
        holder.imageView.setImageResource(images.get(position));
         if (holder.txtname.getText().equals("Default"))
             holder.deleteView.setImageResource(R.drawable.dummy);
         else {
            holder.deleteView.setImageResource(R.drawable.delete);

            holder.deleteView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    PreferencesDAO dao = new PreferencesDAO(v.getContext());
                    if(dao.deletePreference(texts.get(position)) > 0)
                        Toast.makeText(v.getContext(), "Your Preference for " + texts.get(position) + " is deleted", Toast.LENGTH_SHORT).show();
                }
            });
        }
         return convertView;
     }

    public void onPreexecute(){



    }

    static class ViewHolder {
        TextView txtname, txtphone;
        ImageView imageView;
        public ImageView deleteView;

    }




}


