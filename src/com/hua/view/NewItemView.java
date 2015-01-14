
package com.hua.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hua.activity.R;
import com.hua.bean.NewModle;
import com.hua.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

//@EViewGroup(R.layout.item_new)
public class NewItemView extends LinearLayout {

//    @ViewById(R.id.left_image)
    protected ImageView leftImage;

//    @ViewById(R.id.item_title)
    protected TextView itemTitle;

//    @ViewById(R.id.item_content)
    protected TextView itemConten;

//    @ViewById(R.id.article_top_layout)
    protected RelativeLayout articleLayout;

//    @ViewById(R.id.layout_image)
    protected LinearLayout imageLayout;

//    @ViewById(R.id.item_image_0)
    protected ImageView item_image0;

//    @ViewById(R.id.item_image_1)
    protected ImageView item_image1;

//    @ViewById(R.id.item_image_2)
    protected ImageView item_image2;

//    @ViewById(R.id.item_abstract)
    protected TextView itemAbstract;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    protected DisplayImageOptions options;
    
    public NewItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initContentView( context);
	}

	public NewItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initContentView(context);
	}

	public NewItemView(Context context) {
        super(context);
        initContentView(context);
    }

	public void initContentView(Context mContext){
		options = Options.getListOptions();
		View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_new, null);
		leftImage = (ImageView) contentView.findViewById(R.id.left_image);
		itemTitle = (TextView) contentView.findViewById(R.id.item_title);
		itemConten = (TextView) findViewById(R.id.item_content);
		articleLayout = (RelativeLayout) findViewById(R.id.article_top_layout);
		imageLayout = (LinearLayout) findViewById(R.id.layout_image);
		item_image0 = (ImageView) findViewById(R.id.item_image_0);
		item_image1 = (ImageView) findViewById(R.id.item_image_1);
		item_image2 = (ImageView) findViewById(R.id.item_image_2);
		itemAbstract = (TextView) findViewById(R.id.item_abstract);
	}
	
	
    public void setTexts(String titleText, String contentText, String imgUrl, String currentItem) {
        articleLayout.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.GONE);
        itemTitle.setText(titleText);
        if ("北京".equals(currentItem)) {

        } else {
            itemConten.setText(contentText);
        }
        if (!"".equals(imgUrl)) {
            leftImage.setVisibility(View.VISIBLE);
            imageLoader.displayImage(imgUrl, leftImage, options);
        } else {
            leftImage.setVisibility(View.GONE);
        }
    }

    public void setImages(NewModle newModle) {
        imageLayout.setVisibility(View.VISIBLE);
        articleLayout.setVisibility(View.GONE);
        itemAbstract.setText(newModle.getTitle());
        List<String> imageModle = newModle.getImagesModle().getImgList();
        imageLoader.displayImage(imageModle.get(0), item_image0, options);
        imageLoader.displayImage(imageModle.get(1), item_image1, options);
        imageLoader.displayImage(imageModle.get(2), item_image2, options);
    }
}
