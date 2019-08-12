package com.hynet.heebit.components.widget.tablayout.listener.implement;import com.hynet.heebit.components.widget.tablayout.TabLayout;import com.hynet.heebit.components.widget.tablayout.TabStrip;import androidx.viewpager.widget.ViewPager;public class InternalViewPagerListener implements ViewPager.OnPageChangeListener {    private int scrollState;    private TabStrip tabStrip;    private TabLayout tabLayout;    private ViewPager.OnPageChangeListener onPageChangeListener;    public InternalViewPagerListener(TabStrip tabStrip, TabLayout tabLayout, ViewPager.OnPageChangeListener onPageChangeListener) {        this.tabStrip = tabStrip;        this.tabLayout = tabLayout;        this.onPageChangeListener = onPageChangeListener;    }    @Override    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {        int tabStripChildCount = tabStrip.getChildCount();        if ((position < 0) || (position >= tabStripChildCount)) {            return;        }        tabStrip.onViewPagerPageChanged(position, positionOffset);        tabLayout.scrollToTab(position, positionOffset);        if (onPageChangeListener != null) {            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);        }    }    @Override    public void onPageScrollStateChanged(int state) {        this.scrollState = state;        if (onPageChangeListener != null) {            onPageChangeListener.onPageScrollStateChanged(state);        }    }    @Override    public void onPageSelected(int position) {        if (scrollState == ViewPager.SCROLL_STATE_IDLE) {            tabStrip.onViewPagerPageChanged(position, 0f);            tabLayout.scrollToTab(position, 0);        }        for (int i = 0, size = tabStrip.getChildCount(); i < size; i++) {            tabStrip.getChildAt(i).setSelected(position == i);        }        if (onPageChangeListener != null) {            onPageChangeListener.onPageSelected(position);        }//        tabLayout.requestLayout();    }}