package com.apple.disview.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.apple.asset.ChartEntity;
import com.apple.disview.R;
import com.apple.disview.ChartView;

/**
 * 柱状形显示demo
 * 
 * @author Administrator
 * 
 */
public class ChartActivity extends Activity {

	private LinearLayout layout_chart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 先编写一个布局文件
		this.setContentView(R.layout.layout_chart);
		//创建一个线性容器，之后就是向着里面嵌套视图
		layout_chart = (LinearLayout) this.findViewById(R.id.lay_chart);
		LinearLayout.LayoutParams layParam = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layParam.gravity = Gravity.CENTER_HORIZONTAL;
		//创建柱状性参数对象
		ChartEntity entity = new ChartEntity();
		entity.scale = 6;
		entity.row_height = 60;
		entity.row_weight = 4;
		entity.padding_weight = 8;
		//创建一个纵向数据容器
		List<String> hList = new ArrayList<String>();
		//创建一个横向数据容器，注解数据格式如:key,是否显示在横坐标
		List<String> wList = new ArrayList<String>();
		//创建一个存储高度值容器
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		hList.add("0公里");
		hList.add("10-");
		hList.add("20-");
		hList.add("30-");
		entity.hList = hList;
		for (int i = 0; i < 30; i++) {
			String curStr;
			if ((i + 1) % 5 == 0) {
				curStr = (i + 1) + ",true";
			} else {
				curStr = (i + 1) + ",false";
			}
			wList.add(curStr);
			map.put(curStr, i * 4);
		}
		entity.wList = wList;
		entity.map = map;
		ChartView chartView = new ChartView(this.getApplicationContext(),
				entity);
		layout_chart.addView(chartView, layParam);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
}