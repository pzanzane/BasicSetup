package com.decos.fixi.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.decos.fixi.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by pankaj on 11/12/14.
 */
public class AsyncGCMRegister extends AsyncTask<Void,Void,String>{

    public static interface ICallBackGCMRegister{
        void callBackGcmRegister(String registrationId);
    }

    private ICallBackGCMRegister callBackGCMRegister=null;
    private Context context = null;
    private String gcmSenderId=null;

    public AsyncGCMRegister(Context context,ICallBackGCMRegister callBackGCMRegister){
        this.callBackGCMRegister=callBackGCMRegister;
        this.context=context;
        //gcmSenderId = context.getResources().getString(R.string.gcm_sender_id);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String strRegistrationId) {
        super.onPostExecute(strRegistrationId);

        if(!TextUtils.isEmpty(strRegistrationId)){
            //PreferenceHelper.putString(PreferenceHelper.Constants.PREFERENCE_GSMREGISTRATIONID, strRegistrationId);
        }

        if(callBackGCMRegister!=null){
            callBackGCMRegister.callBackGcmRegister(strRegistrationId);
        }
    }

    @Override
    protected String doInBackground(Void... params) {

        String strRegistrationId = null;
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        try {
            strRegistrationId = gcm.register(gcmSenderId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strRegistrationId;
    }
}
