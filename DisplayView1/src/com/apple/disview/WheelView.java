package com.apple.disview;

/*
 * 
 * 
 *  
 *  Copyright 2012 hushaoping
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.apple.listener.OnWheelChangedListener;
import com.apple.listener.OnWheelScrollListener;
import com.apple.listener.WheelAdapter;
import com.apple.listener.onWheelClickListener;

/**
 * Numeric wheel view.
 * 
 * @author Yuri Kanivets
 */
public class WheelView extends View {
	/** Scrolling duration */
	private static final int SCROLLING_DURATION = 500;

	/** Minimum delta for scrolling */
	private static final int MIN_DELTA_FOR_SCROLLING = 1;

	/** Current value & label text color */
	private static final int VALUE_TEXT_COLOR = 0xF0000000;

	/** Items text color */
	private static final int ITEMS_TEXT_COLOR = 0xFF000000;

	/** Top and bottom shadows colors */
	private static final int[] SHADOWS_COLORS = new int[] { 0xFFfffbf7,
			0xFFffebda, 0xFFffebd9 };
	private static final int[] HSHADOWS_COLORS = new int[] { 0xFFe6e4e4,
			0xFF7d7c7c, 0x5e5d5d };
	private static final int[] BSHADOWS_COLORS = new int[] { 0x5e5d5d,
			0xFF7d7c7c, 0xFFe6e4e4 };
	/** Additional items height (is added to standard text item height) */
	/** Text size */
	private static float TEXT_SIZE = 24;// 24

	/** Default count of visible items */
	private static final int DEF_VISIBLE_ITEMS = 3;

	// Wheel Values
	private WheelAdapter adapter = null;
	private int currentItem = 0;

	// Widths
	private int itemsWidth = 0;
	private int labelWidth = 0;

	// Count of visible items
	private int visibleItems = DEF_VISIBLE_ITEMS;

	// Item height
	private int itemHeight = 0;

	// Text paints
	private TextPaint itemsPaint;
	private TextPaint valuePaint;

	// Layouts
	// private StaticLayout itemsLayout;
	private StaticLayout valueLayout;
	private StaticLayout srightLayout;
	private StaticLayout sleftLayout;
	// Label & background
	private String label;
	// Scrolling
	private boolean isScrollingPerformed;
	private int scrollingOffset;

	// Scrolling animation
	private GestureDetector gestureDetector;
	private Scroller scroller;
	private int lastScrollY;

	// Cyclic
	boolean isCyclic = true;

	// Listeners
	private List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();
	private List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();

	private Context mcontext;
	private onWheelClickListener wClick;

	private LinearLayout mlayout;
	private LinearLayout leftLayout;
	// type view
	private WheelView wview;
	private boolean single = false;
	private int mWidth;
	private int mHeight;

	private boolean leftCheck = false;

