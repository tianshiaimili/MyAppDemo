package com.hua.fragment;

import com.hua.utils.LogUtils2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class TestFragment extends Fragment{

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils2.i("***onCreate***");
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils2.i("***onCreateView***");
		String con = "Testing...";
		if(savedInstanceState != null){
			Bundle mBundle = savedInstanceState;
			 con = mBundle.getString("textContent");
		}
		TextView mTextView = new TextView(getActivity());
		
		mTextView.setText(con);
		mTextView.setTextSize(50f);
		
		return mTextView;
	}

	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LogUtils2.i("***onViewCreated***");
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtils2.i("***onActivityCreated***");
	}
	

	@Override
	public void onStart() {
		super.onStart();
		LogUtils2.i("***onStart***");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		LogUtils2.i("***onResume***");
	}
	
}
