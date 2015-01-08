package com.hua.view;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Random;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.hua.activity.R;
import com.hua.utils.LogUtils2;

public class BannerImageViewGif extends ImageView implements OnClickListener{

    /** 
     * 播放GIF动画的关键类 
     */  
    private Movie mMovie;  
  
    /** 
     * 开始播放按钮图片 
     */  
    private Bitmap mStartButton;  
  
    /** 
     * 记录动画开始的时间 
     */  
    private long mMovieStart;  
  
    /** 
     * GIF图片的宽度 
     */  
    private int mImageWidth;  
  
    /** 
     * GIF图片的高度 
     */  
    private int mImageHeight;  
  
    /** 
     * 图片是否正在播放 
     */  
    private boolean isPlaying;  
  
    /** 
     * 是否允许自动播放 
     */  
    private boolean isAutoPlay;  
  
    /** 
     * PowerImageView构造函数。 
     *  
     * @param context 
     */  
	public BannerImageViewGif(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	  /** 
     * PowerImageView构造函数。 
     *  
     * @param context 
     */  
    public BannerImageViewGif(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
    }

    /** 
     * PowerImageView构造函数，在这里完成所有必要的初始化操作。 
     *  
     * @param context 
     */ 
	public BannerImageViewGif(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PowerImageView);
		int resourceId  = getResourceId(typedArray, context, attrs);
		
		if(resourceId != 0){
			 // 当资源id不等于0时，就去获取该资源的流  
			InputStream inputStream = getResources().openRawResource(resourceId);
			// 使用Movie类对流进行解码  
			mMovie = Movie.decodeStream(inputStream);
			if(mMovie != null){
				
				//// 如果返回值不等于null，就说明这是一个GIF图片，下面获取是否自动播放的属性  
				isAutoPlay = typedArray.getBoolean(R.styleable.PowerImageView_auto_play, true);
				//
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				mImageWidth = bitmap.getWidth();  
                mImageHeight = bitmap.getHeight();  
                bitmap.recycle(); 
				
                if(!isAutoPlay){
                	
//                	mStartButton = BitmapFactory.decodeResource(getResources(), R.drawable.start_play);
                	setOnClickListener(this);
                }
                
			}
			
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(mMovie != null){
			// 如果是GIF图片则重写设定PowerImageView的大小
			setMeasuredDimension(mImageWidth, mImageHeight);
			
		}
	}
	
	@Override
	protected void onDraw(final Canvas canvas) {
		if(mMovie == null){
			  // mMovie等于null，说明是张普通的图片，则直接调用父类的onDraw()方法  
			super.onDraw(canvas);
		}else {
			
			 // mMovie不等于null，说明是张GIF图片  
			if(isAutoPlay){
				// 如果允许自动播放，就调用playMovie()方法播放GIF动画  
				playMovie(canvas);
				invalidate();
				LogUtils2.i("----after invalidate()");
				
			}else {
				LogUtils2.w("-----playMovie(canvas)");
				 // 不允许自动播放时，判断当前图片是否正在播放 
				if(isPlaying){
					
					  // 正在播放就继续调用playMovie()方法，一直到动画播放结束为止  
					if(playMovie(canvas)){
						isPlaying = false;
					}
					
					invalidate();
					
				}else {
					mMovie.setTime(0);
					mMovie.draw(canvas, 0, 0);
					int x = (mImageWidth - mStartButton.getWidth()) /2 ;
					int y = (mImageHeight - mStartButton.getHeight())/2 ;
					 canvas.drawBitmap(mStartButton, x, y, null);  
				}
				
			}
			
		}
	}
	
	
    /** 
     * 通过Java反射，获取到src指定图片资源所对应的id。 
     *  
     * @param a 
     * @param context 
     * @param attrs 
     * @return 返回布局文件中指定图片资源所对应的id，没有指定任何图片资源就返回0。 
     */  
    private int getResourceId(TypedArray a, Context context, AttributeSet attrs) {  
    	String imageName=null;
    	Field field2 = null;
    	try {  
            Field field = TypedArray.class.getDeclaredField("mValue");  
            field.setAccessible(true);  
            TypedValue typedValueObject = (TypedValue) field.get(a);  
            
            /**
             * 在drawable中获取图片id
             */
            
            Random random = new Random();
            int tempNum = random.nextInt(8);
             imageName = "welcome4";
             field2 = R.drawable.class.getDeclaredField(imageName);
//            field2.setAccessible(true);
            int id = field2.getInt(imageName);
            
            LogUtils2.d("id===="+id);
            
            return id;//typedValueObject.resourceId;  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (a != null) {  
                a.recycle();  
            }  
        }  
    	
        try {
			return field2.getInt("welcome0");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} 
    }

	@Override
	public void onClick(View v) {

	      if (v.getId() == getId()) {  
	            // 当用户点击图片时，开始播放GIF动画  
	            isPlaying = true;  
	            invalidate();  
	        }  
		
	}  

	   /** 
     * 开始播放GIF动画，播放完成返回true，未完成返回false。 
     *  
     * @param canvas 
     * @return 播放完成返回true，未完成返回false。 
     */  
    private boolean playMovie(Canvas canvas) {  
    	LogUtils2.i("----playMovie");
        long now = SystemClock.uptimeMillis();  
        if (mMovieStart == 0) {  
            mMovieStart = now;  
        }  
        /**
         * 动画变换需要的时间
         */
        int duration = mMovie.duration();  
//        LogUtils2.d("duration=="+duration);
        if (duration == 0) {  
            duration = 1000;  
        }  
        int relTime = (int) ((now - mMovieStart) % duration);  
        mMovie.setTime(relTime);  
        mMovie.draw(canvas, 0, 0);  
        /**
         * 如果当前时间减去动画开始时间大于了动画持续时间，那就说明动画播放完成了
         */
        if ((now - mMovieStart) >= duration) {  
            mMovieStart = 0;  
            return true;  
        }  
        return false;  
    }

}
