package com.hua.activity;

import java.util.LinkedHashSet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hua.fragment.HomeFragment;
import com.hua.fragment.TestPullToRightFragment;
import com.hua.utils.FragmentUtils;
import com.hua.utils.FragmentUtils.FragmentTabBarController;
import com.hua.utils.FragmentUtils.FragmentTabSwitcher;
import com.hua.utils.FragmentUtils.FragmentTabSwitcherFeed;
import com.hua.utils.KeyboardUtils;
import com.hua.utils.LogUtils2;

public class MainActivityPhone extends BaseActivity {

	/**对UITab选择*/
	private FragmentTabSwitcher mFragmentTabSwitcher;
	private static final String TAB_HOME = "home";
	private static final String TAB_HOT = "hot";
	private static final String TAB_FIND = "find";
	private static final String TAB_ASSISTANT = "assistant";
	private static final String TAB_SETTING = "setting";
	//
	private View currentSelectedIcon;
	private FragmentTabBarController mFragmentTabBarController;
	private ViewGroup backLayout;
	private ImageView backButton;
	private ImageView backLogo;
	private RelativeLayout navigationBar;
	// search
	private ImageView searchButton;
	private boolean isCrew;// 应该是判断user是否点击了详细信息或者进入新的page
	private boolean isOnclickDetail;// 应该是判断user是否点击了详细信息或者进入新的page

	private ImageView filterButton;
	private long lastClickBackTime = 0;

	//
	private static final String CURRENT_FRAGMENT_TAG = "currentFragmentTag";
	// private SearchAutoCompleteFragment searchAutoCompleteFragment;
	// private SearchAutoCompleteCrewDetailFragment
	// searchAutoCompleteCrewDetailFragment;
	// private HomeSearchBarPopupWindow mHomeSearchBarPopupWindow = null;
	private EditText mEditText;
	// private SearchFragment mSearchFragment;
	// private SearchFragment2 mSearchFragment2;
	/**
	 * 用来显示自定的dialog
	 */
	// private SweetAlertDialog mSweetAlertDialog;
	//滑动的Menu...
//	private SlidingMenu mSlidingMenu;
	
	/**
	 * 当前版本号
	 */
	private int currentVersionCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		initSlidingMenu();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity_phone);
		initFragmentTabSwitcher() ;
		initUI();
	}

	private void initUI() {

//		mHomeSearchBarPopupWindow = new HomeSearchBarPopupWindow(this,
//				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		mHomeSearchBarPopupWindow.setOnSearchBarItemClickListener(new MyOnSearchBarItemClickListener());
		//
		mEditText = (EditText) findViewById(R.id.index_search_edit);
		mEditText.setInputType(InputType.TYPE_NULL);
//		mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				Toast.makeText(getApplicationContext(), "asdasda", 300).show();mSearchFragment = new SearchFragment();
//				startFragment(mSearchFragment);
//				mSearchFragment.setHostActivity(MainActivityPhone.this);
//				navigationBar.setVisibility(View.GONE);	
//			}
//		});
		mEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "asdasda", 300).show();mSearchFragment = new SearchFragment();
//				mSearchFragment = new SearchFragment();
//				mSearchFragment2 = new SearchFragment2();
//				startFragment(mSearchFragment2);
//				mSearchFragment.setHostActivity(MainActivityPhone.this);
				navigationBar.setVisibility(View.GONE);	
			}
		});
		//返回的布局
		backLayout = (ViewGroup) findViewById(R.id.main_activity_navigation_bar_back_layout);
		backButton = (ImageView) findViewById(R.id.main_activity_navigation_bar_back_img);
		backLogo = (ImageView) findViewById(R.id.main_activity_navigation_bar_back_logo);
		backLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(backButton.getVisibility() == View.VISIBLE){
					mFragmentTabSwitcher.popFragment();
					if(isCrew||isOnclickDetail){
						navigationBar.setVisibility(View.GONE);
						isCrew = false;
						isOnclickDetail = false;
					} else {
						navigationBar.setVisibility(View.VISIBLE);
					}
				}
				
			}
		});
		
		/**
		 * 下面是过滤的设置
		 */
		filterButton = (ImageView) findViewById(R.id.main_activity_navigation_bar_right_filter);
		filterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(MainActivityPhone.this,FilterPhoneActivity.class);
//				intent.putExtra(CURRENT_FRAGMENT_TAG, mFragmentTabSwitcher.getCurrentTabId());
//				startActivityForResult(intent, 200);
//				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		
		//test
//		initRightFragment();
		
		
	}
	
	//test right fragemnt
