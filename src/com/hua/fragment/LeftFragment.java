package com.hua.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LeftFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		String con = "Testing...";
		if(savedInstanceState != null){
			Bundle mBundle = savedInstanceState;
			 con = mBundle.getString("textContent");
		}
		TextView mTextView = new TextView(getActivity());
		
		mTextView.setText(con);
		
		return mTextView;
	}
	
}
