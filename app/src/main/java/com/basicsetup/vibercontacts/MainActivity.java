package com.clevertrap.contactsfromviber;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    /*//vnd.android.cursor.item/vnd.com.viber.voip.viber_number_call
    //vnd.android.cursor.item/vnd.com.viber.voip.viber_number_message
    //vnd.android.cursor.item/vnd.com.viber.voip.viber_out_call_viber

    //Not required
    //vnd.android.cursor.item/vnd.com.viber.voip.google_voice_message
    //vnd.android.cursor.item/vnd.com.viber.voip.viber_out_call_none_viber

    private String []strRequiredPermissions = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};
    private final int request_all_permission = 1;
    private boolean isAllPermissionsGrant = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isAllPermissionsGranted()){
            getContacts();
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
    private void getContacts(){

        //org.telegram.messenger
        //com.whatsapp
        //com.viber.voip
        //918147306686
        //9004717129

        callViber("9004717129","vnd.android.cursor.item/vnd.com.viber.voip.google_voice_message");
        //============================================
        AppExecutor.getINSTANCE().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                //DumpTables.printRawContacts(getContentResolver(),"com.viber.voip");
            }
        });
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
                getContacts();
            }

        }

    }

    private void callWhatsApp(String number){


        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
        intent.setPackage("com.whatsapp");
        startActivity(intent);
    }
    private void callViber(String number,String mimeType){

        final String contactId = Utility.getContactIdFromPhoneNumber(getContentResolver(),number);
        final String contactMimeTypeDataId = Utility.getContactMimeTypeDataId(this,
                contactId,
                mimeType);

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
    }*/
}
