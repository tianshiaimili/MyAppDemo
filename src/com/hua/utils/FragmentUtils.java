/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   FragmentUtils.java

package com.hua.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.hua.activity.R;
import com.hua.app.BaseFragment;

// Referenced classes of package com.pccw.gzmobile.app:
//            BaseFragment

public class FragmentUtils
{
	 private static final String TAG = "com/pccw/gzmobile/app/FragmentUtils.getSimpleName()";
	
    private static abstract class AbsFragmentTabSwitcher
    {
        public abstract void switchTab(String s, boolean flag);

        public static final int INVALID_TAB_INDEX = -1;
        /**主要的FragmentActivity,主要由MainActivityPhone传进来 调用*/
        protected FragmentActivity mFragmentActivity;
        /**这个是针对 要引入fragment的布局中的对应layout的的ID（这里是FrameLayout） 例如在MainActivityPhone中的R.id.main_activity_fragment_container*/
        protected int mContainerId;
        /** UI底部Tab的点击响应类，主要由在MainActivityPhone中的 
         * new FragmentTabSwitcher(this, R.id.main_activity_fragment_container, rootFragmentFeed); 使用*/
        protected FragmentTabSwitcherFeed mSwitcherFeed;
        /**这个是用来存放UI底部Tab的Map，例如这里主要有 Home 、 Hot、Find、Assistan、Setting这几个tab*/
        protected LinkedHashMap mRootFragmentTags;
        /**这个主要是针对每个UI底部的tab的对应的一个Map，
         * 主要到时方便在某个UITab点击进入新的fragment后，可以做统计，
         * 然后返回的时候方便操作，例如 一次性就返回到RootTabFragmrnt
         * （这里的RootTabFragment主要是指Home、Hot、、等等UI底部的Tab）*/
        protected final LinkedHashMap mTabStacks = new LinkedHashMap();
        /**UI底部Tab的总个数*/
        private final int mTabCount;
        protected Fragment mCurrentFragment;
        protected String mCurrentRootFragmentTag;
        protected String mPreviousRootFragmentTag;
        protected FragmentTabSwitcherListener mSwitcherListener;
    	
        public void setSwitcherListener(FragmentTabSwitcherListener listener)
        {
            mSwitcherListener = listener;
        }

        public FragmentActivity getFragmentActivity()
        {
            return mFragmentActivity;
        }
        
        /**返回底部UI的Tab个数*/
        public int getTabCount()
        {
            return mTabCount;
        }

        public Fragment getCurrentTabFragment()
        {
            return mCurrentFragment;
        }

        public String getCurrentTabId()
        {
            return mCurrentRootFragmentTag;
        }

        /**
         * ��ȡ��ǰѡ�е��±�
         * @return
         */
        public int getCurrentTabIndex()
        {
            return ((Integer)mRootFragmentTags.get(mCurrentRootFragmentTag)).intValue();
        }

        public String getPreviousTabId()
        {
            return mPreviousRootFragmentTag;
        }

        /**
         * ��ȡ��һ����index
         * @return
         */
        public int getPreviousTabIndex()
        {
            return mPreviousRootFragmentTag == null ? -1 : ((Integer)mRootFragmentTags.get(mPreviousRootFragmentTag)).intValue();
        }

        /**
         * 判断是不是点击了同一个tab
         * @return
         */
        protected boolean isSameTab()
        {
        	LogUtils2.d("mPreviousRootFragmentTag== "+mPreviousRootFragmentTag);
        	LogUtils2.d("mCurrentRootFragmentTag== "+mCurrentRootFragmentTag);
            return mPreviousRootFragmentTag != null && mPreviousRootFragmentTag.equals(mCurrentRootFragmentTag);
        }

        /**
         * 这个相当于UI下面的tab 选项 
         * 主要是当点击时后 做创建等操作
         */
        public void switchTab(String rootFragmentTag)
        {
            switchTab(rootFragmentTag, true);
        }

