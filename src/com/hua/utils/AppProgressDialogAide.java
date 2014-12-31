/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.hua.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import java.io.PrintStream;
import java.util.Locale;

public final class AppProgressDialogAide
{

    private static final String TAG = "com/pccw/gzmobile/app/AppProgressDialogAide.getSimpleName()";
    private ProgressDialog mProgressDialog;
    private CharSequence mProgressDialogMessage;
    private boolean hasCanceled;
    private Object dialogCancelTag;
    private android.content.DialogInterface.OnCancelListener mProgressDialogCancelListener;
    private android.content.DialogInterface.OnDismissListener mProgressDialogDismissListener;
    private Activity mActivity;
    private ProgressDialogDismissListener mDismissListener;
    private ProgressDialogCancelListener mCancelListener;
	
    public static interface ProgressDialogCancelListener
    {

        public abstract void onCancel(DialogInterface dialoginterface);
    }

    public static interface ProgressDialogDismissListener
    {

        public abstract void onDismiss(DialogInterface dialoginterface);
    }


    public AppProgressDialogAide(Activity act)
    {
        mActivity = act;
    }

    public static ProgressDialog createProgressDialog(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable, android.content.DialogInterface.OnCancelListener cancelListener, int style)
    {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.setProgressStyle(style);
        return dialog;
    }

    private static String getDefaultLoadingMessage()
    {
        String msg = "";
        Locale loc = Locale.getDefault();
        String language = loc.getLanguage();
        String country = loc.getCountry();
        System.out.println((new StringBuilder("Default locale is ")).append(language).append("_").append(country).toString());
        if(language.equals("zh"))
        {
            if(country.equals("TW") || country.equals("HK"))
                msg = "\u8F09\u5165\u4E2D";
            else
                msg = "\u8F7D\u5165\u4E2D";
        } else
        {
            msg = "Loading...";
        }
        return msg;
    }

    public void setProgressDialogMessage(CharSequence message)
    {
        mProgressDialogMessage = message;
    }

    public void setProgressDialogDismissListener(ProgressDialogDismissListener listener)
    {
        mDismissListener = listener;
    }

    public void setProgressDialogCancelListener(ProgressDialogCancelListener listener)
    {
        mCancelListener = listener;
    }

    public final void showProgressDialog(boolean cancelable)
    {
        showProgressDialog(((Context) (mActivity)), cancelable);
    }

    public final void showProgressDialog(Context context, boolean cancelable)
    {
        if(mProgressDialogCancelListener == null)
        {
            Log.w(TAG, (new StringBuilder(String.valueOf(getClass().getName()))).append(" ProgressDialog OnCancelListener is null.").toString());
            mProgressDialogCancelListener = new android.content.DialogInterface.OnCancelListener() {

                public void onCancel(DialogInterface dialog)
                {
                    Log.d(AppProgressDialogAide.TAG, "ProgressDialog is onCancel().");
                    hasCanceled = true;
                    if(mCancelListener != null)
                        mCancelListener.onCancel(dialog);
                }

                final AppProgressDialogAide this$0;

            
            {
                this$0 = AppProgressDialogAide.this;
//                super();
            }
            }
;
        }
        if(mProgressDialogDismissListener == null)
        {
            Log.w(TAG, (new StringBuilder(String.valueOf(getClass().getName()))).append(" ProgressDialog OnDismissListener is null.").toString());
            mProgressDialogDismissListener = new android.content.DialogInterface.OnDismissListener() {

                public void onDismiss(DialogInterface dialog)
                {
                    Log.d(AppProgressDialogAide.TAG, "ProgressDialog is onDismiss().");
                    mProgressDialog = null;
                    if(mDismissListener != null)
                        mDismissListener.onDismiss(dialog);
                }

                final AppProgressDialogAide this$0;

            
            {
                this$0 = AppProgressDialogAide.this;
//                super();
            }
            }
;
        }
        if(mProgressDialog != null && mProgressDialog.isShowing())
        {
            Log.d(TAG, "ProgressDialog isShowing.");
            return;
        }
        if(!mActivity.isFinishing())
        {
            hasCanceled = false;
            if(mProgressDialogMessage == null)
                mProgressDialogMessage = getDefaultLoadingMessage();
            mProgressDialog = createProgressDialog(context, null, mProgressDialogMessage, true, cancelable, mProgressDialogCancelListener, 0);
            mProgressDialog.setOnDismissListener(mProgressDialogDismissListener);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.show();
            Log.d(TAG, (new StringBuilder(String.valueOf(getClass().getName()))).append(" show a new ProgressDialog.").toString());
        }
    }

    public final void dismissProgressDialog()
    {
        if(mProgressDialog == null || mActivity == null)
        {
            Log.v(TAG, (new StringBuilder(String.valueOf(getClass().getName()))).append(" has no ProgressDialog, no need to call dismissProgressDialog().").toString());
            return;
        }
        Log.d(TAG, (new StringBuilder(String.valueOf(getClass().getName()))).append(".dismissProgressDialog(), isFinishing() : ").append(mActivity.isFinishing()).toString());
        if(mProgressDialog.isShowing() && !mActivity.isFinishing())
            mProgressDialog.dismiss();
    }

    public void setProgressDialogCancelTag(Object cancelTag)
    {
        dialogCancelTag = cancelTag;
    }

    public final boolean progressDialogHasCanceled(Object cancelTag)
    {
        if(mProgressDialog == null || mActivity == null)
        {
            Log.w(TAG, (new StringBuilder(String.valueOf(getClass().getName()))).append(" progressDialogHasCanceled() no dialog or activity.").toString());
            return true;
        }
        Log.d(TAG, (new StringBuilder(String.valueOf(getClass().getName()))).append(" ProgressDialog has canceled ").append(hasCanceled).toString());
        boolean retval;
        if(hasCanceled)
            retval = true;
        else
        if(!mProgressDialog.isShowing())
        {
            retval = true;
        } else
        {
            boolean notSameTag = cancelTag != dialogCancelTag;
            Log.d(TAG, (new StringBuilder(String.valueOf(getClass().getName()))).append(" ProgressDialog tag is not the same ").append(notSameTag).toString());
            retval = notSameTag;
        }
        Log.w(TAG, (new StringBuilder(String.valueOf(getClass().getName()))).append(" progressDialogHasCanceled() returns ").append(retval).toString());
        return retval;
    }

    public void destroyProgressDialog()
    {
        mActivity = null;
        try
        {
            Log.v(TAG, (new StringBuilder(String.valueOf(getClass().getName()))).append(" destroyProgressDialog().").toString());
            dismissProgressDialog();
            mProgressDialog = null;
        }
        catch(Exception e)
        {
            Log.e(TAG, (new StringBuilder(String.valueOf(getClass().getName()))).append(" destroyProgressDialog() failed.").toString(), e);
        }
    }







}


/*
	DECOMPILATION REPORT

	Decompiled from: E:\WorkSoftwareTool\NowEclipse\workspace2\nmplayer\trunk\nmplayer\libs\supportlib.jar
	Total time: 774 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/