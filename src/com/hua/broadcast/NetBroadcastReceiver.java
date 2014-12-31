package com.hua.broadcast;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import com.hua.utils.PreferenceUtils;

/**
 * to Listener the NwtWork status
 * @author zero
 *
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
	public static final String BOOT_COMPLETED_ACTION = "com.way.action.BOOT_COMPLETED";
	public static ArrayList<EventHandler> mListeners = new ArrayList<EventHandler>();

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)) {
			if (mListeners.size() > 0){
				// 通知接口完成加载
			
				for (EventHandler handler : mListeners) {
					handler.onNetChange();
				}
			}
		} else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
//			Intent xmppServiceIntent = new Intent(context, Service.class);
//			context.stopService(xmppServiceIntent);
		} else {
//			if (!TextUtils.isEmpty(PreferenceUtils.getPrefString(context,
//					PreferenceConstants.PASSWORD, ""))
//					&& PreferenceUtils.getPrefBoolean(context,
//							PreferenceConstants.AUTO_START, true)) {
//				Intent i = new Intent(context, XXService.class);
//				i.setAction(BOOT_COMPLETED_ACTION);
//				context.startService(i);
//			}
		}
	}

	public static abstract interface EventHandler {

		public abstract void onNetChange();
	}
}