        /**AbsFragmentTabSwitcher 的方法 在FragmentTabSwitcher中的构造器中调用*/
        public AbsFragmentTabSwitcher(FragmentActivity act, int containerId, FragmentTabSwitcherFeed feed)
        {
            mFragmentActivity = act;
            if(mFragmentActivity == null){
            	throw new RuntimeException("FragmentActivity can NOT be null.");
            }
            mContainerId = containerId;
            if(mContainerId == 0)
                throw new RuntimeException("Container id can NOT be 0.");
            mSwitcherFeed = feed;
            if(mSwitcherFeed == null){
                throw new RuntimeException("FragmentTabSwitcherFeed can NOT be null.");
            }
            /**UI底部Tab项的 集合*/
            LinkedHashSet tagSet = feed.getRootFragmentTags();
            LogUtils2.d("tagSet===="+tagSet.size());
            if(tagSet == null || tagSet.size() == 0){
                throw new RuntimeException("FragmentTabSwitcherFeed.getRootFragmentTags() returns null or size is 0.");
            }
            mRootFragmentTags = new LinkedHashMap(3);
            Iterator tagSetIterator = tagSet.iterator();
            for(int index = 0; tagSetIterator.hasNext(); index++)
            {
                String tag = (String)tagSetIterator.next();
                if(tag == null){
                    throw new RuntimeException("tab root fragment tag can NOT be null.");
                }
                if(tag.equals("")){
                    throw new RuntimeException("tab root fragment tag can NOT be empty.");
                }
                mRootFragmentTags.put(tag, Integer.valueOf(index));
            }

            Log.w(FragmentUtils.TAG, (new StringBuilder("FragmentTabSwitcher root fragment tags : ")).append(mRootFragmentTags).toString());
            mTabCount = mRootFragmentTags.size();
            String tag;
            for(tagSetIterator = tagSet.iterator(); tagSetIterator.hasNext(); mTabStacks.put(tag, new LinkedList()))
                tag = (String)tagSetIterator.next();

            Log.w(FragmentUtils.TAG, (new StringBuilder("FragmentTabSwitcher tab stacks : ")).append(mTabStacks).toString());
        }
    }

    /**
     * 这个主要是对UI底部的Tab的点击做对应的操作
     * 例如 改变UITab的颜色 生成新的对应UITab的Fragment内容
     * @author Hua
     *
     */
    public static abstract class FragmentTabBarController
    {


