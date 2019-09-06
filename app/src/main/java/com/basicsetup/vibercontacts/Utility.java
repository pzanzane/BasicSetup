package com.clevertrap.contactsfromviber;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.provider.ContactsContract.PhoneLookup;
import android.widget.Toast;

public class Utility {
/*
    private static String getFormattedPhoneNumber(Context context, String input) {

        String normalizedPhone = input.replaceAll("[^0-9+]", "");

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String countryCode = tm.getSimCountryIso();
            String phoneNumber = PhoneNumberUtils.formatNumber(normalizedPhone, countryCode.toUpperCase());
            //String formattedNumber = PhoneNumberUtils.formatNumberToE164(phoneNumber,countryCode).replaceAll("[^0-9]", "");

            return phoneNumber;

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

    public static String getContactIdFromPhoneNumber(ContentResolver contentResolver,String phone) {
        if (TextUtils.isEmpty(phone))
            return null;
        final Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        final Cursor phoneQueryCursor = contentResolver.query(uri, new String[]{PhoneLookup._ID}, null, null, null);
        if (phoneQueryCursor != null) {
            if (phoneQueryCursor.moveToFirst()) {
                String result = phoneQueryCursor.getString(phoneQueryCursor.getColumnIndex(PhoneLookup._ID));
                phoneQueryCursor.close();
                return result;
            }
            phoneQueryCursor.close();
        }
        return null;
    }

    public void openWhatsApp(Context context,String phone){

        final String formattedPhoneNumber = getFormattedPhoneNumber(context, phone);
        final String contactId = getContactIdFromPhoneNumber(context.getContentResolver(),phone);
        final String contactMimeTypeDataId = getContactMimeTypeDataId(context,contactId, "vnd.android.cursor.item/vnd.com.whatsapp.profile");
        if (!TextUtils.isEmpty(contactMimeTypeDataId)) {

            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + formattedPhoneNumber));
            intent.setPackage("com.whatsapp");
            context.startActivity(intent);

        } else{

            Toast.makeText(context, "cannot find this contact on whatsapp", Toast.LENGTH_SHORT).show();

        }
    }*/
}
