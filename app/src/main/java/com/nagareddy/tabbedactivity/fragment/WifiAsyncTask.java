package com.nagareddy.tabbedactivity.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import com.nagareddy.tabbedactivity.helperclasses.WifiNetwork;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by praneeth on 07/03/2016.
 */

class WifiAsyncTask extends AsyncTask<Void,Void,Void> {
    Context parentContext;
    UIDataUpdater updater;
    ArrayList<String> ssids = new ArrayList<String>();


    public void registerUI(Context context, UIDataUpdater updater) {
        this.parentContext = context;
        this.updater = updater;
    }


    ProgressDialog dialog;

    @Override
    protected Void doInBackground(Void ... params) {
        WifiManager wifi;
        wifi = (WifiManager) parentContext.getSystemService(Context.WIFI_SERVICE);
        wifi.startScan();
        try{
            Thread.sleep(2000);
        }catch(InterruptedException ie){ }
        ssids = getSSIDS(wifi.getScanResults());
        return null;
    }

    @Override
    protected void onPreExecute() {

dialog = ProgressDialog.show(parentContext, "Loading...", "Please wait...", true);

    }


    @Override
    protected void onPostExecute(Void result) {
        dialog.hide();
        updater.onScanListComplete(ssids);
    }

    public static ArrayList<String> getSSIDS(List<ScanResult> scanResults) {
        HashSet<WifiNetwork> set=new HashSet<WifiNetwork>();
        for(ScanResult result: scanResults) {
            set.add(new WifiNetwork(result.SSID,result.level,result.BSSID));
        }
        ArrayList<String> list=new ArrayList<String>();
        for(WifiNetwork wifi:set){
            list.add(wifi.getSSID());
        }
        return list;
    }
}