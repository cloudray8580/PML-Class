package com.example.advertisementdemo;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class AutoPagerAdapter extends PagerAdapter {

	private List<View> listview;

	public AutoPagerAdapter(List<View> l) {
		listview = l;
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(listview.get(position));
		return listview.get(position);
	}

	@Override
	public int getCount() {
		return listview.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(listview.get(position));
	}

}
