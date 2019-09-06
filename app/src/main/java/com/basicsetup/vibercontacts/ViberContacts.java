package com.clevertrap.contactsfromviber;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ViberContacts {

    interface ICallBackViberContacts{
        void callBackViberContacts(List<ModelContact> list);
    }

    private ICallBackViberContacts callBack = null;
    public ViberContacts(ICallBackViberContacts callBack){
        this.callBack = callBack;
    }
    private void getAllContacts(ContentResolver contentResolver,String packageName){

        Cursor contactCursor = contentResolver.query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts.CONTACT_ID,
                        ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY},
                ContactsContract.RawContacts.ACCOUNT_TYPE + " LIKE ?",
                new String[]{packageName},
                null);

        if(contactCursor!=null && contactCursor.moveToFirst()){

            do {





            }while(contactCursor.moveToNext());
        }
    }

    public void getContactsWithMiMeTypes(ContentResolver contentResolver, String arrMimeTypes[]){

        HashSet<String> hashSet = new HashSet<>();
        List<ModelContact> listContactsWithMiMeTypes = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        builder.append("'"+arrMimeTypes[0]+"'");
        for(int pos =1; pos < arrMimeTypes.length;pos++){
            builder.append(","+"'"+arrMimeTypes[pos]+"'");
        }


        Cursor contactCursor = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data.CONTACT_ID,
                        ContactsContract.Data.DISPLAY_NAME,
                        ContactsContract.Data.MIMETYPE},
                        ContactsContract.Data.MIMETYPE+" IN ("+builder.toString()+")",
                null,
                    null);

        Log.d("WASTE","contactCursor count:"+(contactCursor!=null?contactCursor.getCount():0));
        if(contactCursor!=null && contactCursor.moveToFirst()){

            do {
                String dataContactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                if(!hashSet.add(dataContactId)){
                    continue;
                }

                String name         =   contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                String number       =   getNumber(contentResolver,dataContactId);

                ModelContact model = new ModelContact();
                model.setContactId(dataContactId);
                model.setContactName(name);
                model.setContactNumber(number);

                listContactsWithMiMeTypes.add(model);
            }while(contactCursor.moveToNext());
        }
        contactCursor.close();
        callBack.callBackViberContacts(listContactsWithMiMeTypes);
    }

    private String getNumber(ContentResolver contentResolver,String contactId){
        Cursor contactCursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " LIKE ?",
                new String[]{contactId},
                null);

        if(contactCursor!=null && contactCursor.moveToFirst()){
            return contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        return null;
    }

    public static String getContactMimeTypeDataId(@NonNull Context context, String contactId, @NonNull String mimeType) {
        if (TextUtils.isEmpty(mimeType))
            return null;
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data._ID}, ContactsContract.Data.MIMETYPE + "= ? AND "
                + ContactsContract.Data.CONTACT_ID + "= ?", new String[]{mimeType, contactId}, null);
        if (cursor == null)
            return null;
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        String result = cursor.getString(cursor.getColumnIndex(ContactsContract.Data._ID));
        cursor.close();
        return result;
    }
}
