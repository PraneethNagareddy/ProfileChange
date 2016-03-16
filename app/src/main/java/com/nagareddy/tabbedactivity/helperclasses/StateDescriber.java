package com.nagareddy.tabbedactivity.helperclasses;

/**
 * Created by praneeth on 26/09/2015.
 */
public class StateDescriber {

    private int mode;
    private String ssid;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public StateDescriber(int mode, String ssid) {
        this.mode = mode;
        this.ssid = ssid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateDescriber that = (StateDescriber) o;
        return getSsid().equals(that.getSsid());
    }

    @Override
    public int hashCode() {
        return getSsid().hashCode();
    }
}
