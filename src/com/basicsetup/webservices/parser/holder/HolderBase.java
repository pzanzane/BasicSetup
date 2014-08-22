/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 * HolderBase.java
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

package com.basicsetup.webservices.parser.holder;

import android.os.Parcelable;

public abstract class HolderBase implements Parcelable{	
	
	
	protected int code=-1;
	protected String mErrorMsg = "";
	
	public String getmErrorMsg() {
		return mErrorMsg;
	}
	public void setmErrorMsg(String mErrorMsg) {
		this.mErrorMsg = mErrorMsg;
	}
	public void setCode(int code){
		this.code= code;	
	}
	public int getCode(){		
		return code;
	}
}
