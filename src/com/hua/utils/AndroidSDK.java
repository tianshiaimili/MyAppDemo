/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.hua.utils;

import android.util.Log;

/**
 * 这是获取android版本号
 * @author Hua
 *
 */
public final class AndroidSDK
{

	  private static final String TAG = "AndroidSDK";
	    public static final int API_LEVEL = getApiLevel();
	
    public AndroidSDK()
    {
    }

    /**
     * get the android sysytem version
     * @return
     */
    public static int getApiLevel()
    {
        int INVALID_API_LEVEL = 0;
        int apiLevel = 0;
        try
        {
            apiLevel = android.os.Build.VERSION.SDK_INT;
//            apiLevel = Integer.parseInt(android.os.Build.VERSION.SDK);
            LogUtils2.w(TAG, (new StringBuilder("android.os.Build.VERSION.SDK = ")).append(apiLevel).toString());
        }
        catch(Exception e)
        {
            LogUtils2.w(TAG, (new StringBuilder("parseInt(android.os.Build.VERSION.SDK) failed : ")).append(e).toString());
        }
        if(apiLevel == 0)
            try
            {
            	/**
            	 * 通过反射获取版本号
            	 */
                apiLevel = ((Integer)ReflectionUtils.getStaticFieldValue(
                		ReflectionUtils.getField(
                				ReflectionUtils.getInnerClass("android.os.Build", "VERSION"), true, "SDK_INT"))).intValue();
                LogUtils2.w(TAG, (new StringBuilder("android.os.Build.VERSION.SDK_INT = ")).append(apiLevel).toString());
            }
            catch(Exception e)
            {
                LogUtils2.w(TAG, (new StringBuilder("reflect android.os.Build.VERSION.SDK_INT failed : ")).append(e).toString());
            }
        return apiLevel;
    }

  

}


/*
	DECOMPILATION REPORT

	Decompiled from: E:\WorkSoftwareTool\NowEclipse\workspace2\nmplayer\trunk\nmplayer\libs\supportlib.jar
	Total time: 169 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/