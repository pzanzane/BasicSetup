package com.basicsetup.Locations;

import android.content.Context;

import com.app.poclocation.poclocation.Locations.HelperGoogleLocation;
import com.app.poclocation.poclocation.Locations.ILocationHelper;
import com.app.poclocation.poclocation.Locations.LocationCallBacks;

/**
 * Created by pankaj on 29/12/14.
 */
public class LocationFactory {

    public static ILocationHelper getLocationClient(Context context,
                                             LocationCallBacks callback,
                                             int locationAccuracy){

        return new HelperGoogleLocation(context,callback,locationAccuracy);

    }
}
