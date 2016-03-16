package com.nagareddy.tabbedactivity.helperclasses;

/**
 * Created by praneeth on 03/11/2015.
 */
public class WifiNetwork{

    public String SSID;
    public int level;
    public String BSSID;

    public WifiNetwork(String SSID, int level, String BSSID) {
        this.SSID = SSID;
        this.level = level;
        this.BSSID = BSSID;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    @Override
    public boolean equals(Object o) {
        WifiNetwork wifi=(WifiNetwork)o;
        Boolean returnValue = this.getSSID().equals(wifi.getSSID());
        if (returnValue && wifi.getLevel()<this.getLevel())
            wifi.setLevel(this.getLevel());
        System.out.println(" :"+returnValue);
        return returnValue;
    }

    public int hashCode() {
        return 12345;
    };

    @Override
    public String toString(){
        return "[SSID:"+this.getSSID()+" level:"+this.getLevel()+"]";
    }

}