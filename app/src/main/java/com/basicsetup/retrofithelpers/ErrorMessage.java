package com.tech20.mobiledelivery.retrofitclient;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fidel25 on 12/05/2017.
 */

public class ErrorMessage {

    public static final String BLANK_RESPONSE_MESSAGE="Blank Response Body";

    public static ErrorMessage errorMessage = null;
    public static ErrorMessage getBlankError(){
        if(errorMessage == null){
            errorMessage = new ErrorMessage(BLANK_RESPONSE_MESSAGE);
        }
        return errorMessage;
    }

    public ErrorMessage(){}
    public ErrorMessage(String msg){
        message = msg;
    }
    @SerializedName("Message")
    private String message = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
