package com.basicsetup.webservices.parser.holder;

import android.os.Parcelable;

public abstract class HolderBase implements Parcelable{

	protected int code=-1;
	public void setCode(int code){
		this.code= code;	
	}
	public int getCode(){
		return code;
	}
}
