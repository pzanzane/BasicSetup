package com.basicsetup.webservices;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.basicsetup.helper.BooleanUtils;
import com.basicsetup.helper.ParcelableNameValuePair;

public class WebserviceModel implements Parcelable{

	private int priority = WebserviceConstants.REQUEST_PRIORITY_MEDIUM;
	private int methodeType;
	private int requestType; 
	private List<ParcelableNameValuePair> optionalParams;
	private ArrayList<String> urlParams;
	private boolean synced;
	private Bundle extraData;
	
	private int errorCode;
	
	public WebserviceModel(){
		
	}
	private WebserviceModel(Parcel parcel){
		readFromParcel(parcel);
	}
	public int getMethodeType() {
		return methodeType;
	}

	public void setMethodeType(int methodeType) {
		this.methodeType = methodeType;
	}

	public int getRequestType() {
		return requestType;
	}
	public void setRequestType(int requestType) {
		this.requestType = requestType;
	} 

	public List<ParcelableNameValuePair> getOptionalParams() {
		return optionalParams;
	}

	public void setListParams(List<ParcelableNameValuePair> listParams) {
		this.optionalParams = listParams;
	}

	public ArrayList<String> getUrlParams() {
		return urlParams;
	}
	public void setUrlParams(ArrayList<String> urlParams) {
		this.urlParams = urlParams;
	}
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public Bundle getExtraData() {
		return extraData;
	}
	public void setExtraData(Bundle extraData) {
		this.extraData = extraData;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	
	public boolean isSynced() {
		return synced;
	}
	public void setSynced(boolean synced) {
		this.synced = synced;
	}
	public void readFromParcel(Parcel parcel){ 
		extraData = parcel.readBundle();
		priority=parcel.readInt();
		methodeType = parcel.readInt();
		requestType = parcel.readInt();
		errorCode = parcel.readInt();
		synced = BooleanUtils.intToBool(parcel.readInt());
		
		optionalParams = new ArrayList<ParcelableNameValuePair>();
		parcel.readList(optionalParams,
				ParcelableNameValuePair.class.getClassLoader());
		
		urlParams = new ArrayList<String>();
		parcel.readList(urlParams,
				String.class.getClassLoader());
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		 dest.writeBundle(extraData);
		dest.writeInt(priority);
		dest.writeInt(methodeType);
		dest.writeInt(requestType);
		dest.writeInt(errorCode);
		dest.writeInt(BooleanUtils.boolToInt(synced));
		dest.writeList(optionalParams);
		dest.writeList(urlParams);
		
	}

	// this is used to regenerate your object. All Parcelables must have a
		// CREATOR that implements these two methods
		public static final Parcelable.Creator<WebserviceModel> CREATOR = new Parcelable.Creator<WebserviceModel>() {
			public WebserviceModel createFromParcel(Parcel in) {
				return new WebserviceModel(in);
			}

			public WebserviceModel[] newArray(int size) {
				return new WebserviceModel[size];
			}
		}; 
}
