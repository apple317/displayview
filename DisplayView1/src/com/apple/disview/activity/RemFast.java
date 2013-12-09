package com.apple.disview.activity;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.apple.asset.DateHelper;
import com.apple.disview.R;
import com.apple.disview.WheelView;
import com.apple.listener.ArrayWheelAdapter;
import com.apple.listener.OnWheelChangedListener;
import com.apple.listener.OnWheelScrollListener;

public class RemFast extends Activity {
	private LayoutInflater mInflater;
	private Context mcontext;
	private boolean wheelScrolled = false;
	// private DomobAdView mAdview320x50;
	private WheelView wheel_hours;
	private WheelView wheel_minute;
	private String remenderTime;
	private WheelView wheel_month;
	private WheelView wheel_day;
	private int beforeMonth = -1;
	private LinearLayout interGELinearLayout;
	public static final String[] HOUR_STRING = { "00", "01", "02", "03", "04",
			"05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
			"16", "17", "18", "19", "20", "21", "22", "23" };
	public static final String[] MINTUE_STRING = { "00", "01", "02", "03",
			"04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
			"15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
			"26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36",
			"37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47",
			"48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58",
			"59" };
	public static final String[] DAY_STRING = { "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
			"17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
			"28", "29", "30", "31" };
	public static final String[] MONTH_STRING = { "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "10", "11", "12" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_wheel);
		mcontext = this;
		mInflater = LayoutInflater.from(mcontext);
		createHead();
	}
	/**
	 * layout_remfast布局文件
	 * 
	 * 
	 * <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/lay_content"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:orientation="vertical" >

    <LinearLayout
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="10dp" >
		<!-- 布局添加一个滑轮视图-->
        <com.apple.disview.WheelView
            android:id="@+id/fast_month"
            android:layout_width="0dip"
            android:layout_height="100dp"
            android:layout_gravity="center_vertical"
            android:layout_weight="1.0" />

        <com.apple.disview.WheelView
            android:id="@+id/fast_day"
            android:layout_width="0dip"
            android:layout_height="100dp"
            android:layout_gravity="center_vertical"
            android:layout_weight="1.0" />

        <com.apple.disview.WheelView
            android:id="@+id/fast_hours"
            android:layout_width="0dip"
            android:layout_height="100dp"
            android:layout_gravity="center_vertical"
            android:layout_weight="1.0" />

        <com.apple.disview.WheelView
            android:id="@+id/fast_mintue"
            android:layout_width="0dip"
            android:layout_height="100dp"
            android:layout_gravity="center_vertical"
            android:layout_weight="1.0" />
    </LinearLayout>

</LinearLayout>
	 * 
	 * 
	 * 
	 */

	private void createHead() {
		LinearLayout laymottom = (LinearLayout) findViewById(R.id.four_mottom);
		//创建一个滑轮子视图类
		View mottom = mInflater.inflate(R.layout.layout_remfast, null);
		//初始化一个滑轮视图对象
		wheel_month = (WheelView) mottom.findViewById(R.id.fast_month);
		//设置滑轮标记
		wheel_month.setTag("month");
		//设置滑轮数据集
		wheel_month.setAdapter(new ArrayWheelAdapter<String>(MONTH_STRING));
		//设置滑轮当前所在值
		wheel_month.setCurrentItem(wheel_month.getCurrentVal("02"));
		//设置滑轮是否可以循环滑动
		wheel_month.setCyclic(true);
		//设置滑轮标题
		wheel_month.setLabel("月");
		//添加滑轮变化监听事件
		wheel_month.addChangingListener(wheelChangeListener);
		//添加滑轮滚动变化监听事件
		wheel_month.addScrollingListener(wheelScrolledListener, null);
		wheel_day = (WheelView) mottom.findViewById(R.id.fast_day);
		wheel_day.setAdapter(new ArrayWheelAdapter<String>(DAY_STRING));
		wheel_day.setCurrentItem(3);
		wheel_day.setLabel("天");
		wheel_day.setTag("day");
		wheel_day.setCyclic(true);
		wheel_day.addChangingListener(wheelChangeListener);
		wheel_day.addScrollingListener(wheelScrolledListener, null);
		wheel_hours = (WheelView) mottom.findViewById(R.id.fast_hours);
		wheel_hours.setAdapter(new ArrayWheelAdapter<String>(HOUR_STRING));
		wheel_hours.setCyclic(true);
		wheel_hours.setTag("hours");
		wheel_minute = (WheelView) mottom.findViewById(R.id.fast_mintue);
		wheel_minute.setAdapter(new ArrayWheelAdapter<String>(MINTUE_STRING));
		wheel_minute.setCyclic(true);
		wheel_minute.setTag("minute");
		wheel_minute.setLabel("分钟");
		wheel_hours.setLabel("小时");
		wheel_hours.setCurrentItem(1);
		wheel_minute.setCurrentItem(2);
		wheel_minute.addScrollingListener(wheelScrolledListener, null);
		wheel_minute.addChangingListener(wheelChangeListener);
		wheel_minute.addScrollingListener(wheelScrolledListener, null);
		wheel_hours.addChangingListener(wheelChangeListener);
		wheel_hours.addScrollingListener(wheelScrolledListener, null);
		wheel_hours.setleftCheck(true);
		wheel_minute.setleftCheck(true);
		wheel_day.setleftCheck(true);
		laymottom.addView(mottom);
	}

	OnWheelScrollListener wheelScrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			wheelScrolled = true;
		}
		@Override
		public void onScrollingFinished(WheelView wheel) {
			// TODO Auto-generated method stub
			String tag = wheel.getTag().toString();
			wheelScrolled = false;
		}
	};

	// Wheel changed listener
	private OnWheelChangedListener wheelChangeListener = new OnWheelChangedListener() {
		@Override
		public void onLayChanged(WheelView wheel, int oldValue, int newValue,
				LinearLayout layout) {
			// TODO Auto-generated method stub

		}
	};

	
}