	/**
	 * Constructor
	 */
	public WheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mcontext = context;
		initData(context);
		wview = this;
	}

	/**
	 * Constructor
	 */
	public WheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mcontext = context;
		initData(context);
		wview = this;
	}

	/**
	 * Constructor
	 */
	public WheelView(Context context) {
		super(context);
		mcontext = context;
		initData(context);
		wview = this;
	}

	/**
	 * Initializes class data
	 * 
	 * @param context
	 *            the context
	 */
	private void initData(Context context) {
		gestureDetector = new GestureDetector(context, gestureListener);
		gestureDetector.setIsLongpressEnabled(false);
		scroller = new Scroller(context);

	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		mHeight = getHeight();
	}

	/**
	 * Gets wheel adapter
	 * 
	 * @return the adapter
	 */
	public WheelAdapter getAdapter() {
		return adapter;
	}

	/**
	 * Sets wheel adapter
	 * 
	 * @param adapter
	 *            the new wheel adapter
	 */
	public void setAdapter(WheelAdapter adapter) {
		this.adapter = adapter;
		invalidateLayouts();
		invalidate();
	}

	/**
	 * Set the the specified scrolling interpolator
	 * 
	 * @param interpolator
	 *            the interpolator
	 */
	public void setInterpolator(Interpolator interpolator) {
		scroller.forceFinished(true);
		scroller = new Scroller(getContext(), interpolator);
	}

	/**
	 * Gets count of visible items
	 * 
	 * @return the count of visible items
	 */
	public int getVisibleItems() {
		return visibleItems;
	}

	/**
	 * Sets count of visible items
	 * 
	 * @param count
	 *            the new count
	 */
	public void setVisibleItems(int count) {
		visibleItems = count;
		invalidate();
	}

	/**
	 * Gets label
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets label
	 * 
	 * @param newLabel
	 *            the label to set
	 */
	public void setLabel(String newLabel) {
		label = newLabel;
	}

	/**
	 * Adds wheel changing listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addChangingListener(OnWheelChangedListener listener) {
		changingListeners.add(listener);
	}

	/**
	 * Removes wheel changing listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeChangingListener(OnWheelChangedListener listener) {
		changingListeners.remove(listener);
	}

	/**
	 * Notifies changing listeners
	 * 
	 * @param oldValue
	 *            the old wheel value
	 * @param newValue
	 *            the new wheel value
	 */
	protected void notifyChangingListeners(int oldValue, int newValue) {
		for (OnWheelChangedListener listener : changingListeners) {
			listener.onLayChanged(this, oldValue, newValue, mlayout);
		}
	}

	/**
	 * Adds wheel scrolling listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addScrollingListener(OnWheelScrollListener listener,
			GestureScrollView gs) {
		if (gs != null)
			gs.setGesture(gestureDetector);
		scrollingListeners.add(listener);
	}

	/**
	 * Removes wheel scrolling listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.remove(listener);
	}

	/**
	 * Notifies listeners about starting scrolling
	 */
	protected void notifyScrollingListenersAboutStart() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingStarted(this);
		}
	}

	/**
	 * Notifies listeners about ending scrolling
	 */
	protected void notifyScrollingListenersAboutEnd() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingFinished(this);
		}
	}

	/**
	 * Gets current value
	 * 
	 * @return the current value
	 */
	public int getCurrentItem() {
		return currentItem;
	}

	/**
	 * Sets the current item. Does nothing when index is wrong.
	 * 
	 * @param index
	 *            the item index
	 * @param animated
	 *            the animation flag
	 */
	public void setCurrentItem(int index, boolean animated) {
		if (adapter == null || adapter.getItemsCount() == 0) {
			return; // throw?
		}
		if (index < 0 || index >= adapter.getItemsCount()) {
			if (isCyclic) {
				while (index < 0) {
					index += adapter.getItemsCount();
				}
				index %= adapter.getItemsCount();
			} else {
				return; // throw?
			}
		}
		if (index != currentItem) {
			if (animated) {
				scroll(index - currentItem, SCROLLING_DURATION);
			} else {
				invalidateLayouts();
				int old = currentItem;
				currentItem = index;
				notifyChangingListeners(old, currentItem);
				invalidate();
			}
		}
	}

	/**
	 * Sets the current item w/o animation. Does nothing when index is wrong.
	 * 
	 * @param index
	 *            the item index
	 */
	public void setCurrentItem(int index) {
		setCurrentItem(index, false);
	}

	/**
	 * Tests if wheel is cyclic. That means before the 1st item there is shown
	 * the last one
	 * 
	 * @return true if wheel is cyclic
	 */
	public boolean isCyclic() {
		return isCyclic;
	}

	/**
	 * Set wheel cyclic flag
	 * 
	 * @param isCyclic
	 *            the flag to set
	 */
	public void setCyclic(boolean isCyclic) {
		this.isCyclic = isCyclic;
		invalidate();
		invalidateLayouts();
	}

	/**
	 * Invalidates layouts
	 */
	private void invalidateLayouts() {
		valueLayout = null;
		valueLayout = null;
		scrollingOffset = 0;
	}

	/**
	 * Initializes resources
	 */
	private void initResourcesIfNecessary() {
		if (itemsPaint == null) {
			itemsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
					| Paint.FAKE_BOLD_TEXT_FLAG);
			itemsPaint.setTextSize(TEXT_SIZE);
			itemsPaint.setTextAlign(Align.CENTER);
		}

		if (valuePaint == null) {
			valuePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
					| Paint.FAKE_BOLD_TEXT_FLAG | Paint.DITHER_FLAG);
			valuePaint.setTextSize(TEXT_SIZE);
			valuePaint.setShadowLayer(0.1f, 0, 0.1f, 0xFFC0C0C0);
			valuePaint.setTextAlign(Align.CENTER);
		}
		// setBackgroundResource(R.color.font_white);
	}

	/**
	 * Returns text item by index
	 * 
	 * @param index
	 *            the item index
	 * @return the item or null
	 */
	private String getTextItem(int index) {
		if (adapter == null || adapter.getItemsCount() == 0) {
			return null;
		}
		int count = adapter.getItemsCount();
		if ((index < 0 || index >= count) && !isCyclic) {
			return null;
		} else {
			while (index < 0) {
				index = count + index;
			}
		}
		index %= count;
		return adapter.getItem(index);
	}

	/**
	 * Builds text depending on current value
	 * 
	 * @param useCurrentValue
	 * @return the text
	 */
	private String buildText(boolean useCurrentValue) {
		StringBuilder itemsText = new StringBuilder();
		int addItems = visibleItems / 2 + 1;
		for (int i = currentItem - addItems; i <= currentItem + addItems; i++) {
			String text = getTextItem(i);
			if (text != null) {
				itemsText.append(text);
			}
			if (i < currentItem + addItems) {
				itemsText.append("\n");
			}
		}
		return itemsText.toString();
	}

	/**
	 * Returns height of wheel item
	 * 
	 * @return the item height
	 */
	private int getItemHeight() {
		if (itemHeight != 0) {
			return itemHeight;
		} else if (valueLayout != null && valueLayout.getLineCount() > 2) {
			itemHeight = valueLayout.getLineTop(2) - valueLayout.getLineTop(1);
			return itemHeight;
		}
		return getHeight() / visibleItems;
	}

	public int getCurrentVal(String itemVal) {
		int current = 0;
		for (int i = 0; i < adapter.getItemsCount(); i++) {
			if (getTextItem(i).equals(itemVal))
				current = i;
		}
		return current;
	}

	/**
	 * Creates layouts
	 * 
	 * @param widthItems
	 *            width of items layout
	 * @param widthLabel
	 *            width of label layout
	 */
	private void createLayouts() {
		String text = getAdapter() != null ? getAdapter().getItem(currentItem)
				: null;
		int itemHeight = (int) (mHeight / DEF_VISIBLE_ITEMS / 2 - TEXT_SIZE / 2);
		if (single) {
			sleftLayout = new StaticLayout("", valuePaint, 0,
					Layout.Alignment.ALIGN_CENTER, 1, 0.0F, false);
			valueLayout = new StaticLayout(buildText(isScrollingPerformed),
					valuePaint, this.getWidth() * 2 / 3,
					Layout.Alignment.ALIGN_NORMAL, 1, itemHeight * 2 - 4, false);
			srightLayout = new StaticLayout(label == null ? "" : label,
					valuePaint, this.getWidth() / 3,
					Layout.Alignment.ALIGN_CENTER, 1, itemHeight * 2 - 4, false);
		} else {
			sleftLayout = new StaticLayout("", valuePaint, this.getWidth() / 3,
					Layout.Alignment.ALIGN_CENTER, 1, 0.0F, false);
			valueLayout = new StaticLayout(buildText(isScrollingPerformed),
					valuePaint, this.getWidth() / 3,
					Layout.Alignment.ALIGN_NORMAL, 1, itemHeight * 2 - 4, false);
			srightLayout = new StaticLayout(label == null ? "" : label,
					valuePaint, this.getWidth() / 3,
					Layout.Alignment.ALIGN_CENTER, 1, itemHeight * 2 - 4, false);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		initResourcesIfNecessary();
		createLayouts();
		drawValue(canvas);
		canvas.restore();
		Rect r = new Rect();
		Shader mShader = new LinearGradient(0, 0, 100, 100, SHADOWS_COLORS,
				new float[] { 0, 0.5f, 1.0f }, Shader.TileMode.CLAMP);
		Shader hCheckShader = new LinearGradient(0, 0, 100, 100,
				HSHADOWS_COLORS, new float[] { 0, 0.5f, 1.0f },
				Shader.TileMode.CLAMP);
		Shader bCheckShader = new LinearGradient(0, 0, 100, 100,
				BSHADOWS_COLORS, new float[] { 0, 0.5f, 1.0f },
				Shader.TileMode.CLAMP);
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setShader(mShader);
		p.setAlpha(127);
		r.left = 0;
		r.top = mHeight / 3;
		r.right = r.left + this.getWidth();
		r.bottom = mHeight / 3 + r.top;
		canvas.drawRect(r, p);
		if (leftCheck) {
			p.setShader(hCheckShader);
			p.setStyle(Paint.Style.FILL);
			Rect rCheck = new Rect();
			rCheck.left = 0;
			rCheck.top = 0;
			rCheck.right = 1;
			rCheck.bottom = (r.bottom + mHeight / 3) / 2;
			canvas.drawRect(rCheck, p);
			p.setShader(bCheckShader);
			Rect bCheck = new Rect();
			bCheck.left = 0;
			bCheck.top = (r.bottom + mHeight / 3) / 2;
			bCheck.right = 1;
			bCheck.bottom = r.bottom + mHeight / 3;
			canvas.drawRect(bCheck, p);
			// Paint rline = new Paint();
			// rline.setStyle(Paint.Style.STROKE);
			// rline.setTextSkewX((float) 3.0);
			// rline.setColor(0xFFffd5b0);
			// canvas.drawLine(0,0,0,r.bottom+mHeight/3, rline);//
		}
		Paint pline = new Paint();
		pline.setColor(0xFFffd5b0);
		pline.setStyle(Paint.Style.FILL);
		pline.setTextSkewX((float) 3.0);
		canvas.drawLine(0, r.top, r.right, r.top, pline);
		canvas.drawLine(0, r.bottom, r.right, r.bottom, pline);//
		canvas.drawLine(0, r.top, 0, r.bottom, pline);//
		canvas.drawLine(r.right, r.top, r.right, r.bottom, pline);//
	}

	/**
	 * Draws value and label layout
	 * 
	 * @param canvas
	 *            the canvas for drawing
	 */
	private void drawValue(Canvas canvas) {
		canvas.save();
		valuePaint.setColor(VALUE_TEXT_COLOR);
		valuePaint.drawableState = getDrawableState();
		int itemHeight = (int) (mHeight / DEF_VISIBLE_ITEMS / 2 - TEXT_SIZE / 2);
		Rect bounds = new Rect();
		if (single) {
			if (sleftLayout != null) {
				canvas.save();
				canvas.translate(0, bounds.top);
				sleftLayout.draw(canvas);
				canvas.restore();
			}
			// // draw label
			if (srightLayout != null) {
				canvas.save();
				canvas.translate((this.getWidth() / 3) * 2, itemHeight * 3
						+ TEXT_SIZE);
				srightLayout.draw(canvas);
				canvas.restore();
			}
			// draw current value
			if (valueLayout != null) {
				canvas.save();
				int top = valueLayout.getLineTop(1);
				canvas.translate(this.getWidth() / 3
						+ (valueLayout.getWidth() / 11) * 2, -top
						+ scrollingOffset + itemHeight);
				itemsPaint.setColor(ITEMS_TEXT_COLOR);
				itemsPaint.drawableState = getDrawableState();
				valueLayout.draw(canvas);
				canvas.restore();
			}
		} else {
			if (sleftLayout != null) {
				canvas.save();
				canvas.translate(this.getWidth() / 4, bounds.top);
				sleftLayout.draw(canvas);
				canvas.restore();
			}
			// draw label
			if (srightLayout != null) {
				canvas.save();
				canvas.translate((this.getWidth() / 3) * 2, itemHeight * 3
						+ TEXT_SIZE);
				srightLayout.draw(canvas);
				canvas.restore();
			}
			// draw current value
			if (valueLayout != null) {
				canvas.save();
				int top = valueLayout.getLineTop(1);
				canvas.translate(this.getWidth() / 3
						+ (valueLayout.getWidth() / 11) * 5, -top
						+ scrollingOffset + itemHeight);
				itemsPaint.setColor(ITEMS_TEXT_COLOR);
				itemsPaint.drawableState = getDrawableState();
				valueLayout.draw(canvas);
				canvas.restore();
			}
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		WheelAdapter adapter = getAdapter();
		if (adapter == null) {
			return true;
		}
		if (!gestureDetector.onTouchEvent(event)
				&& event.getAction() == MotionEvent.ACTION_UP)
			justify();
		return true;
	}

	/**
	 * Scrolls the wheel
	 * 
	 * @param delta
	 *            the scrolling value
	 */
	private void doScroll(int delta) {
		scrollingOffset += delta;
		int count = scrollingOffset / getItemHeight();
		int pos = currentItem - count;
		if (isCyclic && adapter.getItemsCount() > 0) {
			// fix position by rotating
			while (pos < 0) {
				pos += adapter.getItemsCount();
			}
			pos %= adapter.getItemsCount();
		} else if (isScrollingPerformed) {
			//
			if (pos < 0) {
				count = currentItem;
				pos = 0;
			} else if (pos >= adapter.getItemsCount()) {
				count = currentItem - adapter.getItemsCount() + 1;
				pos = adapter.getItemsCount() - 1;
			}
		} else {
			// fix position
			pos = Math.max(pos, 0);
			pos = Math.min(pos, adapter.getItemsCount() - 1);
		}

		int offset = scrollingOffset;
		if (pos != currentItem) {
			setCurrentItem(pos, false);
		} else {
			invalidate();
		}

		// update offset
		scrollingOffset = offset - count * getItemHeight();
		if (scrollingOffset > getHeight()) {
			scrollingOffset = scrollingOffset % getHeight() + getHeight();
		}
	}

	// gesture listener
	private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
		public boolean onDown(MotionEvent e) {
			if (wview.getTag() != null && wClick != null)
				wClick.onClick(wview.getTag());
			if (isScrollingPerformed) {
				scroller.forceFinished(true);
				clearMessages();
				return true;
			}
			return false;
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			startScrolling();
			doScroll((int) -distanceY);
			return true;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			lastScrollY = currentItem * getItemHeight() + scrollingOffset;
			int maxY = isCyclic ? 0x7FFFFFFF : adapter.getItemsCount()
					* getItemHeight();
			int minY = isCyclic ? -maxY : 0;
			scroller.fling(0, lastScrollY, 0, (int) -velocityY / 2, 0, 0, minY,
					maxY);
			setNextMessage(MESSAGE_SCROLL);
			return true;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return super.onSingleTapUp(e);
		}

	};

	// Messages
	private final int MESSAGE_SCROLL = 0;
	private final int MESSAGE_JUSTIFY = 1;

	/**
	 * Set next message to queue. Clears queue before.
	 * 
	 * @param message
	 *            the message to set
	 */
	private void setNextMessage(int message) {
		clearMessages();
		animationHandler.sendEmptyMessage(message);
	}

	/**
	 * Clears messages from queue
	 */
	private void clearMessages() {
		animationHandler.removeMessages(MESSAGE_SCROLL);
		animationHandler.removeMessages(MESSAGE_JUSTIFY);
	}

	// animation handler
	private Handler animationHandler = new Handler() {
		public void handleMessage(Message msg) {
			scroller.computeScrollOffset();
			int currY = scroller.getCurrY();
			int delta = lastScrollY - currY;
			lastScrollY = currY;
			if (delta != 0) {
				doScroll(delta);
			}

			// scrolling is not finished when it comes to final Y
			// so, finish it manually
			if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
				currY = scroller.getFinalY();
				scroller.forceFinished(true);
			}
			if (!scroller.isFinished()) {
				animationHandler.sendEmptyMessage(msg.what);
			} else if (msg.what == MESSAGE_SCROLL) {
				justify();
			} else {
				finishScrolling();
			}
		}
	};

	/**
	 * Justifies wheel
	 */
	private void justify() {
		if (adapter == null) {
			return;
		}
		lastScrollY = 0;
		int offset = scrollingOffset;
		int itemHeight = getItemHeight();
		boolean needToIncrease = offset > 0 ? currentItem < adapter
				.getItemsCount() : currentItem > 0;
		if ((isCyclic || needToIncrease)
				&& Math.abs((float) offset) > (float) itemHeight / 2) {
			if (offset < 0)
				offset += itemHeight + MIN_DELTA_FOR_SCROLLING;
			else
				offset -= itemHeight + MIN_DELTA_FOR_SCROLLING;
		}
		if (Math.abs(offset) > MIN_DELTA_FOR_SCROLLING) {
			scroller.startScroll(0, 0, 0, offset, SCROLLING_DURATION);
			setNextMessage(MESSAGE_JUSTIFY);
		} else {
			finishScrolling();
		}
	}

	/**
	 * Starts scrolling
	 */
	private void startScrolling() {
		if (!isScrollingPerformed) {
			isScrollingPerformed = true;
			notifyScrollingListenersAboutStart();
		}
	}

	/**
	 * Finishes scrolling
	 */
	void finishScrolling() {
		if (isScrollingPerformed) {
			notifyScrollingListenersAboutEnd();
			isScrollingPerformed = false;
		}
		invalidateLayouts();
		invalidate();
	}

	/**
	 * Scroll the wheel
	 * 
	 * @param itemsToSkip
	 *            items to scroll
	 * @param time
	 *            scrolling duration
	 */
	public void scroll(int itemsToScroll, int time) {
		scroller.forceFinished(true);
		lastScrollY = scrollingOffset;
		int offset = itemsToScroll * getItemHeight();
		scroller.startScroll(0, lastScrollY, 0, offset - lastScrollY, time);
		setNextMessage(MESSAGE_SCROLL);
		startScrolling();
	}

	public void setWheelClick(onWheelClickListener l) {
		wClick = l;
	}

	public void setSingle() {
		single = true;
	}

	public void setViewHeight(int height) {
		mHeight = height;
	}

	public void setleftCheck(boolean mleftCheck) {
		leftCheck = mleftCheck;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = resolveSize(mWidth, widthMeasureSpec);
		final int height = resolveSize(mHeight, heightMeasureSpec);
		setMeasuredDimension(width, height);
	}
}
