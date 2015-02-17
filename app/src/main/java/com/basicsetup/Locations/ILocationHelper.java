package com.basicsetup.Locations;

/**
 * Created by pankaj on 29/12/14.
 */
public interface ILocationHelper {
    public void connectLocation();
    public void disconnectLocation();
    public boolean isGpsEnabled();
    public boolean isWifiEnabled();
    public boolean isNetoworkEnabled();
}
