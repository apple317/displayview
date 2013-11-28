package com.apple.disview;

import java.util.LinkedHashMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.hupu.run.untils.DateHelper;
import com.pyj.common.DeviceInfo;

public class ChartView extends View {

	private LinkedHashMap<String, String> map;

	private int flag;
	private int margin;

	private Paint paint;

	//每个柱形宽度
	private int zWith=0;
	private int zPadding=0;
	private int startX=0;
	private int totalHeight=0;
	private int lineHeight=0;
	public ChartView(Context context, int flag,
			LinkedHashMap<String, String> map,int wigth) {
		super(context);
		this.flag = flag;
		margin = 0;
		paint = new Paint();
		paint.setAntiAlias(true);
		this.map = map;
		int swigth=(wigth-DeviceInfo.DipToPixels(this.getContext(), 65))/(DateHelper.getDayofMonth()*3);
		zWith=swigth*2;
		zPadding=swigth;
		startX=DeviceInfo.DipToPixels(this.getContext(), 65);
		totalHeight=DeviceInfo.DipToPixels(this.getContext(),260);
		lineHeight=DeviceInfo.DipToPixels(this.getContext(),240);
	}

	public void drawAxis(Canvas canvas) {
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(2);
		paint.setTextSize(DeviceInfo.DipToPixels(this.getContext(), 12));
		canvas.drawLine(startX, lineHeight,
				DateHelper.getDayofMonth() * zWith + DateHelper.getDayofMonth() * zPadding
						+ startX + 10, lineHeight, paint);
		canvas.drawLine(startX, 0, startX, lineHeight, paint);
		int x = 0;
		int y = 0;
		for (int i = 1; i < DateHelper.getDayofMonth(); i++) {
			x = startX + i * zPadding + (i) * zWith - 2;
			if (i == 1 || i % 5 == 0)
				canvas.drawText(i < 10 ? "0" + i : i + "", x, totalHeight, paint);
		}
		for (int i = 0; i < 4; i++) {
			y = lineHeight - i * DeviceInfo.DipToPixels(this.getContext(),60);
			canvas.drawText(i == 0 ? i + "(公里)" : 10 * i + "-",
					i == 0 ? DeviceInfo.DipToPixels(this.getContext(),24) : DeviceInfo.DipToPixels(this.getContext(),50), y, paint);
		}
	}


	public void drawChart(Canvas canvas) {
		paint.setColor(Color.WHITE);
		int temp = 65;
		if (map != null && map.size() > 0) {
			for (int i = 1; i < DateHelper.getDayofMonth() + 1; i++) {
				int total = (int) (map.get((i<10?"0"+i:i)) == null
						|| map.get((i<10?"0"+i:i)).equals("") ? 0 : Double.valueOf(map
						.get((i<10?"0"+i:i)))*6);
				canvas.drawRect(i * zPadding + (i - 1) * zWith + startX, lineHeight -total, i * zPadding
						+ i * zWith + startX, lineHeight, paint);
			}
		}
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		final int heightSize = MeasureSpec.getSize(totalHeight);
		setMeasuredDimension(widthSize, heightSize);
	}

	@Override
	public void onDraw(Canvas canvas) {
		drawAxis(canvas);
		drawChart(canvas);
	}

}
