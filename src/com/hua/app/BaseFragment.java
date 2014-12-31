/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.hua.app;

import java.lang.reflect.Method;
import java.util.Locale;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hua.utils.AppLocaleAide;
import com.hua.utils.AppProgressDialogAide;

// Referenced classes of package com.pccw.gzmobile.app:
//            AppLocaleAide, AppProgressDialogAide

public class BaseFragment extends Fragment
    implements AppLocaleAide.AppLocaleAideSupport
{
	

    private static final String TAG = "com/pccw/gzmobile/app/BaseFragment.getSimpleName()";
    private View mFragmentView;
    private boolean mFragmentHasOnStopBefore;
    private AppLocaleAide mAppLocaleAide;
    private AppProgressDialogAide mAppDialogAide;
    private SparseArray mManagedDialogs;
	
    private static class ManagedDialog
    {

        Dialog mDialog;
        Bundle mArgs;

        private ManagedDialog()
        {
        }

        ManagedDialog(ManagedDialog manageddialog)
        {
            this();
        }
    }


    public BaseFragment()
    {
        mAppLocaleAide = new AppLocaleAide(this);
    }

    public void holdFragmentView(View view)
    {
        mFragmentView = view;
    }

    public View retrieveFragmentView()
    {
        return mFragmentView;
    }

    public View createOrRetrieveFragmentView(LayoutInflater inflater, ViewGroup container, int fragmentLayoutResId)
    {
        View view = retrieveFragmentView();
        if(view == null)
        {
            view = inflater.inflate(fragmentLayoutResId, container, false);
            holdFragmentView(view);
        }
        return view;
    }

    private String getFragmentLogId()
    {
        return (new StringBuilder(String.valueOf(getClass().getSimpleName()))).append(" with tag ").append(getTag()).toString();
    }

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        Log.d(TAG, (new StringBuilder(String.valueOf(getFragmentLogId()))).append(" onAttach() : ").append(getActivity()).toString());
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, (new StringBuilder(String.valueOf(getFragmentLogId()))).append(" onCreate()").toString());
        mAppLocaleAide.syncLocaleWithAppLocaleOnCreate(getActivity());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(TAG, (new StringBuilder(String.valueOf(getFragmentLogId()))).append(" onCreateView()").toString());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, (new StringBuilder(String.valueOf(getFragmentLogId()))).append(" onActivityCreated(), isFragmentRecreatedWithHoldView() ").append(isFragmentRecreatedWithHoldView()).toString());
        mAppDialogAide = new AppProgressDialogAide(getActivity());
    }

    protected void onRestart()
    {
        Log.d(TAG, (new StringBuilder(String.valueOf(getFragmentLogId()))).append(" onRestart()").toString());
    }

    public void onStart()
    {
        if(mFragmentHasOnStopBefore)
            onRestart();
        super.onStart();
        Log.d(TAG, (new StringBuilder(String.valueOf(getFragmentLogId()))).append(" onStart()").toString());
    }

    public void onResume()
    {
        super.onResume();
        Log.d(TAG, (new StringBuilder(String.valueOf(getFragmentLogId()))).append(" onResume()").toString());
        mAppLocaleAide.syncLocaleWithAppLocaleOnResume(getActivity());
    }

    public void onPause()
    {
        super.onPause();
        Log.d(TAG, (new StringBuilder(String.valueOf(getFragmentLogId()))).append(" onPause()").toString());
    }

    public void onStop()
    {
        super.onStop();
        Log.d(TAG, (new StringBuilder(String.valueOf(getFragmentLogId()))).append(" onStop()").toString());
        mFragmentHasOnStopBefore = true;
    }

    public void onDestroyView()
    {
        super.onDestroyView();
        if(mFragmentView != null)
        {
            android.view.ViewParent parent = mFragmentView.getParent();
            if(parent instanceof ViewGroup)
                ((ViewGroup)parent).removeView(mFragmentView);
        }
        Log.i(TAG, (new StringBuilder(String.valueOf(getFragmentLogId()))).append(" onDestroyView()").toString());
        mAppDialogAide.destroyProgressDialog();
        destroyManagedDialogs();
    }

    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, (new StringBuilder(String.valueOf(getFragmentLogId()))).append(" onDestroy()").toString());
    }

    public void onDetach()
    {
        super.onDetach();
        mFragmentView = null;
        Log.d(TAG, (new StringBuilder(String.valueOf(getFragmentLogId()))).append(" onDetach() : ").append(getActivity()).toString());
    }

    public boolean onHostActivityBackPressed()
    {
        return false;
    }

    public boolean isFragmentRecreatedWithHoldView()
    {
        return mFragmentHasOnStopBefore && mFragmentView != null;
    }

    private Dialog createDialog(Integer dialogId, Bundle state, Bundle args)
    {
        Dialog dialog = onCreateDialog(dialogId.intValue(), args);
        if(dialog == null)
        {
            return null;
        } else
        {
            dispatchDialogOnCreate(dialog, state);
            return dialog;
        }
    }

    private static void dispatchDialogOnCreate(Dialog dialog, Bundle state)
    {
        try
        {
//            Method dispatchOnCreateMethod = android/app/Dialog.getDeclaredMethod("dispatchOnCreate", new Class[] {
//                android/os/Bundle
        	Method dispatchOnCreateMethod = null;/*dialog.getgetDeclaredMethod("dispatchOnCreate", new Class[] {
                    Bundle.class*/
//            });
            if(!dispatchOnCreateMethod.isAccessible())
                dispatchOnCreateMethod.setAccessible(true);
            dispatchOnCreateMethod.invoke(dialog, new Object[] {
                state
            });
        }
        catch(Exception exception) { }
    }

    public final void showDialog(int id)
    {
        showDialog(id, null);
    }

    public final boolean showDialog(int id, Bundle args)
    {
        if(mManagedDialogs == null)
            mManagedDialogs = new SparseArray();
        ManagedDialog md = (ManagedDialog)mManagedDialogs.get(id);
        if(md == null)
        {
            md = new ManagedDialog(null);
            md.mDialog = createDialog(Integer.valueOf(id), null, args);
            if(md.mDialog == null)
                return false;
            mManagedDialogs.put(id, md);
        }
        md.mArgs = args;
        onPrepareDialog(id, md.mDialog, md.mArgs);
        md.mDialog.show();
        return true;
    }

    protected Dialog onCreateDialog(int id)
    {
        return null;
    }

    protected Dialog onCreateDialog(int id, Bundle args)
    {
        return onCreateDialog(id);
    }

    protected void onPrepareDialog(int id, Dialog dialog)
    {
        dialog.setOwnerActivity(getActivity());
    }

    protected void onPrepareDialog(int id, Dialog dialog, Bundle args)
    {
        onPrepareDialog(id, dialog);
    }

    public final void dismissDialog(int id)
    {
        if(mManagedDialogs == null)
            throw missingDialog(id);
        ManagedDialog md = (ManagedDialog)mManagedDialogs.get(id);
        if(md == null)
        {
            throw missingDialog(id);
        } else
        {
            md.mDialog.dismiss();
            return;
        }
    }

    public final void removeDialog(int id)
    {
        if(mManagedDialogs != null)
        {
            ManagedDialog md = (ManagedDialog)mManagedDialogs.get(id);
            if(md != null)
            {
                md.mDialog.dismiss();
                mManagedDialogs.remove(id);
            }
        }
    }

    private IllegalArgumentException missingDialog(int id)
    {
        return new IllegalArgumentException((new StringBuilder("no dialog with id ")).append(id).append(" was ever ").append("shown via Activity#showDialog").toString());
    }

    private final void destroyManagedDialogs()
    {
        if(mManagedDialogs != null)
        {
            int numDialogs = mManagedDialogs.size();
            for(int i = 0; i < numDialogs; i++)
            {
                ManagedDialog md = (ManagedDialog)mManagedDialogs.valueAt(i);
                if(md.mDialog.isShowing())
                    md.mDialog.dismiss();
            }

            mManagedDialogs.clear();
            mManagedDialogs = null;
        }
    }

    private boolean isHostActivityNull()
    {
        if(getActivity() == null)
        {
            Log.e(TAG, (new StringBuilder(String.valueOf(getClass().getSimpleName()))).append(".getActivity() returns null").toString());
            return true;
        } else
        {
            return false;
        }
    }

    private boolean isFragmentViewNull()
    {
        if(getView() == null)
        {
            Log.e(TAG, (new StringBuilder(String.valueOf(getClass().getSimpleName()))).append(".getView() returns null").toString());
            return true;
        } else
        {
            return false;
        }
    }

    public final View findActivityViewById(int id)
    {
        if(isHostActivityNull())
            return null;
        else
            return getActivity().findViewById(id);
    }

    public final View findViewById(int id)
    {
        if(isFragmentViewNull())
            return null;
        else
            return getView().findViewById(id);
    }

    public Context getApplicationContext()
    {
        if(isHostActivityNull())
            return null;
        else
            return getActivity().getApplicationContext();
    }

    public final Application getApplication()
    {
        if(isHostActivityNull())
            return null;
        else
            return getActivity().getApplication();
    }

    public Intent getIntent()
    {
        if(isHostActivityNull())
            return null;
        else
            return getActivity().getIntent();
    }

    public void finish()
    {
        if(isHostActivityNull())
        {
            return;
        } else
        {
            getActivity().finish();
            return;
        }
    }

    public boolean isFinishing()
    {
        if(isHostActivityNull())
            return false;
        else
            return getActivity().isFinishing();
    }

    public void setProgressDialogMessage(CharSequence message)
    {
        if(mAppDialogAide != null)
            mAppDialogAide.setProgressDialogMessage(message);
    }

    public void setProgressDialogDismissListener(AppProgressDialogAide.ProgressDialogDismissListener listener)
    {
        if(mAppDialogAide != null)
            mAppDialogAide.setProgressDialogDismissListener(listener);
    }

    public void setProgressDialogCancelListener(AppProgressDialogAide.ProgressDialogCancelListener listener)
    {
        if(mAppDialogAide != null)
            mAppDialogAide.setProgressDialogCancelListener(listener);
    }

    public final void showProgressDialog(boolean cancelable)
    {
        if(mAppDialogAide != null)
            mAppDialogAide.showProgressDialog(cancelable);
    }

    public final void showProgressDialog(Context context, boolean cancelable)
    {
        if(mAppDialogAide != null)
            mAppDialogAide.showProgressDialog(context, cancelable);
    }

    public final void dismissProgressDialog()
    {
        if(mAppDialogAide != null)
            mAppDialogAide.dismissProgressDialog();
    }

    public void setProgressDialogCancelTag(Object cancelTag)
    {
        if(mAppDialogAide != null)
            mAppDialogAide.setProgressDialogCancelTag(cancelTag);
    }

    public final boolean progressDialogHasCanceled(Object cancelTag)
    {
        if(mAppDialogAide != null)
            return mAppDialogAide.progressDialogHasCanceled(cancelTag);
        else
            return true;
    }

    public void setAppLocale(Context context, Locale newLocale)
    {
        mAppLocaleAide.setAppLocale(context, newLocale);
    }

    public void onLocaleChanged()
    {
    }

    public void toast(String text, boolean shortDuration)
    {
        Toast.makeText(getActivity(), text, shortDuration ? 0 : 1).show();
    }

    public void toast(int textResId, boolean shortDuration)
    {
        Toast.makeText(getActivity(), getResources().getString(textResId), shortDuration ? 0 : 1).show();
    }


}


/*
	DECOMPILATION REPORT

	Decompiled from: E:\WorkSoftwareTool\NowEclipse\workspace2\nmplayer\trunk\nmplayer\libs\supportlib.jar
	Total time: 480 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/