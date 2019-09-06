package com.basicsetup.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

/**
 * Created by pankaj on 6/8/15.
 */
public class SimpleAlertDialogUtils implements DialogInterface.OnClickListener {

    private String title = null;
    private String message = null;
    private String positiveButtonText = null;
    private String negativeButtonText = null;
    private DialogInterface.OnClickListener positiveClickListener = null;
    private DialogInterface.OnClickListener negativeClickListener = null;
    private String nutralButtonText = null;
    private DialogInterface.OnClickListener nutralClickListener = null;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
    }

    public void setNegativeButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
    }

    public void setPositiveClickListener(DialogInterface.OnClickListener positiveClickListener) {
        this.positiveClickListener = positiveClickListener;
    }

    public void setNegativeClickListener(DialogInterface.OnClickListener negativeClickListener) {
        this.negativeClickListener = negativeClickListener;
    }

    public void setNutralButtonText(String nutralButtonText) {
        this.nutralButtonText = nutralButtonText;
    }

    public void setNutralClickListener(DialogInterface.OnClickListener nutralClickListener) {
        this.nutralClickListener = nutralClickListener;
    }

    public void showDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if(!TextUtils.isEmpty(title)){
            builder.setTitle(title);
        }
        if(!TextUtils.isEmpty(message)){
            builder.setMessage(message);
        }

        builder.setPositiveButton(positiveButtonText, this);
        builder.setNegativeButton(negativeButtonText, this);
        builder.setNeutralButton(nutralButtonText,this);
        builder.create().show();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int which) {

        dialogInterface.dismiss();

        switch (which){
            case AlertDialog.BUTTON_POSITIVE:

                if(positiveClickListener!=null){
                    positiveClickListener.onClick(dialogInterface,which);
                }

                break;

            case AlertDialog.BUTTON_NEGATIVE:

                if(negativeClickListener!=null){
                    negativeClickListener.onClick(dialogInterface,which);
                }

                break;

            case AlertDialog.BUTTON_NEUTRAL:

                if(nutralClickListener!=null){
                    nutralClickListener.onClick(dialogInterface,which);
                }

                break;
        }
    }
}
