package com.clevertrap.contactsfromviber;


import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

public class UtilityCommonsDatKind {
/*

    private ContentResolver contentResolver = null;

    public UtilityCommonsDatKind(ContentResolver contentResolver){
        this.contentResolver =contentResolver;
    }

    public ArrayList<ModelContact> getContacts (String packageNameOfApp){
        Cursor contactCursor = contentResolver.query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{
                        ContactsContract.RawContacts.CONTACT_ID
                            },
                ContactsContract.RawContacts.ACCOUNT_TYPE + " LIKE ?",
                new String[]{packageNameOfApp},
                null);

        ArrayList<ModelContact> myWhatsappContacts = new ArrayList<>();
        int contactIdColumnIndex = contactCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID);


        if (contactCursor != null && contactCursor.getCount() > 0) {

            if (contactCursor.moveToFirst()) {
                do {

                    String contactId = contactCursor.getString(contactIdColumnIndex);
                    ModelContact model = getContactFromCommonDataKinds(contactId);

                    myWhatsappContacts.add(model);
                    Log.d("WASTE","ContactName:" +model.getContactName()+"\n"
                            +"ContactNumber: "+model.getContactNumber());


                } while (contactCursor.moveToNext());

                //Close the cursor to free memory
                contactCursor.close();
            }
        }

        return myWhatsappContacts;

    }
    private ModelContact getContactFromCommonDataKinds(String contactId){



        if (!TextUtils.isEmpty(contactId)) {
            //Get Data from ContactsContract.CommonDataKinds.Phone of Specific CONTACT_ID
            Cursor curosrContactContract = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.DATA,
                            ContactsContract.CommonDataKinds.Phone.DATA1,
                            ContactsContract.CommonDataKinds.Phone.DATA2,
                            ContactsContract.CommonDataKinds.Phone.DATA3},
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{contactId}, null);

            if (curosrContactContract != null && curosrContactContract.moveToFirst()) {

                String id = curosrContactContract.getString(curosrContactContract.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name = curosrContactContract.getString(curosrContactContract.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = curosrContactContract.getString(curosrContactContract.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String data = curosrContactContract.getString(curosrContactContract.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));




                ModelContact model = new ModelContact();
                model.setContactId(id);
                model.setContactName(name);
                model.setContactNumber(number);


                return model;

            }
            curosrContactContract.close();
        }

        return null;
    }
*/

}
