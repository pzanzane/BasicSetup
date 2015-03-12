package com.basicsetup.Locations;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import com.basicsetup.R;
import com.basicsetup.helper.AndroidVersionUtil;
import com.basicsetup.helper.AsyncTaskUtils;
import com.basicsetup.helper.ToastUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by pankaj on 29/12/14.
 */
public class HelperGoogleLocation implements ILocationHelper,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    public static String TAG = "HelperGoogleLocation";

    private Context context = null;
    private LocationCallBacks callBacks = null;
    private LocationRequest mLocationRequest = null;
    private GoogleApiClient mGoogleApiClient = null;
    private int mPriority = -1;

    public HelperGoogleLocation(Context context,
                                LocationCallBacks callBacks,
                                int locationPriority) {
        this.context = context;
        this.callBacks = callBacks;
        this.mPriority = locationPriority;

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void connectLocation() {

        if (mGoogleApiClient.isConnected()) {
            getLocation();
        } else {

            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(mPriority);
            mLocationRequest.setInterval(2000);
            mLocationRequest.setNumUpdates(1);
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void disconnectLocation() {

        if (mGoogleApiClient != null &&
                mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    /*
     *Google Api Client
     */
    @Override
    public void onConnected(Bundle bundle) {
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        getAddress(location);
    }

    private void getLocation() {
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void getAddress(Location location) {
        // If Google Play Services is available
        if (servicesConnected()) {
            // Display the current location in the UI
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                AsyncTaskUtils.execute(
                        new AsyncTaskGetAddress(
                                context,
                                location.getAccuracy(),
                                iGetAddress
                        ), new LatLng[]{latLng}
                );
            } else {

                callBacks.locationDetails(context.getResources().getString(R.string.no_address_found),
                                          context.getResources().getString(R.string.no_address_found),
                                          null, null,
                                          0f,
                                          0f,
                                          -1f);
            }
        } else {
            ToastUtils.showToast(context.getResources().getString(R.string.no_google_play_Service));
        }
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;

        } else {
            // Google Play services was not available for some reason
            return false;
        }
    }

    private AsyncTaskGetAddress.IGetAddress iGetAddress = new AsyncTaskGetAddress.IGetAddress() {
        @Override
        public void fetchedAddress(LatLng location,
                                   String longAddress,
                                   String shortAddress,
                                   String pinCode,
                                   String country,
                                   float accuracy) {

            callBacks.locationDetails(longAddress, shortAddress, pinCode, country, location.latitude, location.longitude,accuracy);
        }
    };

    @Override
    public boolean isGpsEnabled() {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;

        } else {
            return true;
        }
    }

    @Override
    public boolean isWifiEnabled() {
        WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isNetoworkEnabled() {

        ConnectivityManager connectivity =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity!=null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = -1;
        String locationProviders;

        if (AndroidVersionUtil.getAndroidVersion()>= Build.VERSION_CODES.KITKAT){
            try {

                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }
}