        private Context mContext;
        private boolean mHorizontalTabBar;
        /**底部UI的TabLinearlayout*/
        private LinearLayout mLinearLayout;
        private LayoutInflater mLayoutInflater;
        /**判断是否已经创建过UITab*/
        private boolean built;
        /**
         * 这个主要是对UI底部的Tab的点击做对应的操作
         * 例如 改变UITab的颜色 生成新的对应UITab的Fragment内容
         *@param context 传入的上下文Context
         *@param container 在main_activity_phone中的底部的Linearlayout
         *@param horizontalTabBar 是否为水平方向设计 ，垂直方向是平板的设计
         *@param FILL_PARENT = -1; 
         *@param LinearLayout.LayoutParams.MATCH_PARENT = -1; 所以 -1表示的是layout_height的大小
         *@param WRAP_CONTENT = -2;
         */
        public FragmentTabBarController(Context context, ViewGroup container, boolean horizontalTabBar)
        {
            mContext = context;
            mHorizontalTabBar = horizontalTabBar;
            mLinearLayout = new LinearLayout(mContext);
            mLayoutInflater = LayoutInflater.from(mContext);
//            
//            public static final int FILL_PARENT = -1;
//            public static final int MATCH_PARENT = -1;
//            public static final int WRAP_CONTENT = -2;
            
            if(mHorizontalTabBar)
            {
                mLinearLayout.setOrientation(0);
                int tabBarHeight = -2;
                if(container.getLayoutParams().height == -1){
                    tabBarHeight = -1;
                }
                mLinearLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tabBarHeight));
                mLinearLayout.setGravity(16);
                container.addView(mLinearLayout);
            } else
            {
                mLinearLayout.setOrientation(1);
                int tabBarWidth = -2;
                if(container.getLayoutParams().width == -1)
                    tabBarWidth = -1;
                mLinearLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(tabBarWidth, -1));
                mLinearLayout.setGravity(1);
                ScrollView sv = new ScrollView(mContext);
                sv.setVerticalScrollBarEnabled(false);
                sv.setVerticalFadingEdgeEnabled(false);
//                if(AndroidSDK.API_LEVEL >= 9)
                if(android.os.Build.VERSION.SDK_INT >= 9)
                    sv.setOverScrollMode(2);
                sv.addView(mLinearLayout);
                container.addView(sv);
            }
        }
    	
        /**
         * 刷新并创建底部的UITab
         * @param rebuild
         */
        private void refreshTabBar(boolean rebuild)
        {
            if(rebuild){
                mLinearLayout.removeAllViews();
            }
            int count = getCount();
            int childCount = mLinearLayout.getChildCount();
            View convertView = null;
            View tabBarItemView = null;
            for(int i = 0; i < count; i++)
            {
                if(childCount > i)
                    convertView = mLinearLayout.getChildAt(i);
                LogUtils2.w("begin getview in refreshTabBar+++++++++++");
                tabBarItemView = getView(i, convertView, mLinearLayout, mLayoutInflater);
                LogUtils2.w("tabBarItemView=="+tabBarItemView);
                if(rebuild)
                    if(mHorizontalTabBar)
                    {
                    	LogUtils2.e("mHorizontalTabBar=="+mHorizontalTabBar);
                    	LogUtils2.e("tabBarItemView==***"+tabBarItemView.getClass());
                    	LogUtils2.e("tabBarItemView.getLayoutParams().height=="+
                    			tabBarItemView.getLayoutParams().WRAP_CONTENT);
                    	
                    	android.widget.LinearLayout.LayoutParams ll = 
                    			new android.widget.LinearLayout.LayoutParams(0, tabBarItemView.getLayoutParams().WRAP_CONTENT);
                    	LogUtils2.e("ll=="+ll);
                        ll.weight = 1.0F;
                        mLinearLayout.addView(tabBarItemView, ll);
                    } else
                    {
                        mLinearLayout.addView(tabBarItemView);
                    }
            }

        }
        
        /**创建底部的UI_Tab*/
        public void createTabBar()
        {
            if(!built)
            {
                refreshTabBar(true);
                built = true;
            } else
            {
                throw new IllegalStateException("Tab bar has been created. No need to create again.");
            }
        }

        public void refreshTabBar()
        {
            if(!built)
            {
                throw new IllegalStateException("Tab bar has not been created.");
            } else
            {
                refreshTabBar(false);
                return;
            }
        }
        
        /**返回底部UITab的个数*/
        public abstract int getCount();
        /**这里是对UI底部每个Tab的里面的view做操作，
         * 例如每个tab中的ImageView和TextView的背景颜色
         * @param i 对应tab的index 例如 home 是0 、hot 是 1、等
         * @param view 填充到底部UITab的LinearLayout的子View 这里是tab_bar_item.xml
         * @param viewgroup 底部UITab的Linearlayout
         * @param LayoutInflater 用来装载子view
         */
        public abstract View getView(int i, View view, ViewGroup viewgroup, LayoutInflater layoutinflater);

        /**返回当前UITab对应的view，主要是为改变背景颜色和字体颜色*/
        public final View getItem(int position)
        {
            if(!built)
                throw new IllegalStateException("Tab bar has not been created.");
            else
                return mLinearLayout.getChildAt(position);
        }

    }

    /**
     * 
     * @author Hua
     *
     */
    public static class FragmentTabSwitcher extends AbsFragmentTabSwitcher
    {

    	/**FragmentTabSwitcher 的构造器 用来初始化 UI底部的Tab选项*/
        public FragmentTabSwitcher(FragmentActivity act, int containerId, FragmentTabSwitcherFeed feed)
        {
            super(act, containerId, feed);
        }
    	
        /**
         * 这个相当于UI下面的tab 选项 
         * 主要是当点击时后 做创建等操作
         */
        public void switchTab(String rootFragmentTag, boolean skipSameTab)
        {
        	LogUtils2.v("********switchTab*********  = "+rootFragmentTag);
        	
        	if(getCurrentTabStack() != null){
        		
        		LogUtils2.e("switchTab=size= "+getCurrentTabStack().size());
        		LogUtils2.d("switchTab=getFirst= "+getCurrentTabStack().getFirst());
        		LogUtils2.e("switchTab=getLast= "+getCurrentTabStack().getLast());
        	}
        	
            if(rootFragmentTag == null)
                throw new NullPointerException("Fragment tag can NOT be null.");
            mPreviousRootFragmentTag = mCurrentRootFragmentTag;
            mCurrentRootFragmentTag = rootFragmentTag;
           /**
            * 点击同一个tab的时候的处理
            */
            LogUtils2.i("skipSameTab==  "+skipSameTab +"   isSameTab()== "+isSameTab());
            if(skipSameTab && isSameTab())
            {
            	LogUtils2.w("same**************");
                Log.w(FragmentUtils.TAG, (new StringBuilder(String.valueOf(getClass().getSimpleName()))).append(" no need to switch same tab : ").append(mCurrentRootFragmentTag).toString());
                if(mSwitcherListener != null)
                {
                    int tabIndex = getCurrentTabIndex();
                    int preTabIndex = getPreviousTabIndex();
                    Log.d(FragmentUtils.TAG, (new StringBuilder("onTabSelected : ")).append(mCurrentRootFragmentTag).append(", ").append(tabIndex).append(", ").append(mPreviousRootFragmentTag).append(", ").append(preTabIndex).toString());
                    mSwitcherListener.onTabSelected(mCurrentRootFragmentTag, tabIndex, mPreviousRootFragmentTag, preTabIndex);
                }
                return;
            }
           
            
            LogUtils2.e("getCurrentTabStack().size()== "+getCurrentTabStack().size());
            if(getCurrentTabStack().size() == 0){
            	LogUtils2.i("getCurrentTabStack().size() == 0");
                pushFragment(true, new Fragment[] {
                    mSwitcherFeed.newRootFragment(rootFragmentTag)
                });
            } else {
                pushFragment(false, new Fragment[] {
                    peekTopmostFragment()
                });
            }
            if(mSwitcherListener != null)
            {
                int tabIndex = getCurrentTabIndex();
                int preTabIndex = getPreviousTabIndex();
                Log.d(FragmentUtils.TAG, (new StringBuilder("onTabSelected : ")).append(mCurrentRootFragmentTag).append(", ").append(tabIndex).append(", ").append(mPreviousRootFragmentTag).append(", ").append(preTabIndex).toString());
                mSwitcherListener.onTabSelected(mCurrentRootFragmentTag, tabIndex, mPreviousRootFragmentTag, preTabIndex);
            }
        }

        /**
         * 返回的是上一个tab （即下面的五个选项中之一）的对应的LinkedList（例如上一次选中的是seting 然后现在选中的是move，则返回的是seting的LinkedList包含内容）
         * LinkedList（这个里面封装的是在当前的tab中如settingfragment页面中打开了多少个子页 例如点击了settingfragment中的
         * 一个item然后进入下一层 则 LinkedList就加一层 里面的值 类似 setting - 1）
         * @return
         */
        private LinkedList getCurrentTabStack()
        {
        	LogUtils2.w("mCurrentRootFragmentTag==="+mCurrentRootFragmentTag);
            if(mCurrentRootFragmentTag == null)
            {
                Log.w(FragmentUtils.TAG, "Please call switchTab() first before using other methods.");
                return null;
            }
            LinkedList tab = (LinkedList)mTabStacks.get(mCurrentRootFragmentTag);
            if(tab == null)
            {
                Log.w(FragmentUtils.TAG, (new StringBuilder("Can NOT find the the tab with key ")).append(mCurrentRootFragmentTag).toString());
                return null;
            } else
            {
            	
//            	LogUtils2.i("tab.szie=="+tab.size()+"   tab.get(0)== "+tab.get(0));
                return tab;
        }
        }

        /**
         * 
         * @return
         */
        public boolean isCurrentTabStackEmpty()
        {
            LinkedList tab = getCurrentTabStack();
            boolean ret = tab == null || tab.isEmpty();
            if(ret)
                Log.w(FragmentUtils.TAG, "Current tab stack is empty.");
            return ret;
        }

        /**
         * ��getCurrentTabStack������ȡ����һ��fragment
         * -----当按下返回键后 把当前的fragment去掉，把之前的换做为第一个----
         * 这里就例如 当点击了第一个tab 而且还点击了tab（move）里面的item进入到下一层了，
         * 当点击另外一个tab（setting）后，再店家上一个tab时（move），返回进入item时的样子
         * 因为LinkedList 是先进后出型的，所以最后点击的item 就在LinkedList的第一个 所以直接getFirst()即可获取
         * @return
         */
        public Fragment peekTopmostFragment()
        {
            if(isCurrentTabStackEmpty())
                return null;
            else
                return mFragmentActivity.getSupportFragmentManager().findFragmentByTag((String)getCurrentTabStack().getFirst());
            	//
        }

        /**
         * 添加fragment
         * @param add 是否新创建添加
         * @param fragments 
         */
        private void pushFragment(boolean add, Fragment fragments[])
        {
            FragmentManager manager = mFragmentActivity.getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
//            ft.setCustomAnimations(R.anim.push_left_in,0,0,R.anim.push_left_out);
            Fragment afragment[];
            int j = (afragment = fragments).length;
            for(int i = 0; i < j; i++)
            {
                Fragment fragment = afragment[i];
                LogUtils2.i("****mCurrentFragment== "+mCurrentFragment);
                if(mCurrentFragment != null)
                {
//                    Log.d(FragmentUtils.TAG, (new StringBuilder("Detach fragment ")).append(mCurrentFragment.getTag()).toString());
                   /**
                    */
                    LogUtils2.i("pushFragment*******");
//                    transaction.detach(mCurrentFragment);
                    transaction.hide(mCurrentFragment);
                }
                if(add)
                {
                	LogUtils2.i("add********=="+add);
                    String fragmentTag = (new StringBuilder(String.valueOf(mCurrentRootFragmentTag))).append("-").append(getCurrentTabStack().size()).toString();
                    transaction.add(mContainerId, fragment, fragmentTag);
//                    mFragmentActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                    ft.setCustomAnimations(R.anim.push_left_in,0,0,R.anim.push_left_out);
                    getCurrentTabStack().addFirst(fragmentTag);
                } else
                {
                	LogUtils2.e("pushFragment_______attach______");
                	LogUtils2.i("add******false**=="+add);
//                    Log.d(FragmentUtils.TAG, (new StringBuilder("Attach fragment ")).append(fragment.getTag()).toString());
                	LogUtils2.e("test  if  the fragment is null  = "+ fragment);
                    transaction.attach(fragment);
                    
                    if(!fragment.isAdded()){
                    	 String fragmentTag = (new StringBuilder(String.valueOf(mCurrentRootFragmentTag))).append("-").append(getCurrentTabStack().size()).toString();
                    	 transaction.add(mContainerId, fragment, fragmentTag);
                    }else {
						transaction.show(fragment);
					}
                   
//                    ft.replace(R.id.main_activity_fragment_container, fragment);
//                    ft.setCustomAnimations(R.anim.push_left_in,0,0,R.anim.push_left_out);
                }
                mCurrentFragment = fragment;
            }

            transaction.commit();
        }

        public void pushFragment(Fragment fragment)
        {
            pushFragment(true, new Fragment[] {
                fragment
            });
        }

        public void pushFragments(Fragment fragments[])
        {
            pushFragment(true, fragments);
        }

        /**
         * 在tab中的当前子fragment
         * @return
         */
        private Fragment popTopmostFragment()
        {
            if(isCurrentTabStackEmpty())
            {
                return null;
            } else
            {
                String tag = (String)getCurrentTabStack().removeFirst();
                Log.d(FragmentUtils.TAG, (new StringBuilder("Remove fragment ")).append(tag).toString());
                return mFragmentActivity.getSupportFragmentManager().findFragmentByTag(tag);
            }
        }

        /**
         * �Ƴ�ǰ���׸�fragment
         * @return
         */
        public Fragment popFragment()
        {
            FragmentManager manager = mFragmentActivity.getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
//            ft.setCustomAnimations(R.anim.push_left_in,0,0,R.anim.push_left_out);
//            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.remove(popTopmostFragment());
            mCurrentFragment = peekTopmostFragment();
            if(mCurrentFragment.isAdded()){
            	ft.show(mCurrentFragment);
            }
//            ft.attach(mCurrentFragment);
            Log.d(FragmentUtils.TAG, (new StringBuilder("Attach fragment ")).append(mCurrentFragment.getTag()).toString());
            ft.commit();
            return mCurrentFragment;
        }

        public Fragment popToRootFragment()
        {
            int size = getCurrentTabStack().size();
            if(size > 1)
            {
                FragmentManager manager = mFragmentActivity.getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                for(int i = 0; i < size - 1; i++)
                    ft.remove(popTopmostFragment());

                mCurrentFragment = peekTopmostFragment();
                if(mCurrentFragment.isAdded()){
                	ft.show(mCurrentFragment);
                }
//                ft.attach(mCurrentFragment);
                Log.d(FragmentUtils.TAG, (new StringBuilder("Attach fragment ")).append(mCurrentFragment.getTag()).toString());
                ft.commit();
            }
            return mCurrentFragment;
        }

        public boolean isRootFragment()
        {
            return getCurrentTabStack().size() == 1;
        }

        /**
         * ������һ��fragmenttab
         */
        public void onFragmentActivityBackPressed()
        {
            onFragmentActivityBackPressed(null);
        }

        
        public void onFragmentActivityBackPressed(Runnable superOnBackPressed)
        {
            Fragment f = getCurrentTabFragment();
            LogUtils2.d("Currtent=size=="+getCurrentTabStack().size());
            LogUtils2.d("getCurrentTabFragment=="+getCurrentTabFragment());
            if(f == null)
            {
            	LogUtils2.e("f==="+f);
                if(superOnBackPressed != null)
                    superOnBackPressed.run();
                else
                    mFragmentActivity.finish();
            } else if((f instanceof BaseFragment) && ((BaseFragment)f).onHostActivityBackPressed())
                System.out.println("Fragment handled onBackPressed().");
            else
            if(isRootFragment())
                mFragmentActivity.finish();
            else{
                LogUtils2.e("onFragmentActivityBackPressed888");
            	popFragment();
            	}
            }

        public void switchTab(String s)
        {
            super.switchTab(s);
        }

        public Fragment getCurrentTabFragment()
        {
            return super.getCurrentTabFragment();
        }

        public  int getPreviousTabIndex()
        {
            return super.getPreviousTabIndex();
        }

        public  void setSwitcherListener(FragmentTabSwitcherListener fragmenttabswitcherlistener)
        {
            super.setSwitcherListener(fragmenttabswitcherlistener);
        }

        public  String getCurrentTabId()
        {
            return super.getCurrentTabId();
        }

        public  int getTabCount()
        {
            return super.getTabCount();
        }

        public  int getCurrentTabIndex()
        {
            return super.getCurrentTabIndex();
        }

        public  FragmentActivity getFragmentActivity()
        {
            return super.getFragmentActivity();
        }

        public  String getPreviousTabId()
        {
            return super.getPreviousTabId();
        }

    }
    /////////////////////////

    /**
     * 一个对UI地下的tab部分 的点击做对应的操作的接口
     * @author yue
     *
     */
    public static interface FragmentTabSwitcherFeed
    {

        public abstract Fragment newRootFragment(String s);

        public abstract LinkedHashSet getRootFragmentTags();
    }

    public static interface FragmentTabSwitcherListener
    {

        public abstract void onTabSelected(String s, int i, String s1, int j);
    }

    /**
     * 
     * @author Hua
     *
     */
    public static class FragmentTabSwitcherWithoutZorder extends AbsFragmentTabSwitcher
    {

        public void switchTab(String rootFragmentTag, boolean skipSameTab)
        {
            if(rootFragmentTag == null)
                throw new NullPointerException("Fragment tag can NOT be null.");
            mPreviousRootFragmentTag = mCurrentRootFragmentTag;
            mCurrentRootFragmentTag = rootFragmentTag;
            if(skipSameTab && isSameTab())
            {
                Log.w(FragmentUtils.TAG, (new StringBuilder(String.valueOf(getClass().getSimpleName()))).append(" no need to switch same tab : ").append(mCurrentRootFragmentTag).toString());
                if(mSwitcherListener != null)
                {
                    int tabIndex = getCurrentTabIndex();
                    int preTabIndex = getPreviousTabIndex();
                    Log.d(FragmentUtils.TAG, (new StringBuilder("onTabSelected : ")).append(mCurrentRootFragmentTag).append(", ").append(tabIndex).append(", ").append(mPreviousRootFragmentTag).append(", ").append(preTabIndex).toString());
                    mSwitcherListener.onTabSelected(mCurrentRootFragmentTag, tabIndex, mPreviousRootFragmentTag, preTabIndex);
                }
                return;
            }
            Log.d(FragmentUtils.TAG, (new StringBuilder(String.valueOf(getClass().getSimpleName()))).append(" switch tab : ").append(rootFragmentTag).toString());
            FragmentManager manager = mFragmentActivity.getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            if(mCurrentFragment != null)
            {
                Log.d(FragmentUtils.TAG, (new StringBuilder("detach fragment : ")).append(mCurrentFragment).toString());
                ft.detach(mCurrentFragment);
            } else
            if(manager.findFragmentByTag(rootFragmentTag) != null)
                Log.w(FragmentUtils.TAG, "Strange case : current is null but the coming one is not null. Have you re-assigned the switcher instance?");
            mCurrentFragment = manager.findFragmentByTag(rootFragmentTag);
            if(mCurrentFragment == null)
            {
                mCurrentFragment = mSwitcherFeed.newRootFragment(rootFragmentTag);
                Log.d(FragmentUtils.TAG, (new StringBuilder("add fragment : ")).append(mCurrentFragment).toString());
                ft.add(mContainerId, mCurrentFragment, rootFragmentTag);
            } else
            {
                Log.d(FragmentUtils.TAG, (new StringBuilder("re-attach fragment : ")).append(mCurrentFragment).toString());
                ft.attach(mCurrentFragment);
            }
            ft.commit();
            if(mSwitcherListener != null)
            {
                int tabIndex = getCurrentTabIndex();
                int preTabIndex = getPreviousTabIndex();
                Log.d(FragmentUtils.TAG, (new StringBuilder("onTabSelected : ")).append(mCurrentRootFragmentTag).append(", ").append(tabIndex).append(", ").append(mPreviousRootFragmentTag).append(", ").append(preTabIndex).toString());
                mSwitcherListener.onTabSelected(mCurrentRootFragmentTag, tabIndex, mPreviousRootFragmentTag, preTabIndex);
            }
        }

        public  void switchTab(String s)
        {
            super.switchTab(s);
        }

        public  Fragment getCurrentTabFragment()
        {
            return super.getCurrentTabFragment();
        }

        public  int getPreviousTabIndex()
        {
            return super.getPreviousTabIndex();
        }

        public  void setSwitcherListener(FragmentTabSwitcherListener fragmenttabswitcherlistener)
        {
            super.setSwitcherListener(fragmenttabswitcherlistener);
        }

        public  String getCurrentTabId()
        {
            return super.getCurrentTabId();
        }

        public  int getTabCount()
        {
            return super.getTabCount();
        }

        public  int getCurrentTabIndex()
        {
            return super.getCurrentTabIndex();
        }

        public  FragmentActivity getFragmentActivity()
        {
            return super.getFragmentActivity();
        }

        public  String getPreviousTabId()
        {
            return super.getPreviousTabId();
        }

        public FragmentTabSwitcherWithoutZorder(FragmentActivity act, int containerId, FragmentTabSwitcherFeed feed)
        {
            super(act, containerId, feed);
        }
    }


    private FragmentUtils()
    {
    }

    public static void printFragmentStates(Fragment f)
    {
        if(f == null)
        {
            return;
        } else
        {
            Log.d(TAG, (new StringBuilder(String.valueOf(f.getClass().getSimpleName()))).append(".isAdded() : ").append(f.isAdded()).toString());
            Log.d(TAG, (new StringBuilder(String.valueOf(f.getClass().getSimpleName()))).append(".isDetached() : ").append(f.isDetached()).toString());
            Log.d(TAG, (new StringBuilder(String.valueOf(f.getClass().getSimpleName()))).append(".isHidden() : ").append(f.isHidden()).toString());
            Log.d(TAG, (new StringBuilder(String.valueOf(f.getClass().getSimpleName()))).append(".isInLayout() : ").append(f.isInLayout()).toString());
            Log.d(TAG, (new StringBuilder(String.valueOf(f.getClass().getSimpleName()))).append(".isRemoving() : ").append(f.isRemoving()).toString());
            Log.d(TAG, (new StringBuilder(String.valueOf(f.getClass().getSimpleName()))).append(".isResumed() : ").append(f.isResumed()).toString());
            Log.d(TAG, (new StringBuilder(String.valueOf(f.getClass().getSimpleName()))).append(".isVisible() : ").append(f.isVisible()).toString());
            return;
        }
    }

    public static void add(FragmentActivity act, Fragment fragment, int containerId, String tag)
    {
        FragmentManager manager = act.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(containerId, fragment, tag);
        ft.commit();
    }

    public static void add(FragmentActivity act, Fragment fragment, int containerId, String tag, String backStackStateName)
    {
        FragmentManager manager = act.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(containerId, fragment, tag);
        ft.addToBackStack(backStackStateName);
        ft.commit();
    }

    public static void remove(FragmentActivity act, Fragment fragment)
    {
        FragmentManager manager = act.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    public static void remove(FragmentActivity act, Fragment fragment, String backStackStateName)
    {
        FragmentManager manager = act.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.remove(fragment);
        ft.addToBackStack(backStackStateName);
        ft.commit();
    }

    public static void replace(FragmentActivity act, Fragment fragment, int containerId, String tag)
    {
        FragmentManager manager = act.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(containerId, fragment, tag);
        ft.commit();
    }

    public static void replace(FragmentActivity act, Fragment fragment, int containerId, String tag, String backStackStateName)
    {
        FragmentManager manager = act.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(containerId, fragment, tag);
        ft.addToBackStack(backStackStateName);
        ft.commit();
    }

    public static void attach(FragmentActivity act, Fragment fragment)
    {
        FragmentManager manager = act.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.attach(fragment);
        ft.commit();
    }

    public static void attach(FragmentActivity act, Fragment fragment, String backStackStateName)
    {
        FragmentManager manager = act.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.attach(fragment);
        ft.addToBackStack(backStackStateName);
        ft.commit();
    }

    public static void detach(FragmentActivity act, Fragment fragment)
    {
        FragmentManager manager = act.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.detach(fragment);
        ft.commit();
    }

    public static void detach(FragmentActivity act, Fragment fragment, String backStackStateName)
    {
        FragmentManager manager = act.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.detach(fragment);
        ft.addToBackStack(backStackStateName);
        ft.commit();
    }

    public static void show(FragmentActivity act, Fragment fragment)
    {
        FragmentManager manager = act.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.show(fragment);
        ft.commit();
    }

    public static void hide(FragmentActivity act, Fragment fragment)
    {
        FragmentManager manager = act.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.hide(fragment);
        ft.commit();
    }

    public static void setContentViewFragment(FragmentActivity act, int containerId, Fragment fragment, String tag)
    {
        act.getSupportFragmentManager().beginTransaction().add(containerId, fragment, tag).commit();
    }
    
    /**
     * 把UI底部需要的Tab转换成一个LinkedHashSet，然后方便
     * 点击返回，后退等操作
     * @param tags
     * @return
     */
    public static LinkedHashSet makeRootFragmentTags(String... tags)
    {
        if(tags == null || tags.length == 0)
            return null;
        LinkedHashSet tagSet = new LinkedHashSet(tags.length);
        int size = tags.length;
        for(int i = 0; i < size; i++){
            tagSet.add(tags[i]);
        }

//        LogUtils2.i("tags[i]="+tags[0]+tags[1]+tags[2]);
        return tagSet;
    }

}


/*
	DECOMPILATION REPORT

	Decompiled from: E:\WorkSoftwareTool\NowEclipse\workspace2\nmplayer\trunk\nmplayer\libs\supportlib.jar
	Total time: 51 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/