package com.nagareddy.tabbedactivity.logic;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.nagareddy.tabbedactivity.R;
import com.nagareddy.tabbedactivity.dao.PreferencesDAO;
import com.nagareddy.tabbedactivity.helperclasses.StateDescriber;

import net.soulwolf.widget.materialradio.MaterialRadioGroup;
import net.soulwolf.widget.materialradio.listener.OnCheckedChangeListener;

/**
 * Created by praneeth on 27/03/2016.
 */
public class ProfileSelectionDialogue extends DialogFragment {

    ProfileSelectionDialogListener mListener;
    private String ssid;
    public void setSsid(String ssid, ProfileSelectionDialogListener listener){
        this.ssid = ssid;
        mListener = listener;
    }

    public interface ProfileSelectionDialogListener {
        public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog);
        public void onDialogNegativeClick(android.support.v4.app.DialogFragment dialog);
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

        final PreferencesDAO dao = new PreferencesDAO(getActivity());
        int mode = dao.getPreferenceof(ssid);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        builder.setTitle(ssid);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.content_profile_selection_dialogue, null);
        final MaterialRadioGroup radioGroup = (MaterialRadioGroup) dialogView.findViewById(R.id.imageradiogroup);
        SeekBar seekBar = (SeekBar)dialogView.findViewById(R.id.imgeradioseekbar);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MaterialRadioGroup materialRadioGroup, int i) {
                if (i == R.id.imageradioringer) {
                    dialogView.findViewById(R.id.imageradiovolume).setVisibility(View.VISIBLE);
                    dialogView.findViewById(R.id.imgeradioseekbar).setVisibility(View.VISIBLE);
                }
                else{
                    dialogView.findViewById(R.id.imageradiovolume).setVisibility(View.INVISIBLE);
                    dialogView.findViewById(R.id.imgeradioseekbar).setVisibility(View.INVISIBLE);
                }
            }
        });
        switch (mode) {
            case 1: radioGroup.check(R.id.imageradiovibrate);
                break;
            case 2: radioGroup.check(R.id.imageradiomute);
                break;
            default : radioGroup.check(R.id.imageradioringer);
                dialogView.findViewById(R.id.imageradiovolume).setVisibility(View.VISIBLE);
                seekBar = (SeekBar)dialogView.findViewById(R.id.imgeradioseekbar);
                seekBar.setVisibility(View.VISIBLE);
                if (mode == -1) seekBar.setProgress(100);
                else seekBar.setProgress((mode-3)*100/maxVol);
                break;
        }
       builder.setView(dialogView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int mode = -1;
                        String modeDescription = "";
                        int checked = radioGroup.getCheckedRadioButtonId();
                        switch (checked) {
                            case R.id.imageradiomute:
                                mode = 2;
                                modeDescription = "Silence";
                                break;
                            case R.id.imageradiovibrate:
                                mode = 1;
                                modeDescription = "Vibrate";
                                break;
                            case R.id.imageradioringer:
                                mode = Math.round(((SeekBar)dialogView.findViewById(R.id.imgeradioseekbar)).getProgress() * maxVol / 100) + 3;
                                modeDescription = "Ring at " + ((SeekBar)dialogView.findViewById(R.id.imgeradioseekbar)).getProgress() + "% volume";
                                break;
                        }
                        if (dao.insertASilentProfile(new StateDescriber(mode, ssid))) {
                            Toast.makeText(getContext(), "Preference for " + ssid + ":" + modeDescription, Toast.LENGTH_LONG).show();
                            //ConnectivityManager connMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                            //NetworkInfo netInfo = connMan.getActiveNetworkInfo();
                            WifiManager wifiMan = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
                            WifiInfo wifiInfo = wifiMan.getConnectionInfo();
                            // Toast.makeText(context, "wifi:"+wifiInfo.getSSID(), Toast.LENGTH_SHORT).show();
                            if (ssid.equals("Default")
                                    && wifiInfo.getSSID().equals("")) {
                                applyMode(mode);
                            }
                            if (wifiInfo.getSSID().equals("\"" + ssid + "\"")) {
                                applyMode(mode);
                            }
                        }
                        mListener.onDialogPositiveClick(ProfileSelectionDialogue.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ProfileSelectionDialogue.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }


    private void applyMode(int preferredMode){
        AudioManager audio_mngr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        if (preferredMode == 1) {
            audio_mngr.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        } else {
            if (preferredMode == 2) {
                audio_mngr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else {
                if (preferredMode == 3) {
                    audio_mngr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                } else {
                    audio_mngr.setStreamVolume(AudioManager.STREAM_RING, (preferredMode - 3), AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_PLAY_SOUND);
                }
            }
        }
        //Toast.makeText(context, "Preferred Mode:" + preferredMode, Toast.LENGTH_SHORT).show();
    }

}
