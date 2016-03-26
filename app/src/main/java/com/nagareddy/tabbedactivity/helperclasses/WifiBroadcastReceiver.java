package com.nagareddy.tabbedactivity.helperclasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.nagareddy.tabbedactivity.dao.PreferencesDAO;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    PreferencesDAO mydb;
    static boolean firstConnect = true;
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        mydb = new PreferencesDAO(context);
        String currentSSID = null;
        NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (netInfo != null
                && ((!netInfo.isConnected())
                || netInfo.getType() == (ConnectivityManager.TYPE_MOBILE))){

            applyPreference("Default");
        }

        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMan.getConnectionInfo();

        if (currentSSID == null || currentSSID.equals("")) {
            for (int i = 1; i <= 5; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
                currentSSID = wifiInfo.getSSID();
                if (currentSSID != null && !currentSSID.equals("")) break;
            }
        }

        if (netInfo != null) {
            if (ConnectivityManager.TYPE_WIFI == netInfo.getType()) {
                if (netInfo.isConnected()) {
                    applyPreference(currentSSID);
                }
            }
        }
    }

    private void applyPreference(String currentSSID) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        final int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        currentSSID = currentSSID.replace("\"", "");
        String modeDescription = "";
        AudioManager audio_mngr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int preferredMode = mydb.getPreferenceof(currentSSID);
        if (preferredMode == 1) {
            audio_mngr.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            modeDescription = "Vibrate";
        } else {
            if (preferredMode == 2) {
                audio_mngr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                modeDescription = "Silent";
            } else {
                if (preferredMode == 3) {
                    audio_mngr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                } else {
                    audio_mngr.setStreamVolume(AudioManager.STREAM_RING, (preferredMode - 3), AudioManager.FLAG_ALLOW_RINGER_MODES | AudioManager.FLAG_PLAY_SOUND);
                    modeDescription = (preferredMode - 3)*100/maxVol+"% volume";
                }
            }
        }
        Toast.makeText(context, "Preferred Mode:" + modeDescription, Toast.LENGTH_SHORT).show();
    }
}
