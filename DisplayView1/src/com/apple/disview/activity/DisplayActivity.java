package com.apple.disview.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.apple.disview.R;

/**
 * 柱状形显示demo
 * 
 * @author Administrator
 * 
 */
public class DisplayActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 先编写一个布局文件
		this.setContentView(R.layout.layout_home);
		TextView txt_chart = (TextView) this.findViewById(R.id.txt_chart);
		TextView txt_grid = (TextView) this.findViewById(R.id.txt_grid);
		TextView txt_wheel = (TextView) this.findViewById(R.id.txt_wheel);
		txt_chart.setOnClickListener(this);
		txt_wheel.setOnClickListener(this);
		txt_grid.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.txt_chart:
			intent = new Intent(this, ChartActivity.class);
			this.startActivity(intent);
			break;
		case R.id.txt_wheel:
			intent = new Intent(this, RemFast.class);
			this.startActivity(intent);
			break;
		case R.id.txt_grid:
			intent = new Intent(this, GridActivity.class);
			this.startActivity(intent);
			break;
		}
	}

}