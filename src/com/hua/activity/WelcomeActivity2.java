package com.hua.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.hua.utils.LogUtils2;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 刚打开时显示的页面，后台在加载数据    啦啦啦德玛西亚   啦啦啦啦啦啦的玛西亚
 * 提交ok？lalalallalas大三大四的
 * @author Hua
 *
 */
public class WelcomeActivity2 extends BaseActivity {


	private TextView welcomeTextView;
	private boolean isOver;
	private Animation animation1;
	int count;
//	private MyLoadImageTask task;
//	private MyImageLoader myImageLoader;
	/**
	 * 读取文件解析出来的json格式数据的 list
	 */
//	private List<HomeBannerData> mHomeBannerDatas = null;
//	private List<TempAppData> mTempAppDatas = null;
	private boolean isSendToHander;
	
	Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			
			switch (msg.what) {
			case 5:
				try {
//					Thread.sleep(2000);
//					welcomeTextView.startAnimation(animation1);
					welcomeTextView.setVisibility(View.VISIBLE);
					   AnimatorSet set = new AnimatorSet();
				        //包含平移、缩放和透明度动画
				        set.playTogether(
//				                ObjectAnimator.ofFloat(welcomeTextView, "translationX", 0, 80),
				                ObjectAnimator.ofFloat(welcomeTextView, "translationY", 0, 80),
				                ObjectAnimator.ofFloat(welcomeTextView, "scaleX", 0f, 1f),
				                ObjectAnimator.ofFloat(welcomeTextView, "scaleY", 0f, 1f),
				                ObjectAnimator.ofFloat(welcomeTextView, "alpha", 0f, 1));
				        //动画周期为500ms
							set.setInterpolator(OvershootInterpolator.class.newInstance());
				        set.setDuration(1000).start();
				        set.addListener(new AnimatorListener() {
							
							@Override
							public void onAnimationStart(Animator arg0) {
								
							}
							
							@Override
							public void onAnimationRepeat(Animator arg0) {
								
							}
							
							@Override
							public void onAnimationEnd(Animator arg0) {
								// TODO Auto-generated method stub
								LogUtils2.d("+++++++++++++");
								Intent intent = new Intent(getBaseContext(), MainActivityPhone.class);
//								Bundle extras = new Bundle();
//								extras.put
//								if(mHomeBannerDatas != null){
//									
//									intent.putExtra("appName",mHomeBannerDatas.get(0).getAppName());
//								}
//								Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
								startActivity(intent);
								overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
								finish();
							}
							
							@Override
							public void onAnimationCancel(Animator arg0) {
								// TODO Auto-generated method stub
								
							}
						});
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 3:
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
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome_activity2);
//		myImageLoader = new MyImageLoader(getApplicationContext());
		welcomeTextView = (TextView) findViewById(R.id.welcome_text);
//		animation1 = AnimationUtils.loadAnimation(getBaseContext(), R.animator.shake);
		
		new Thread(){
			
			public void run() {
				try {
					Thread.sleep(3000);
					
//					welcomeTextView.setAlpha(0.0f);
//					handler.obtainMessage().sendToTarget();
					handler.sendMessage(handler.obtainMessage(5));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			};
			
		}.start();
	}
	
	
	
}
