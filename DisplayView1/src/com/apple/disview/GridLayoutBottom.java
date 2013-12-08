package com.apple.disview;



import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;

public class GridLayoutBottom extends ViewGroup {
	
	static private final String TAG = "ButtonGridLayout";
	static private final int COLUMNS_LAND = 4;
	static private final int COLUMNS_PORT = 3;
	private int COLUMNS, ROWS, NUM_CHILDREN;
	private View[] mButtons;
	
	// This what the fields represent (height is similar):
	// PL: mPaddingLeft
	// BW: mButtonWidth
	// PR: mPaddingRight
	//
	// mWidthInc
	// <-------------------->
	// PL BW PR
	// <----><--------><---->
	// --------
	// | |
	// | button |
	// | |
	// --------
	//
	// We assume mPaddingLeft == mPaddingRight == 1/2 padding between
	// buttons.
	//
	// mWidth == COLUMNS x mWidthInc

	// Width and height of a button
	private int mButtonWidth;
	private int mButtonHeight;

	// Width and height of a button + padding.
	private int mWidthInc;
	private int mHeightInc;

	// Height of the dialpad. Used to align it at the bottom of the
	// view.
	private int mWidth;
	private int mHeight;

	/**
	 * Cache the buttons in a member array for faster access. Compute the
	 * measurements for the width/height of buttons. The inflate sequence is
	 * called right after the constructor and before the measure/layout phase.
	 */
	public GridLayoutBottom(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public GridLayoutBottom(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public GridLayoutBottom(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
//		COLUMNS = mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? COLUMNS_PORT
//				: COLUMNS_LAND;
		COLUMNS=3;
		if (mButtons == null) {
			NUM_CHILDREN = getChildCount();
			mButtons = new View[NUM_CHILDREN];
			// 得到行数
			ROWS = NUM_CHILDREN / COLUMNS
					+ (NUM_CHILDREN % COLUMNS > 0 ? 1 : 0);
		}
		final View[] buttons = mButtons;
		for (int i = 0; i < NUM_CHILDREN; i++) {
			buttons[i] = getChildAt(i);
			// Measure the button to get initialized.
			buttons[i]
					.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		}

		// Cache the measurements.
		final View child = buttons[0];
		mButtonWidth = child.getMeasuredWidth();
		mButtonHeight = child.getMeasuredHeight();
		mWidthInc = mButtonWidth + 10 + 10;
		mHeightInc = mButtonHeight + 10 + 10;
		mWidth = COLUMNS * mWidthInc;
		mHeight = ROWS * mHeightInc;

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		final View[] buttons = mButtons;
		final View parent = (View) getParent();// fill parent mode
		final int height = parent.getHeight();
		final int width = parent.getWidth();
		final int paddingLeft = 10;
		int buttonWidth;
		int buttonHeight;
		int widthInc;
		int heightInc;
		//mPaddingTop
		if (ROWS * mButtonHeight + (ROWS - 1) * 10< height) {
			//mPaddingLeft
			buttonWidth = (width - 10 * (COLUMNS - 1)) / COLUMNS;
			//mPaddingTop
			buttonHeight = (height - 10 * (ROWS - 1)) / ROWS;
			widthInc = width / COLUMNS;
			heightInc = height / ROWS;
			mWidth = width;
			mHeight = height;
		} else {
			buttonWidth = mButtonWidth;
			buttonHeight = mButtonHeight;
			widthInc = mWidthInc;
			heightInc = mHeightInc;
		}

		int i = 0;
		// The last row is bottom aligned.
		//int y = (bottom - top) - mHeight + mPaddingTop;
		int y = (bottom - top) - mHeight + 10;
		for (int row = 0; row < ROWS; row++) {
			int x = paddingLeft;
			for (int col = 0; col < COLUMNS && i < NUM_CHILDREN; col++) {
				View button = buttons[i];
				button.layout(x, y, x + buttonWidth, y + buttonHeight);
				/*
				 * if(button instanceof Button){ Button btn = (Button)button;
				 * int size = buttonHeight/6; btn.setTextSize(size > 32 ? 32 :
				 * size); btn.setGravity(Gravity.CENTER); }
				 */
				x += widthInc;
				i++;
			}
			y += heightInc;
		}
	}

	/**
	 * This method is called twice in practice. The first time both with and
	 * height are constraint by AT_MOST. The second time, the width is still
	 * AT_MOST and the height is EXACTLY. Either way the full width/height
	 * should be in mWidth and mHeight and we use 'resolveSize' to do the right
	 * thing.
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = resolveSize(mWidth, widthMeasureSpec);
		final int height = resolveSize(mHeight, heightMeasureSpec);
		setMeasuredDimension(width, height);
	}
}
