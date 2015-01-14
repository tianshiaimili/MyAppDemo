package com.hua.fragment;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hua.bean.NewModle;
import com.hua.utils.LogUtils2;
import com.hua.widget.swipelistview.SwipeListView;
import com.hua.widget.viewimage.Animations.SliderLayout;
import com.hua.widget.viewimage.SliderTypes.BaseSliderView;
import com.hua.widget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
/**
 * 今日头条的fragment
 * @author zero
 *
 */
public class HeadNewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
OnSliderClickListener{

	
	/**
	 * 头部的横幅滑动布局
	 */
    protected SliderLayout mDemoSlider;
    /**the SwipeRefreshLayout which can pull to refresh*/
    protected SwipeRefreshLayout swipeLayout;
    /**
     * 整个布局的listview
     */
//    @ViewById(R.id.listview)
    protected SwipeListView mListView;
//    @ViewById(R.id.progressBar)
    protected ProgressBar mProgressBar;
    protected HashMap<String, String> url_maps;

    protected HashMap<String, NewModle> newHashMap;

//    @Bean
    protected NewAdapter newAdapter;
    protected List<NewModle> listsModles;
    private int index = 0;
    private boolean isRefresh = false;
    
    
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


	//SwipeRefreshLayout.OnRefreshListener
	@Override
	public void onRefresh() {
		
	}


	///OnSliderClickListener
	@Override
	public void onSliderClick(BaseSliderView slider) {
		
	}
	
}
