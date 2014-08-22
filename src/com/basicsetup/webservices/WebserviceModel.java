/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 * WebserviceModel.java
 * @Project:
 *		Rhythm
 * @Abstract:
 *		
 * @Copyright:
 *     		Copyright © 2014 Saregama India Ltd. All Rights Reserved
 *			Written under contract by Robosoft Technologies Pvt. Ltd.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
/*
 Created by pankaj and adarsha on 27-Mar-2014
 */

package com.basicsetup.webservices;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.basicsetup.helper.BooleanUtils;

public class WebserviceModel implements Parcelable {

	private long id;
	private int priority = WebserviceConstants.REQUEST_PRIORITY_MEDIUM;
	private int methodeType;
	private int requestType;
	private List<ParcelableNameValuePair> optionalParams;
	private ArrayList<String> urlParams;
	private boolean synced;
	private boolean isSyncInBackground;
	private Bundle extraData;
	private int errorCode;	
	private final int MAX_RETRY_COUNT=3;
	private int retryCount=MAX_RETRY_COUNT;
	
	public WebserviceModel() {

	}

	private WebserviceModel(Parcel parcel) {
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
		return extraData == null ? new Bundle() : extraData;
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

	public boolean isSyncInBackground() {
		return isSyncInBackground;
	}

	public void setSyncInBackground(boolean isSyncInBackground) {
		this.isSyncInBackground = isSyncInBackground;
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
	
	public void decrementRetryCount(){
		--retryCount;
	}
	public int getRetryCount(){
		return retryCount;
	}

	public void resetRetryCount() {
		retryCount = MAX_RETRY_COUNT;
	}
	public void readFromParcel(Parcel parcel) {
		extraData = parcel.readBundle();
		priority = parcel.readInt();
		methodeType = parcel.readInt();
		requestType = parcel.readInt();
		errorCode = parcel.readInt();
		retryCount = parcel.readInt();
		synced = BooleanUtils.intToBool(parcel.readInt());
		isSyncInBackground = BooleanUtils.intToBool(parcel.readInt());

		optionalParams = new ArrayList<ParcelableNameValuePair>();
		parcel.readList(optionalParams,
				ParcelableNameValuePair.class.getClassLoader());

		urlParams = new ArrayList<String>();
		parcel.readList(urlParams, String.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeBundle(extraData);
		dest.writeInt(priority);
		dest.writeInt(methodeType);
		dest.writeInt(requestType);
		dest.writeInt(errorCode);
		dest.writeInt(retryCount);
		dest.writeInt(BooleanUtils.boolToInt(synced));
		dest.writeInt(BooleanUtils.boolToInt(isSyncInBackground));
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + requestType;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebserviceModel other = (WebserviceModel) obj;
		if (requestType != other.requestType)
			return false;
		return true;
	}

}
