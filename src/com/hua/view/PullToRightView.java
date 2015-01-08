package com.hua.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.hua.utils.LogUtils2;

/**
 * 往上推动开门效果
 * 
 * @author way
 * 
 */
public class PullToRightView extends RelativeLayout {

	private Context mContext;

	private Scroller mScroller;

	private int mScreenWidth = 0;

	private int mScreenHeigh = 0;

	private int mLastDownX = 0;

	private int mCurryX;

	private int mDelX;

	private boolean mCloseFlag = false;

	private ImageView mImgView;
	
	private boolean isPullToRight;
	
	private int mDuration = 350;
	
	private int slideDistance ;//= 2*mScreenWidth/5;

	public PullToRightView(Context context) {
		super(context);
		mContext = context;
		setupView();
	}

	public PullToRightView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setupView();
	}

	private void setupView() {

		// 这个Interpolator你可以设置别的 我这里选择的是有弹跳效果的Interpolator
		Interpolator polator = new DecelerateInterpolator();
		mScroller = new Scroller(mContext, polator);

		// 获取屏幕分辨率
		WindowManager wm = (WindowManager) (mContext
				.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
//		DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
		mScreenHeigh = dm.heightPixels;
		mScreenWidth = dm.widthPixels;
		slideDistance = 2*mScreenWidth/5;

		// 这里你一定要设置成透明背景,不然会影响你看到底层布局
		this.setBackgroundColor(Color.argb(0, 0, 0, 0));
		mImgView = new ImageView(mContext);
		mImgView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mImgView.setScaleType(ImageView.ScaleType.FIT_XY);// 填充整个屏幕
		// mImgView.setImageResource(R.drawable.ic_launcher); // 默认背景
		mImgView.setBackgroundColor(Color.parseColor("#60000000"));
		addView(mImgView);
	}

	// 设置推动门背景
	public void setBgImage(int id) {
		mImgView.setImageResource(id);
	}

	// 设置推动门背景
	public void setBgImage(Drawable drawable) {
		mImgView.setImageDrawable(drawable);
	}

	// 推动门的动画
	public void startBounceAnim(int startX, int dX, int duration) {
		mScroller.startScroll(startX, 0, dX, 0, duration);
		invalidate();
	}
	
	/**
	 * 滑动到指定位置
	 * @param startX
	 * @param dX
	 * @param duration
	 */
	public void startPullToRightAnim(int startX, int dX, int duration) {
		isPullToRight = (isPullToRight ?false:true);
		LogUtils2.i("isPullToRight = "+isPullToRight);
		mScroller.startScroll(startX, 0, dX, 0, duration);
		invalidate();
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastDownX = (int) event.getX();
			LogUtils2.e("MotionEvent.ACTION_DOWN :mLastDownX== "+mLastDownX);
			if(isPullToRight && event.getX() < slideDistance){
				LogUtils2.e("Test---------------");
//				getParent().requestDisallowInterceptTouchEvent(false);
//				setClickable(false);
				//此时设置slide出来的view可点击
				return false;
			}
			return true;
		case MotionEvent.ACTION_MOVE:
			mCurryX = (int) event.getX();
			System.err.println("ACTION_MOVE=" + mCurryX);
			mDelX = mCurryX - mLastDownX;
//			LogUtils2.e("mCurryY== "+mCurryX);
//			LogUtils2.e("MotionEvent.ACTION_MOVE :mLastDownY== "+mLastDownX);
//			LogUtils2.e("mDelY== "+mDelX);
			// 只准上滑有效 
			//当向右滑动，且滑动的距离不大于屏幕的1/3时允许滑动，否则不行
			if (mDelX > 0 && mDelX <(slideDistance) && !isPullToRight) {
				//滑动过程中需要不停的调用这里绘画
				if(mDelX >= slideDistance){
//					scrollTo(-(slideDistance),0);
					mDelX = slideDistance;
				}else {
					scrollTo(-mDelX,0);
				}
			}else if(mDelX <= 0 && isPullToRight){
				//滑动过程中需要不停的调用这里绘画
				LogUtils2.e("**************");
//				scrollTo(mDelX,0);
				if(mDelX <= (-slideDistance)){
					LogUtils2.e("XXXXXXXXXXX");
				}else {
//					scrollBy(-mDelX/15, 0);
					scrollTo(-mDelX+(-slideDistance),0);
				}
//				scr
			}
			System.err.println("-------------  " + mDelX);

			break;
		case MotionEvent.ACTION_UP:
			mCurryX = (int) event.getX();
			mDelX = mCurryX - mLastDownX;
			if (mDelX > 0 && !isPullToRight) {
				if(mDelX >= slideDistance){
					isPullToRight = (isPullToRight ?false:true);
					mDelX = slideDistance;
				}else {
					startPullToRightAnim(this.getScrollX(), -slideDistance+(-this.getScrollX()), mDuration);
				}
				
//				if (Math.abs(mDelX) > mScreenHeigh / 2) {
//
//					// 向上滑动超过半个屏幕高的时候 开启向上消失动画
//					startBounceAnim(this.getScrollX(), mScreenHeigh, 450);
//					mCloseFlag = true;
//
//				} else {
//					// 向上滑动未超过半个屏幕高的时候 开启向下弹动动画
//					startBounceAnim(this.getScrollX(), -this.getScrollX(), 1000);
//
//				}
			}else if(mDelX < 0 || isPullToRight){
				LogUtils2.w("isPullToRight  =="+isPullToRight);
				if(mDelX <= -(slideDistance)){
					
				}else if(mDelX < 0 && isPullToRight){
					LogUtils2.w("(mDelX < 0 && isPullToRight)");
					startPullToRightAnim(this.getScrollX(), -this.getScrollX(), mDuration);
				}else if(isPullToRight){
					startPullToRightAnim(this.getScrollX(), -this.getScrollX(), mDuration);
				}else if(mDelX>0 && isPullToRight){
//					startPullToRightAnim(this.getScrollX(), -this.getScrollX(), 500);
				}
			}

			break;
		}
		return super.onTouchEvent(event);
	}

	public int Dp2Px(Context context, float dp) {  
	    final float scale = context.getResources().getDisplayMetrics().density;  
	    return (int) (dp * scale + 0.5f);  
	}  
	
	@Override
	public void computeScroll() {

//		LogUtils2.i("***********computeScroll************"); 
		
		if (mScroller.computeScrollOffset()) {
			LogUtils2.i("***********computeScroll************"); 
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			
			Log.i("scroller", "getCurrX()= " + mScroller.getCurrX()
					+ "     getCurrY()=" + mScroller.getCurrY()
					+ "  getFinalY() =  " + mScroller.getFinalY());
			// 不要忘记更新界面
			postInvalidate();
		} else {
			if (mCloseFlag) {
				this.setVisibility(View.GONE);
			}
		}
	}

}
