/*! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 * RHYSocialWrapper.java
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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @File:
 *		STASocialWrapper.java
 * @Project:
 *		Stardom
 * @Abstract:
 *		
 * @Copyright:
 *     		Copyright © 2012-2013, Viacom 18 Media Pvt. Ltd 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/*! Revision history (Most recent first)
 Created by vijayalaxmi on 13-Nov-2013
 */
package com.basicsetup.facebook;


public class RHYSocialWrapper {
	
	public static final String EXTRA_TYPE = "extra_type";
	public static final String EXTRA_ACCESS= "extra_access";
	public static final String EXTRA_STATE= "extra_state";
	public static final String EXTRA_ACTION = "extra_action";
	
	public static final int RES_FACEBOOK_LOGIN = 403; 
	public static final int RES_FACEBOOK_LOGOUT = 406;
	
    public static final int REQUEST_TYPE_LOGIN = 100;
    public static final int REQUEST_TYPE_SHARE = 101;
    public static final int REQUEST_TYPE_NONE = 102;
    public static final int REQUEST_TYPE_LOGOUT = 103;

    public static final int REQUEST_TYPE_TW_REPLY = 104;
    public static final int REQUEST_TYPE_TW_RETWEET = 105;
    public static final int REQUEST_TYPE_TW_FAV = 106;
    public static final int REQUEST_TYPE_TW_GETTIMELINE = 107;
    public static final int REQUEST_TYPE_TW_POST = 108;
    public static final int REQUEST_TYPE_TW_UN_RETWEET = 109;

    public static final String INTENT_PURPOSE = "intent_purpose";

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_DESC = "desc";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_LINK = "link";
    public static final String EXTRA_SHARETYPE = "share_type";

    public static RHYSocialWrapper mSocialSharing = null;

    public static final int TYPE_FACEBOOK = 100;
    public static final int TYPE_TWITTER = 101;
    public static final int TYPE_EMAIL = 102;
    public static final int TYPE_NONE = 103;

    public static final String BUNDLE_KEY_NAME = "name";
    public static final String BUNDLE_KEY_MESSAGE = "message";
    public static final String BUNDLE_KEY_DESCR = "picture";
    public static final String BUNDLE_KEY_LINK = "link";
    public static final String BUNDLE_KEY_TYPE = "type";
    public static final String BUNDLE_KEY_USERID = "userId";
    public static final String BUNDLE_KEY_POSTID = "postId";

    public static final String ACTION_LIKE = "like";
    public static final String ACTION_COMMENT = "comment";
    public static final String ACTION_SHARE = "share";
 

   
}
