package com.hua.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.hua.utils.LogUtils2;

/**
 * 
 * @author Hua
 * 
 */
public class SplashActivity extends BaseActivity {

	private int PAGER_COUNT = 3;
	private boolean isfirstInstall;
	private ViewPager viewPager;
	private SharedPreferences preferences;
	private boolean isSendToHander;
	/**
	 * 读取文件解析出来的json格式数据的 list
	 */

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 5:

				viewPager.setAdapter(new MViewPagerAdapter());
				viewPager.setCurrentItem(0);
				break;
			default:
				break;
			}

		};

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		preferences = getPreferences(MODE_PRIVATE);
		isfirstInstall = preferences.getBoolean("isfirstInstall", false);
		if (!isfirstInstall) {
			LogUtils2.d("***********************");
			setContentView(R.layout.first_install_page);
			//
			LogUtils2.w("111111111");
			viewPager = (ViewPager) findViewById(R.id.viewpager);
			handler.obtainMessage(5).sendToTarget();
			// SharedPreferences preferences =getPreferences(MODE_PRIVATE);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("isfirstInstall", true);
			editor.commit();
		} else {

			LogUtils2.d("+++++++++++++");
			Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
			// Intent intent = new Intent(getBaseContext(),
			// WelcomeActivity.class);
			startActivity(intent);
			finish();

		}
	}

	class MViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return PAGER_COUNT;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			// super.destroyItem(container, position, object);
			container.removeView(((View) object));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		// //

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		// /
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			LogUtils2.w("position==" + position);
			if (position == 2) {
				LogUtils2.w("position***=" + position);
				View view = LayoutInflater.from(getBaseContext()).inflate(
						R.layout.first_install_last_pager_item_, null);
				Button button = (Button) view.findViewById(R.id.button);
				button.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getBaseContext(),
								MainActivityPhone.class);
						startActivity(intent);
						finish();
					}
				});
				container.addView(view);
				return view;
			} else {

				ImageView imageView = new ImageView(getBaseContext());
				imageView.setImageResource(R.drawable.xianjian2);
				container.addView(imageView);
				return imageView;
			}
			// return super.instantiateItem(container, position);
		}
	}

}
