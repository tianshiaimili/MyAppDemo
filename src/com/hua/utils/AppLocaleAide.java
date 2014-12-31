/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.hua.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;

import com.hua.utils.LogUtils2;

public final class AppLocaleAide
{
    public static interface AppLocaleAideSupport
    {

        public abstract void onLocaleChanged();
    }


    public AppLocaleAide(AppLocaleAideSupport support)
    {
        if(support == null)
        {
            throw new IllegalArgumentException("AppLocaleAideSupport can NOT be null when using AppLocaleAide instance.");
        } else
        {
            mAppLocaleAideSupport = support;
            return;
        }
    }

    public static void setDefaultAppLocale(Locale defaultLocale)
    {
        Locale.setDefault(defaultLocale);
    }

    public static Locale getAppLocale(Context context)
    {
        String language = PreferenceManager.getDefaultSharedPreferences(context).getString("app_locale_language", "no_language");
        String country = PreferenceManager.getDefaultSharedPreferences(context).getString("app_locale_country", "NO_COUNTRY");
        String variant = PreferenceManager.getDefaultSharedPreferences(context).getString("app_locale_variant", "NO_VARIANT");
        if(language.equals("no_language") && country.equals("NO_COUNTRY") && variant.equals("NO_VARIANT"))
            return Locale.getDefault();
        else
            return new Locale(language, country, variant);
    }

    public static String getAppLocaleLanguage(Context context)
    {
        return getAppLocale(context).getLanguage().substring(0, 2).toLowerCase(Locale.US);
    }

    public static boolean isAppLocaleEn(Context context)
    {
        Locale loc = getAppLocale(context);
        boolean ret = loc.getLanguage().contains("en");
        LogUtils2.d( (new StringBuilder("Current app locale is ")).append(loc).toString());
        return ret;
    }

    public static boolean isAppLocaleZh(Context context)
    {
        Locale loc = getAppLocale(context);
        boolean ret = loc.getLanguage().contains("zh");
        LogUtils2.d( (new StringBuilder("Current app locale is ")).append(loc).toString());
        return ret;
    }

    public static String getLocalizedString(Context context, String stringName)
    {
        return context.getString(context.getResources().getIdentifier(stringName, "string", context.getPackageName()));
    }

    public boolean setAppLocale(Context context, Locale newLocale)
    {
        Locale resLocale = context.getResources().getConfiguration().locale;
        Locale selfLocale = mLocale != null ? mLocale : resLocale;
        LogUtils2.v( (new StringBuilder("selfLocale = ")).append(selfLocale).append(", newLocale = ").append(newLocale).append("; resLocale = ").append(resLocale).append(", defaultLocale =").append(Locale.getDefault()).append(", mLocale ").append(mLocale).toString());
        boolean changed;
        if(selfLocale.equals(newLocale))
        {
            LogUtils2.d( (new StringBuilder(String.valueOf(context.getClass().getSimpleName()))).append(" no need to setAppLocale, using same locale ").append(newLocale).toString());
            changed = false;
        } else
        {
            Resources res = context.getResources();
            Configuration cfg = res.getConfiguration();
            cfg.locale = newLocale;
            res.updateConfiguration(cfg, res.getDisplayMetrics());
            Locale.setDefault(cfg.locale);
            /**
             * �����޸ĺ����������
             */
            android.content.SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putString("app_locale_language", newLocale.getLanguage());
            editor.putString("app_locale_country", newLocale.getCountry());
            editor.putString("app_locale_variant", newLocale.getVariant());
            editor.commit();
            Log.w(TAG, (new StringBuilder(String.valueOf(context.getClass().getSimpleName()))).append(" setAppLocale from ").append(selfLocale).append(" to ").append(newLocale).toString());
            changed = true;
        }
        mLocale = newLocale;
        return changed;
    }

    private final boolean syncLocaleWithAppLocale(Context context, boolean triggerCallback)
    {
        boolean changed = setAppLocale(context, getAppLocale(context));
        if(changed && triggerCallback)
            mAppLocaleAideSupport.onLocaleChanged();
        return changed;
    }

    public final void syncLocaleWithAppLocaleOnCreate(Context context)
    {
//    	LogUtils2.v();
        LogUtils2.v( "syncLocaleWithAppLocaleOnCreate");
        syncLocaleWithAppLocale(context, false);
    }

    public final void syncLocaleWithAppLocaleOnResume(Context context)
    {
        LogUtils2.v("syncLocaleWithAppLocaleOnResume");
        syncLocaleWithAppLocale(context, true);
    }

    private static final String TAG = "com/pccw/gzmobile/app/AppLocaleAide.getSimpleName()";
    private final AppLocaleAideSupport mAppLocaleAideSupport;
    private Locale mLocale;
    public static final Locale SIMPLIFIED_CHINESE = new Locale("zh", "CN");
    public static final Locale TRADITIONAL_CHINESE_HK = new Locale("zh", "HK");
    public static final Locale TRADITIONAL_CHINESE_TW = new Locale("zh", "TW");
    public static final Locale ENGLISH_US = new Locale("en", "US");
    private static final String APP_LOCALE_SHARED_PREFERENCES_LANGUAGE_KEY = "app_locale_language";
    private static final String APP_LOCALE_SHARED_PREFERENCES_COUNTRY_KEY = "app_locale_country";
    private static final String APP_LOCALE_SHARED_PREFERENCES_VARIANT_KEY = "app_locale_variant";
    private static final String APP_LOCALE_SHARED_PREFERENCES_NO_LANGUAGE = "no_language";
    private static final String APP_LOCALE_SHARED_PREFERENCES_NO_COUNTRY = "NO_COUNTRY";
    private static final String APP_LOCALE_SHARED_PREFERENCES_NO_VARIANT = "NO_VARIANT";

}


/*
	DECOMPILATION REPORT

	Decompiled from: E:\WorkSoftwareTool\NowEclipse\workspace2\nmplayer\trunk\nmplayer\libs\supportlib.jar
	Total time: 31 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/