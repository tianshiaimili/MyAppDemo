
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
    public static List<NewModle> lists = new ArrayList<NewModle>();
	private String currentItem;
    private Context context;
    private static NewAdapter mNewAdapter;
    private int oldIndex = -1;

    public void appendList(List<NewModle> list,int newIndex) {
    	LogUtils2.d("list---"+list.size());
    	LogUtils2.i("newIndex = "+newIndex+"   oldIndex = "+oldIndex);
        if (!lists.contains(list.get(0)) && list != null && list.size() > 0 && newIndex != oldIndex) {
            lists.addAll(list);
            oldIndex = newIndex;
            LogUtils2.e("*********lists.size==***== " +lists.size());
        }
        notifyDataSetChanged();
    }

    public static NewAdapter getNewAdapter(Context tempContext){
    	
    	if(mNewAdapter == null){
    		mNewAdapter = new NewAdapter(tempContext);
    	}
    	return mNewAdapter;
    	
    }
    
    public NewAdapter (Context tempContext){
    	if(tempContext != null){
    		context = tempContext;
    	}
    	
    	if(lists == null){
    		LogUtils2.i("***********NewAdapter.lists==null******");
    		 lists = new ArrayList<NewModle>();
    	}else {
    		LogUtils2.d("***********NewAdapter.lists != null******");
			lists = getLists();
		}
    	
    }


    public List<NewModle> getLists() {
		return lists;
	}

	public void setLists(List<NewModle> lists) {
		this.lists = lists;
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
//        	LogUtils2.e("NewItemView---context ="+context);
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
