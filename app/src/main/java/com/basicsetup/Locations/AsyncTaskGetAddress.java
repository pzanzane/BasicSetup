package com.basicsetup.Locations;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by pankaj on 17/11/14.
 */
public class AsyncTaskGetAddress extends AsyncTask<LatLng, Void, Void> {

    public interface IGetAddress {
        void fetchedAddress(LatLng location, String longAddress, String shortAddress, String pinCode, String country, float accuracy);
    }

    private Context context;
    private IGetAddress iGetAddress;
    private LatLng location;
    private String longAddress = null, shortAddress = null, pinCode = null, country = null;
    private float mAccuracy = -1;

    public AsyncTaskGetAddress(Context context, float locationAccuracy,IGetAddress iGetAddress) {
        this.context = context;
        this.iGetAddress = iGetAddress;
        this.mAccuracy = locationAccuracy;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void param) {

        if (iGetAddress != null) {
            iGetAddress.fetchedAddress(location, longAddress, shortAddress, pinCode, country,mAccuracy);
        }


    }

    @Override
    protected Void doInBackground(LatLng... params) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        location = params[0];
        List<Address> addresses = null;
        try {
               /*
                 * Call the synchronous getFromLocation() method with the latitude and
                 * longitude of the current location. Return at most 1 address.
                 */
            addresses = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude, 10
            );

            // Catch network or other I/O problems.
        } catch (IOException exception1) {
            exception1.printStackTrace();

        } catch (IllegalArgumentException exception2) {
            exception2.printStackTrace();

        }
        Address address = null;
        // If the reverse geocode returned an address
        if (addresses != null && addresses.size() > 0) {
            // Get the first address
            address = addresses.get(0);

            for (int i = 0; i < addresses.size(); i++) {
                if (!TextUtils.isEmpty(addresses.get(i).getPostalCode())) {
                    pinCode = addresses.get(i).getPostalCode();

                    break;
                }
            }

            // Format the first line of address
            getAddressInString(address);
        }
        return null;
    }

    private void getAddressInString(Address modelAddress) {
        StringBuilder longAddressBuilder = new StringBuilder();
        StringBuilder shortAddressBuilder = new StringBuilder();

        int maxAddressline = modelAddress.getMaxAddressLineIndex();

        for (int i = 0; i < maxAddressline; i++) {
            longAddressBuilder.append((i == (maxAddressline - 1)) ? modelAddress.getAddressLine(i) : (modelAddress.getAddressLine(i) + ","));
            if (i == 1) {
                shortAddressBuilder.append(modelAddress.getAddressLine(i));
            }
        }

        longAddress = longAddressBuilder.toString();
        shortAddress = shortAddressBuilder.toString();
        country = modelAddress.getCountryCode();

    }

}
