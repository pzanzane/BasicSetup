package com.wassupondemand.dobe.backendhelpers;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.wassupondemand.dobe.Constants;
import com.wassupondemand.dobe.helpers.HTTPHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * Created by pankaj on 7/8/15.
 */
public class ResultObjectAsync<T> extends AsyncTask<Void,Void,T> {

    private ContentValues optionParams=null;
    private ArrayList<String> lstUrlParams = null;
    private String methodeType = Constants.URLs.METHODE_TYPE_GET;
    private Context mContext=null;
    private HTTPHelper.ResponseObject responseObj = null;
    private boolean cancel = false;
    private JsonDeserializer jsonDeserializer = null;

    private AsyncTaskObjectCompletionListener taskListener=null;
    private Class<T> cls;
    public ResultObjectAsync(Context context,AsyncTaskObjectCompletionListener<T> taskListener, Class<T> cls){
        this.mContext = context;
        this.taskListener=taskListener;
        this.cls=cls;
    }



    public void setOptionParams(ContentValues optionParams) {
        this.optionParams = optionParams;
    }

    public void setLstUrlParams(ArrayList<String> lstUrlParams) {
        this.lstUrlParams = lstUrlParams;
    }

    public void setMethodeType(String methodeType) {
        this.methodeType = methodeType;
    }

    public void setJsonDeserializer(JsonDeserializer jsonDeserializer) {
        this.jsonDeserializer = jsonDeserializer;
    }

    @Override
    protected T doInBackground(Void... voids) {

        HTTPHelper.ResponseObject response = null;
        HTTPHelper helper = new HTTPHelper(HTTPHelper.CONTENT_TYPE_JSON);
        try {

            if(methodeType.equalsIgnoreCase(Constants.URLs.METHODE_TYPE_GET)){

                response = helper.executeHttpGet(Constants.URLs.URL_BASE, lstUrlParams, optionParams);

            }else if(methodeType.equalsIgnoreCase(Constants.URLs.METHODE_TYPE_POST)){

                response = helper.executeHttpPost(Constants.URLs.URL_BASE, lstUrlParams,optionParams);

            }else if(methodeType.equalsIgnoreCase(Constants.URLs.METHODE_TYPE_PUT)){

                response = helper.executeHttpPut(Constants.URLs.URL_BASE, lstUrlParams,optionParams);

            }

            Gson gson = null;
            if (jsonDeserializer != null) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(cls, jsonDeserializer);
                gson = gsonBuilder.create();
            } else {
                gson = new Gson();
            }

            T tt =  null;
            responseObj = response;

            if(response == null){
                return  null;
            }

            //NOTE: If Asynctask cancelled
            if (cancel) return null;


            if(response.getStatusCode() == HttpURLConnection.HTTP_OK){
                tt =  gson.fromJson(response.getStrResponse(), cls);
            }

            return tt;

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(T obj) {
        super.onPostExecute(obj);

        if(responseObj == null){
            Toast.makeText(mContext,Constants.ERROR_CANNOT_CONNECT_SERVER,Toast.LENGTH_LONG).show();
            Toast.makeText(mContext,Constants.ERROR_CANNOT_CONNECT_SERVER,Toast.LENGTH_LONG).show();

            if(taskListener!=null){
                //NOTE: If Asynctask cancelled
                if (cancel) return;
                taskListener.onException(new Exception(Constants.ERROR_CANNOT_CONNECT_SERVER));
            }

            return;
        }
        if(taskListener!=null){
            if (cancel) return;
            if(responseObj.getStatusCode() == HttpURLConnection.HTTP_OK){
                taskListener.onTaskComplete(obj);
            }else{

                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(responseObj.getStrResponse());
                    Toast.makeText(mContext, "" + jobj.getString("Message"), Toast.LENGTH_SHORT).show();
                    taskListener.onException(new Exception(responseObj.getStrResponse()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
}
