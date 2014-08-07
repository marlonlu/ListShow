package com.marlonlu.ListShow;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by marlonlu on 2014/8/6.
 */
public class MyListView extends ViewGroup{

    static final String TAG = "marlonlu";

    Context mContext;
    private GestureDetector  mGestureDetector;
    Scroller mScroller;

    public MyListView(Context context) {
        super(context);

        init(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    private void init(Context context) {
        mContext = context;

        TextView view = new TextView(context);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        view.setText("Hello, world");
        addView(view);

        TextView view1 = new TextView(context);
        view1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        view1.setText("hahaha, Android");
        addView(view1);

        mGestureDetector = new GestureDetector(getContext(), mOnGestureListener);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout");

        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;


        int top = (t + b) / 2 ;

        for(int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);

            int w = view.getMeasuredWidth();
            int h = view.getMeasuredHeight();

            view.layout(l + screenWidth - w , top + (i - 1) * 30, r, b);

            top += h;
        }

        int offset = getScrollY();
        if(Math.abs(offset) > 500)
        {
           int dy = offset > 0 ? (offset - 500) : offset + 500;
           mScroller.startScroll(0, offsetY, 0, -dy);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < this.getChildCount(); ++i) {
            View view = this.getChildAt(i);

            view.measure(
                    MeasureSpec.makeMeasureSpec(720,MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(1280, MeasureSpec.AT_MOST));
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            Log.d(TAG, "computeScroll, scroll to :"  + mScroller.getCurrY());
            postInvalidate();
        } else
            offsetY = getScrollY();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return mGestureDetector.onTouchEvent(ev);
    }

    int offsetY = 0;

    GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            if(Math.abs(distanceY) < 5)
                return true;

            if(!mScroller.computeScrollOffset()) {
               mScroller.abortAnimation();
            }

            scrollBy(0, (int) distanceY);
            postInvalidate();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG, "fling : " + velocityY);
                mScroller.abortAnimation();
                mScroller.fling(mScroller.getCurrX(), getScrollY(),(int) velocityX, (int)(-velocityY),  0, 0, -500, 500);

                postInvalidate();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d(TAG, "onDown");
            mScroller.abortAnimation();
            postInvalidate();

            return true;
        }
    };
}
