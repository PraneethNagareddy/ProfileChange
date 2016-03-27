package com.nagareddy.tabbedactivity.fragment;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.nagareddy.tabbedactivity.R;
import com.nagareddy.tabbedactivity.dao.PreferencesDAO;
import com.nagareddy.tabbedactivity.logic.ProfileSelectionDialogue;
import com.nagareddy.tabbedactivity.logic.Tabs;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment# factory method to
 * create an instance of this fragment.
 */
        public class BlankFragment extends Fragment implements UIDataUpdater,ProfileSelectionDialogue.ProfileSelectionDialogListener {

    ScanListAdapter adapter;
    final ArrayList<String> scanSSIDs =  new ArrayList<String>();
    ArrayList<Integer> savedModes = new ArrayList<Integer>();
    PreferencesDAO dao ;
    private OnFragmentInteractionListener mListener;

    public BlankFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        dao = new PreferencesDAO(getActivity().getApplicationContext());
        updateList();
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.lv_contact);


        adapter = new ScanListAdapter(getActivity(), scanSSIDs, savedModes);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //Toast.makeText(getActivity(), "Clicked"+arg0.getItem(), Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getActivity(), ProfileSelection.class); // alwys use getActivity() as first parameter in fragment. Generally. "this" is used while calling an activity from an other activity. For fragments use getActivity
                //intent.putExtra("ssid", scanSSIDs.get(+position));
                //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
                displayDialogue(scanSSIDs.get(+position));
            }
        });
        return rootView;
//code to get the listView instance using findViewByID etc
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
           // Tabs parentActivity = (Tabs)getActivity();
            //parentActivity.setMenuLayout(0);
            //parentActivity.invalidateOptionsMenu();
            savedModes.clear();
            for (String ssid:scanSSIDs) {
                //scanSSIDs.add(ssid);
                savedModes.add(getImageId(dao.getPreferenceof(ssid)));
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        setUserVisibleHint(true);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public Integer getImageId(int mode){
        if(mode==1) return R.drawable.vibrate;
        else if (mode == 2) return R.drawable.mute;
        else if (mode == 3) return R.drawable.ringer;
        else if(mode == -1) return R.drawable.dummy;
        else return R.drawable.ringer;
    }

    public void OnSavedListComplete(){

    }

    public void updateList(){
        WifiAsyncTask asyncTask = new WifiAsyncTask();
        asyncTask.registerUI(getActivity(), this);
        asyncTask.execute();
    }


     public void onScanListComplete(ArrayList<String> ssids) {
        Tabs parentActivity = (Tabs)getActivity();
        parentActivity.stopAnimation();
        scanSSIDs.clear();
        savedModes.clear();
        for (String ssid:ssids) {
            if(!ssid.trim().equals("")) {
                scanSSIDs.add(ssid);
                savedModes.add(getImageId(dao.getPreferenceof(ssid)));
            }
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onResume(){

        updateList();
        super.onResume();
    }



    public void displayDialogue(String ssid){

        ProfileSelectionDialogue dialog = new ProfileSelectionDialogue();
        dialog.setSsid(ssid,this);
        dialog.show(getActivity().getSupportFragmentManager(), "Preference");
    }

}


