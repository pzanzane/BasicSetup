/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 * OnFBLogInListener.java
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

package com.basicsetup.facebook;

public interface OnFBLogInListener {
	public void onSessionOpen();
	public void onError(Exception exception);
}
