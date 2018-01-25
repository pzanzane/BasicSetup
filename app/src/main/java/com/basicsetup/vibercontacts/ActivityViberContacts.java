package com.clevertrap.contactsfromviber;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.List;

public class ActivityViberContacts extends AppCompatActivity{

    //vnd.android.cursor.item/vnd.com.viber.voip.viber_number_call
    //vnd.android.cursor.item/vnd.com.viber.voip.viber_number_message
    //vnd.android.cursor.item/vnd.com.viber.voip.viber_out_call_viber

    //Not required
    //vnd.android.cursor.item/vnd.com.viber.voip.google_voice_message
    //vnd.android.cursor.item/vnd.com.viber.voip.viber_out_call_none_viber


    private final String VIBER_NUMBER_CALL = "vnd.android.cursor.item/vnd.com.viber.voip.viber_number_call";
    private final String VIBER_NUMBER_MESSAGE = "vnd.android.cursor.item/vnd.com.viber.voip.viber_number_message";

    private RecyclerView recyclerView = null;
    private AdapterContacts adapter = null;

    private String []strRequiredPermissions = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};
    private final int request_all_permission = 1;
    private boolean isAllPermissionsGrant = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viber_contacts);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(isAllPermissionsGranted()){

            callViberContacts();
        }else{
            requestPermission();
        }


    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,
                strRequiredPermissions,
                request_all_permission);
    }
    private boolean isAllPermissionsGranted(){

        for(String strPermission:strRequiredPermissions){
            if(ContextCompat.checkSelfPermission(this,strPermission)!= PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d("WASTE","requestCode:"+requestCode+" permission:"+ Arrays.toString(permissions)+" grantResult:"+Arrays.toString(grantResults));
        if(requestCode == request_all_permission && grantResults.length>0){



            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]<0){
                    isAllPermissionsGrant = false;
                }
            }

            if(!isAllPermissionsGrant){
                requestPermission();
            }else{
                callViberContacts();
            }

        }

    }
    private void callViberContacts(){

        ViberContacts viberContact = new ViberContacts(callback);
        viberContact.getContactsWithMiMeTypes(getContentResolver(),
                new String[]{VIBER_NUMBER_CALL,VIBER_NUMBER_MESSAGE});
    }

    private ViberContacts.ICallBackViberContacts callback = new ViberContacts.ICallBackViberContacts() {
        @Override
        public void callBackViberContacts(List<ModelContact> list) {
            Log.d("WASTE","::"+list.size());

            adapter = new AdapterContacts(list,onClickListener);
            recyclerView.setAdapter(adapter);
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btnCall){
                    Log.d("WASTE","btnCall");
                    ModelContact model = adapter.getItem(Integer.parseInt(String.valueOf(v.getTag())));
                    String contactMimeTypeDataId = ViberContacts.getContactMimeTypeDataId(ActivityViberContacts.this,model.getContactId(),VIBER_NUMBER_CALL);

                callViber(contactMimeTypeDataId,model.getContactNumber());
            }else if (v.getId() == R.id.btnMessage){

                ModelContact model = adapter.getItem(Integer.parseInt(String.valueOf(v.getTag())));
                String contactMimeTypeDataId = ViberContacts.getContactMimeTypeDataId(ActivityViberContacts.this,
                        model.getContactId(),VIBER_NUMBER_MESSAGE);

                callViber(contactMimeTypeDataId,model.getContactNumber());
            }
        }
    };

    private void callViber(String contactMimeTypeDataId,String number){
        Intent intent = null;
        if (contactMimeTypeDataId != null) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.contacts/data/" + contactMimeTypeDataId));
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            intent.setPackage("com.viber.voip");
        } else {
            intent = new Intent("android.intent.action.VIEW", Uri.parse("tel:" + Uri.encode(number)));
            intent.setClassName("com.viber.voip", "com.viber.voip.WelcomeActivity");
        }
        startActivity(intent);
    }
}
