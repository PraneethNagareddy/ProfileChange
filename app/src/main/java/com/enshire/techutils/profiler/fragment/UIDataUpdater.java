package com.enshire.techutils.profiler.fragment;

import java.util.ArrayList;

/**
 * Created by praneeth on 07/03/2016.
 */
public interface UIDataUpdater {
    public abstract void OnSavedListComplete();
    public abstract void onScanListComplete(ArrayList<String> ssids);
}
