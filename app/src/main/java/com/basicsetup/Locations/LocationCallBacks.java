package com.basicsetup.Locations;

/**
 * Created by pankaj on 29/12/14.
 */
public interface LocationCallBacks {
    public void locationDetails(String longAddress, String shortAddress, String pinCode, String country, double lat, double lon, float accuracy);
}
