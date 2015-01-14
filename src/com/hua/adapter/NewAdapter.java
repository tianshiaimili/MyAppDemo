
package com.hua.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hua.bean.NewModle;
import com.hua.utils.LogUtils2;
import com.hua.view.NewItemView;

public class NewAdapter extends BaseAdapter {
    public List<NewModle> lists = new ArrayList<NewModle>();

    private String currentItem;
    private Context context;

    public void appendList(List<NewModle> list) {
    	LogUtils2.d("list---"+list.size());
        if (!lists.contains(list.get(0)) && list != null && list.size() > 0) {
            lists.addAll(list);
            LogUtils2.e("*********lists.size==***== " +lists.size());
        }
        notifyDataSetChanged();
    }

    public NewAdapter (Context tempContext){
    	if(tempContext != null){
    		this.context = tempContext;
    	}
    	
    }

    public void clear() {
        lists.clear();
        notifyDataSetChanged();
    }

    public void currentItem(String item) {
        this.currentItem = item;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NewItemView newItemView;

        if (convertView == null) {
//            newItemView = NewItemView_.build(context);
        	newItemView = new NewItemView(context);
        } else {
            newItemView = (NewItemView) convertView;
        }

        NewModle newModle = lists.get(position);
        if (newModle.getImagesModle() == null) {
            newItemView.setTexts(newModle.getTitle(), newModle.getDigest(),
                    newModle.getImgsrc(), currentItem);
        } else {
            newItemView.setImages(newModle);
        }

        return newItemView;
    }
}
