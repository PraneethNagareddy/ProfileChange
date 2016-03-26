package com.nagareddy.tabbedactivity.fragment;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nagareddy.tabbedactivity.R;
import com.nagareddy.tabbedactivity.dao.PreferencesDAO;
import com.nagareddy.tabbedactivity.helperclasses.StateDescriber;
import com.nagareddy.tabbedactivity.logic.ProfileSelection;
import com.nagareddy.tabbedactivity.logic.Tabs;

import java.util.ArrayList;


public class TwoFragment extends Fragment{
    PreferencesDAO dao;
    final ArrayList<String> savedSSIDs = new ArrayList<String>();
    ArrayList<Integer> savedModes = new ArrayList<Integer>();
    CustomAdapter adapter;
    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dao = new PreferencesDAO(getActivity().getApplicationContext());


        ArrayList<StateDescriber> preferences = dao.getSilentProfiles();
        for (StateDescriber preference : preferences) {
            savedSSIDs.add(preference.getSsid());
            savedModes.add(getImageId(preference.getMode()));
        }

        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.lv_contact);
        adapter = new CustomAdapter(getActivity(), savedSSIDs, savedModes);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //Toast.makeText(getActivity(), "Clicked"+arg0.getItem(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ProfileSelection.class); // alwys use getActivity() as first parameter in fragment. Generally. "this" is used while calling an activity from an other activity. For fragments use getActivity
                intent.putExtra("ssid", savedSSIDs.get(+position));
                startActivity(intent);
            }
        });
        return rootView;//inflater.inflate(R.layout.fragment_one, container, false);
    }

     @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            //Tabs parentActivity = (Tabs)getActivity();
            //parentActivity.setMenuLayout(1);
            //parentActivity.invalidateOptionsMenu();
        }
    }

    public Integer getImageId(int mode){
        if(mode==1) return R.drawable.vibrate;
        else if (mode == 2) return R.drawable.mute;
        else if (mode == 3) return R.drawable.ringer;
        else if(mode == -1) return R.drawable.dummy;
        else return R.drawable.ringer;
    }


    /**
     * Created by keerthichandra on 3/3/2016.
     */
    class CustomAdapter extends BaseAdapter {

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
                //    holder.txtphone = (TextView) convertView.findViewById(R.id.lv_contact_item_phone);
                holder.imageView = (ImageView) convertView.findViewById(R.id.image);
                holder.deleteView = (ImageView) convertView.findViewById(R.id.delete);
                holder.txtphone = (TextView) convertView.findViewById(R.id.lv_contact_item_phone);
                convertView.setTag(holder);
            }

            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtname.setText(texts.get(position));
            holder.imageView.setImageResource(images.get(position));
            if (holder.txtname.getText().equals("Default")) {
                holder.deleteView.setImageResource(R.drawable.dummy);
                holder.txtphone.setText("When not connected to any wifi");
            }
            else {
                holder.deleteView.setImageResource(R.drawable.delete);

                holder.deleteView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(dao.deletePreference(texts.get(position)) > 0) {
                            Toast.makeText(v.getContext(), "Your Preference for " + texts.get(position) + " is deleted", Toast.LENGTH_SHORT).show();
                            dataChanged();
                        }
                    }
                });
            }
            return convertView;
        }
        class ViewHolder {
            TextView txtname, txtphone;
            ImageView imageView;
            public ImageView deleteView;

        }
    }


    private void dataChanged() {
       savedModes.clear();
        savedSSIDs.clear();
        ArrayList<StateDescriber> preferences = dao.getSilentProfiles();
        for (StateDescriber preference : preferences) {
            savedSSIDs.add(preference.getSsid());
            savedModes.add(getImageId(preference.getMode()));
        }
        adapter.notifyDataSetChanged();
    }


}