//	public void initRightFragment(){
//		mSlidingMenu.setSecondaryMenu(R.layout.main_right_layout);
//		FragmentTransaction mFragementTransaction = getSupportFragmentManager()
//				.beginTransaction();
//		Fragment mFrag = new LeftFragment();
//		mFragementTransaction.replace(R.id.main_right_fragment, mFrag);
//		mFragementTransaction.commit();
//	}
	
	
	/**
	 * 初始化 页面地下的tab选择器
	 */
	private void initFragmentTabSwitcher() {

		FragmentTabSwitcherFeed rootFragmentFeed = new FragmentTabSwitcherFeed() {

			@Override
			public Fragment newRootFragment(String tag) {
				if (TAB_HOME.equalsIgnoreCase(tag)) {
					/**
					 * 
					 */
//					return new TestPullToRightFragment();
					return new HomeFragment();

				} else if (TAB_HOT.equalsIgnoreCase(tag)) {

					/**
					 * 第二个 tab项
					 */
					// VODFragment vodFragment = new VODFragment();
					// return vodFragment;
					return new HomeFragment();

				} else if (TAB_FIND.equalsIgnoreCase(tag)) {
					
					return new HomeFragment();
					
				} else if (TAB_ASSISTANT.equalsIgnoreCase(tag)) {
					HomeFragment homeFragment = new HomeFragment();
					return homeFragment;
				} else if (TAB_SETTING.equalsIgnoreCase(tag)) {
					return new HomeFragment();
				}
				return null;
			}

			@Override
			public LinkedHashSet getRootFragmentTags() {
				/**
				 * linkHashset遍历会得出加入时的顺序
				 */
				LogUtils2.e("getRootFragmentTags********");
				return FragmentUtils.makeRootFragmentTags(new String[] {
						TAB_HOME, TAB_HOT, TAB_FIND, TAB_ASSISTANT,
						TAB_SETTING });
			}
			//
		};
		
		//initial the FragmentTabSwitcher 
		mFragmentTabSwitcher = new FragmentTabSwitcher(this, R.id.main_activity_fragment_container, rootFragmentFeed);
		///
		mFragmentTabBarController = new FragmentTabBarController(this, (LinearLayout)findViewById(R.id.tab_bar_container), true) {
			
			@Override
			public View getView(int position, View convertView, ViewGroup viewgroup,
					LayoutInflater layoutinflater) {
				LogUtils2.e("mFragmentTabBarController.getView=="+position);
				ImageView icon = null;
				TextView lable = null;
				////
				if(convertView == null){
					convertView = layoutinflater.inflate(R.layout.tab_bar_item, null);
					icon = (ImageView) convertView.findViewById(R.id.tab_bar_img);
					lable = (TextView) convertView.findViewById(R.id.tab_bar_text);
				}
				
				///
				switch (position) {
				case 0:
					
					icon.setImageResource(R.drawable.tab_tv_selector);
					lable.setText(R.string.tab_bar_tv_series);
					convertView.setTag(TAB_HOME);
					
					break;
				case 1:
					icon.setImageResource(R.drawable.tab_movie_selector);
					lable.setText(R.string.tab_bar_movies);
					convertView.setTag(TAB_HOT);
					
					break;
					
				case 2:
					icon.setImageResource(R.drawable.tab_history_selector);
					lable.setText(R.string.tab_bar_play_history);
					convertView.setTag(TAB_FIND);
					break;
				case 3:
					icon.setImageResource(R.drawable.tab_download_selector);
					lable.setText(R.string.tab_bar_download);
					convertView.setTag(TAB_ASSISTANT);
					break;
				case 4:
					icon.setImageResource(R.drawable.tab_setting_selector);
					lable.setText(R.string.tab_bar_settings);
					convertView.setTag(TAB_SETTING);
					break;
				}
				
				///
				convertView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String tag = (String)v.getTag();
						if(TAB_HOT.equals(tag) || TAB_ASSISTANT.equals(tag)){
							
							mFragmentTabSwitcher.switchTab(tag, false);
						}else{
							mFragmentTabSwitcher.switchTab(tag);
						}
						
						////
						setCurrentSelected(v);
						//
						if(TAB_HOT.equals(tag) && navigationBar != null){
							navigationBar.setVisibility(View.GONE);
						}else {
							
							navigationBar.setVisibility(View.VISIBLE);
						}
						KeyboardUtils.hideKeyboard(navigationBar);
						if(mFragmentTabSwitcher.isRootFragment()) {
							setBackButtonVisible(false);
						}
					}
				});
				return convertView;
			}
			
			@Override
			public int getCount() {
				return mFragmentTabSwitcher.getTabCount();
			}
		};
		
		///
		LogUtils2.d("createTabBar------------");
		mFragmentTabBarController.createTabBar();
		LogUtils2.d("createTabBar**********");
		///
		//
		mFragmentTabSwitcher.setSwitcherListener(new FragmentUtils.FragmentTabSwitcherListener() {
			@Override
			public void onTabSelected(String rootFragmentTag, int tabIndex, String previousRootFragmentTag, int previousTabIndex) {
				setCurrentSelected(mFragmentTabBarController.getItem(tabIndex));
			}
		});
		
		// /首先把TVfragmentTab生成
		mFragmentTabSwitcher.switchTab(TAB_HOME);

	}
	
	/***设置返回按钮的可见一否*/
	public void setBackButtonVisible(boolean visible) {
		backButton.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
	}
	

