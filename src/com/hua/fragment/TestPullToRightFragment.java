package com.hua.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hua.activity.R;
import com.hua.view.PullToRightView;

public class TestPullToRightFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		String con = "Testing...";
		if(savedInstanceState != null){
			Bundle mBundle = savedInstanceState;
			 con = mBundle.getString("textContent");
		}
//		TextView mTextView = new TextView(getActivity());
//		
//		mTextView.setText(con);
		
		PullToRightView pullToRightView = new PullToRightView(getActivity());
		View contentView = inflater.inflate(R.layout.pulltoright_help, null);
		Button mButton = (Button) contentView.findViewById(R.id.testButton);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), ((Button)v).getText(), 300).show();
			}
		});
		
		return contentView;
	}
	
}
