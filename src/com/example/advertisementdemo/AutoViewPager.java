package com.example.advertisementdemo;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("ClickableViewAccessibility")
public class AutoViewPager extends ViewPager {

	public static final String TAG = "AutoViewPager";

	/** 页面切换时间差 */
	public static final int DELAY_TIME = 3000;

	public static final int SCROLL_WHAT = 0;

	/** 当前页面 */
	private int currentPage = 0;

	private Handler handler;

	public AutoViewPager(Context context) {
		super(context);
		init();
	}

	public AutoViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		handler = new MyHandler(this);
		setOnTouchListener(new MyOnTouchListner());
		setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 开始自动轮播
	 */
	public void startAutoScrolled() {
		handler.sendEmptyMessageDelayed(SCROLL_WHAT, DELAY_TIME);
	}

	/**
	 * 停止自动轮播
	 */
	public void stopAutoScrolled() {
		handler.removeMessages(SCROLL_WHAT);
	}

	private static class MyHandler extends Handler {

		private final WeakReference<AutoViewPager> autoViewPager;

		public MyHandler(AutoViewPager autoViewPager) {
			this.autoViewPager = new WeakReference<AutoViewPager>(autoViewPager);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			AutoViewPager viewpager = autoViewPager.get();
			if (viewpager.getAdapter().getCount() == 0)
				return;
			int toPage = (++viewpager.currentPage) % (viewpager.getAdapter().getCount());
			viewpager.setCurrentItem(toPage);
			viewpager.handler.removeMessages(SCROLL_WHAT);
			viewpager.handler.sendEmptyMessageDelayed(SCROLL_WHAT, DELAY_TIME);
		}
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {

		private boolean isScrolled = false;

		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case 0:
				if (AutoViewPager.this.getCurrentItem() == AutoViewPager.this.getAdapter()
						.getCount() - 1 && !isScrolled)
					AutoViewPager.this.setCurrentItem(0);
				else if (AutoViewPager.this.getCurrentItem() == 0 && !isScrolled) {
					AutoViewPager.this
							.setCurrentItem(AutoViewPager.this.getAdapter().getCount() - 1);
				}
				break;
			case 1:
				isScrolled = false;
				break;
			case 2:
				isScrolled = true;
				break;
			}
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int arg0) {
		}
	}

	private class MyOnTouchListner implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				stopAutoScrolled();
				break;
			case MotionEvent.ACTION_UP:
				startAutoScrolled();
				break;
			}
			return false;
		}
	}

}