//	private void initSlidingMenu() {
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
//		setBehindContentView(R.layout.main_left_layout);// 设置左菜单，就是向右滑动显示出来的菜单
////		set
//		FragmentTransaction mFragementTransaction = getSupportFragmentManager()
//				.beginTransaction();
//		Fragment mFrag = new LeftFragment();
//		mFragementTransaction.replace(R.id.main_left_fragment, mFrag);
//		mFragementTransaction.commit();
//		// customize the SlidingMenu
//		mSlidingMenu = getSlidingMenu();
//		mSlidingMenu.setMode(SlidingMenu.LEFT);// 设置是左滑还是右滑，还是左右都可以滑，我这里左右都可以滑
//		mSlidingMenu.setShadowWidth(mScreenWidth / 50);// 设置阴影宽度 就是滑动时页边的阴影
//		mSlidingMenu.setShadowDrawable(R.drawable.shadow_left);// 设置左菜单阴影图片
//		mSlidingMenu.setBehindOffset(mScreenWidth / 5);// 设置菜单宽度
//		mSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
//		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);//设置滑动的模式
//		mSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadow_right);// 设置右菜单阴影图片
//		mSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
//		mSlidingMenu.setBehindScrollScale(0.333f);// 设置滑动时拖拽效果
//		
//		
//	}
	
	
	/**
	 * 大概设置是当选中某个tab时，让包含在此tab中的全部view都高亮显示（即被选中状态）
	 * @param selected
	 */
	private void setCurrentSelected(View selected) {
		// dim previous selected
		if(currentSelectedIcon != null) {
			currentSelectedIcon.setSelected(false);
			if(currentSelectedIcon instanceof ViewGroup) {
				ViewGroup vg = (ViewGroup) currentSelectedIcon;
				int childCount = vg.getChildCount();
				for(int i=0; i<childCount; i++) {
					vg.getChildAt(i).setSelected(false);
				}
			} 
		}
		// highlight current selected
		currentSelectedIcon = selected;
		currentSelectedIcon.setSelected(true);
		if(currentSelectedIcon instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) currentSelectedIcon;
			int childCount = vg.getChildCount();
			for(int i=0; i<childCount; i++) {
				vg.getChildAt(i).setSelected(true);
			}
		} 
	}
	
	/**检查更新*/
	public void checkIsUpdate(){
		
		PackageManager mPackageManager = getPackageManager();
		try {
			
			PackageInfo mPackageInfo = mPackageManager.getPackageInfo(getPackageName(), 0);
			String version = mPackageInfo.versionName;
			currentVersionCode = mPackageInfo.versionCode;
			/**
			 * 从网络获取最新的版本号 和 当前的比较
			 * 小就更新
			 */
			if(false){
				showUpdateDialog();
			}
		} catch (Exception e) {
			// TODO: handle exception
			LogUtils2.e("error=="+e.getMessage());
		}
		
	}
	
	/**
	 * 显示要更新窗口
	 */
	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("检测到新版本");
		builder.setMessage("是否下载更新?");
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				Intent it = new Intent(MainActivityPhone.this, NotificationUpdateActivity.class);
//				startActivity(it);
//				((BaseApplication)getApplication()).setDownload(true);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.show();
	}
	
	
	
	public FragmentTabSwitcher getmFragmentTabSwitcher() {
		return mFragmentTabSwitcher;
	}

	public void setmFragmentTabSwitcher(FragmentTabSwitcher mFragmentTabSwitcher) {
		this.mFragmentTabSwitcher = mFragmentTabSwitcher;
	}

	@Override
	public void onResume() {
		super.onResume();
		setBackButtonVisible(false);
		navigationBar = (RelativeLayout) findViewById(R.id.main_activity_navigation_bar);
		searchButton = (ImageView) findViewById(R.id.main_activity_navigation_bar_right_search);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				searchAutoCompleteFragment = new SearchAutoCompleteFragment();
//				startFragment(searchAutoCompleteFragment);
//				searchAutoCompleteFragment.setHostActivity(MainActivityPhone.this);
//				navigationBar.setVisibility(View.GONE);
				
//				int height = navigationBar.getHeight()
//						+ CommonTools.getStatusBarHeight(MainActivityPhone.this);
//				mHomeSearchBarPopupWindow.showAtLocation(navigationBar, Gravity.TOP, 0, height);
				
			}
		});
	}
	
	public  void startFragment(Fragment fragment) {
		mFragmentTabSwitcher.pushFragment(fragment);
	}
	
	
}
