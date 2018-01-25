package com.clevertrap.contactsfromviber;


import android.content.ContentResolver;
import android.database.Cursor;
import android.database.SQLException;
import android.provider.ContactsContract;
import android.util.Log;

public class DumpTables {
/*
    public static void printRawContacts(ContentResolver contentResolver, String packageName){

        Cursor contactCursor = contentResolver.query(
                ContactsContract.RawContacts.CONTENT_URI,
                null,
                ContactsContract.RawContacts.ACCOUNT_TYPE + " LIKE ?",
                new String[]{packageName},
                null);

        if(contactCursor!=null && contactCursor.moveToFirst()){

            do {

                StringBuilder builder = new StringBuilder();
                int columnCount = contactCursor.getColumnCount();

                for(int i=0;i<columnCount;i++){
                    builder.append("\n");
                    builder.append(contactCursor.getColumnName(i)+" : ");
                    builder.append(contactCursor.getString(i));

                }
                builder.append("=====================================================");

                Log.d("WASTE",builder.toString());

                printContactsData(contentResolver,contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID)));

            }while(contactCursor.moveToNext());
        }
    }

    public static void printContactsData(ContentResolver contentResolver, String contactId){
        Cursor contactCursor = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                null,
                ContactsContract.Data.CONTACT_ID + " LIKE ? AND "+
                        ContactsContract.Data.MIMETYPE+" LIKE ? AND "+
                        ContactsContract.Data.MIMETYPE+" NOT IN ('vnd.android.cursor.item/vnd.com.viber.voip.viber_out_call_none_viber')",
                new String[]{contactId,"%vnd.com.viber.voip%"},
                null);

        if(contactCursor!=null && contactCursor.moveToFirst()){

            do {

                StringBuilder builder = new StringBuilder();
                int columnCount = contactCursor.getColumnCount();

                for(int i=0;i<columnCount;i++){
                    builder.append("\n");
                    builder.append(contactCursor.getColumnName(i)+" : ");

                    try{
                        builder.append(contactCursor.getString(i));
                    }catch (SQLException sqe){
                        builder.append(" NOT A STRING ");
                    }


                }
                builder.append("=====================================================");

                Log.d("ContactsData",builder.toString());

            }while(contactCursor.moveToNext());
        }

    }*/
}
