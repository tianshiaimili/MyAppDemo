/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.hua.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.io.PrintStream;

public class KeyboardUtils
{
    public static abstract class KeyboardVisibilityChangedListener
    {

        public void requestVisibilityUpdate()
        {
            mLayout.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        }

        public void removeVisibilityUpdate()
        {
            mLayout.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }

        public abstract void onKeyboardVisibilityChanged(boolean flag, int i, int j, int k);

        private ViewGroup mLayout;
        private android.view.ViewTreeObserver.OnGlobalLayoutListener listener;
        private boolean isKeyboardVisible;




        public KeyboardVisibilityChangedListener(ViewGroup layout)
        {
            isKeyboardVisible = false;
            mLayout = layout;
            listener = new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                public void onGlobalLayout()
                {
                    int layoutHeight = mLayout.getRootView().getHeight();
                    Rect visibleDisplay = new Rect();
                    mLayout.getWindowVisibleDisplayFrame(visibleDisplay);
                    int statusBarHeight = visibleDisplay.top;
                    int visibleDisplayHeight = visibleDisplay.bottom - visibleDisplay.top;
                    int keyboardHeight = layoutHeight - visibleDisplayHeight - statusBarHeight;
                    System.out.println((new StringBuilder(String.valueOf(layoutHeight))).append(", ").append(statusBarHeight).append(", ").append(visibleDisplayHeight).append(", ").append(keyboardHeight).toString());
                    if(keyboardHeight >= 165)
                    {
                        System.out.println("Keyboard appears.");
                        isKeyboardVisible = true;
                        onKeyboardVisibilityChanged(isKeyboardVisible, layoutHeight, visibleDisplayHeight, keyboardHeight);
                    } else
                    if(isKeyboardVisible)
                    {
                        System.out.println("Keyboard disappears.");
                        isKeyboardVisible = false;
                        onKeyboardVisibilityChanged(isKeyboardVisible, layoutHeight, visibleDisplayHeight, keyboardHeight);
                    }
                }

                final KeyboardVisibilityChangedListener this$1;

                
                {
                    this$1 = KeyboardVisibilityChangedListener.this;
//                    super();
                }
            };
        }
    }


    private KeyboardUtils()
    {
    }

    public static void setEditTextAutoFocus(EditText editText)
    {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    public static void setEditTextNotAutoFocus(View edittextParentLayout)
    {
        edittextParentLayout.setFocusable(true);
        edittextParentLayout.setFocusableInTouchMode(true);
    }

    public static void showKeyboard(EditText editText)
    {
        setEditTextAutoFocus(editText);
        InputMethodManager imm = (InputMethodManager)editText.getContext().getSystemService("input_method");
        imm.showSoftInput(editText, 1);
    }

    public static void hideKeyboard(View editTextParentView)
    {
        InputMethodManager imm = (InputMethodManager)editTextParentView.getContext().getSystemService("input_method");
        imm.hideSoftInputFromWindow(editTextParentView.getWindowToken(), 0);
    }

    public static void setSoftInputAdjustPan(Activity activity, boolean alwaysHidden)
    {
        int mode = 32;
        if(alwaysHidden)
            mode += 3;
        else
            mode += 5;
        activity.getWindow().setSoftInputMode(mode);
    }

    public static void setSoftInputAdjustResize(Activity activity, boolean alwaysHidden)
    {
        int mode = 16;
        if(alwaysHidden)
            mode += 3;
        else
            mode += 5;
        activity.getWindow().setSoftInputMode(mode);
    }

    public static void setSoftInputAdjustNothing(Activity activity, boolean alwaysHidden)
    {
        int mode = 48;
        if(alwaysHidden)
            mode += 3;
        else
            mode += 5;
        activity.getWindow().setSoftInputMode(mode);
    }

    public static final int MINIMUN_KEYBOARD_HEIGHT = 165;
}


/*
	DECOMPILATION REPORT

	Decompiled from: E:\WorkSoftwareTool\NowEclipse\workspace2\nmplayer\trunk\nmplayer\libs\supportlib.jar
	Total time: 980 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/