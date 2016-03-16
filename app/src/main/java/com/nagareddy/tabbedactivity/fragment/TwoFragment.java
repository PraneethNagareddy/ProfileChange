package com.nagareddy.tabbedactivity.fragment;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nagareddy.tabbedactivity.R;
import com.nagareddy.tabbedactivity.dao.PreferencesDAO;
import com.nagareddy.tabbedactivity.helperclasses.StateDescriber;
import com.nagareddy.tabbedactivity.logic.ProfileSelection;
import com.nagareddy.tabbedactivity.logic.Tabs;

import java.util.ArrayList;


public class TwoFragment extends Fragment{
    final ArrayList<String> savedSSIDs = new ArrayList<String>();
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
        PreferencesDAO dao = new PreferencesDAO(getActivity().getApplicationContext());
        ArrayList<Integer> savedModes = new ArrayList<Integer>();

        ArrayList<StateDescriber> preferences = dao.getSilentProfiles();
        for (StateDescriber preference : preferences) {
            savedSSIDs.add(preference.getSsid());
            savedModes.add(getImageId(preference.getMode()));
        }

        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.lv_contact);
        CustomAdapter adapter = new CustomAdapter(getActivity(), savedSSIDs, savedModes);
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
            Tabs parentActivity = (Tabs)getActivity();
            parentActivity.setMenuLayout(1);
            parentActivity.invalidateOptionsMenu();
        }
    }

    public Integer getImageId(int mode){
        if(mode==1) return R.drawable.vibrate;
        else if (mode == 2) return R.drawable.mute;
        else if (mode == 3) return R.drawable.ringer;
        else if(mode == -1) return R.drawable.dummy;
        else return R.drawable.ringer;
    }
}
