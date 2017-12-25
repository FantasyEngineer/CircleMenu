package com.example.administrator.circlemenu;

import android.content.Context;
import android.graphics.Point;
import android.icu.util.IslamicCalendar;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 可滑动的customlayout
 */
public class CustomLayout extends RelativeLayout {
    private ViewDragHelper mDragger;

    private View mAutoBackView;

    private Point mAutoBackOriginPos = new Point();
    private CircleMenuLayout circleMenuLayout;//圆圈
    private boolean isRelease = true;

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                Log.d("回调顺序", "tryCaptureView");
                //开始移动的时候,开始震动
                isRelease = false;
                circleMenuLayout.startShake();
                //mEdgeTrackerView禁止直接移动
                return child == mAutoBackView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }


            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //mAutoBackView手指释放时可以自动回去
                //停止震动
                circleMenuLayout.stopShake();
                if (releasedChild == mAutoBackView) {
                    isRelease = true;
                    mDragger.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                    invalidate();
                }
            }

            //在边界拖动时回调
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
//                mDragger.captureChildView(mEdgeTrackerView, pointerId);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
            }

            //移动的时候坐标
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (isRelease) {//释放的时候点的位置
                    isRelease = false;
                    if (getDisForTwoSpot(left, top, centerX, centerY) < radius) {
                        Log.d("CustomLayout", "在圆内松手的");
                    } else {
                        Log.d("CustomLayout", "在圆外松手的");
                    }
                }
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }

            //当captureview被捕获时回调
            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {

                super.onViewCaptured(capturedChild, activePointerId);
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {

                super.onEdgeTouched(edgeFlags, pointerId);
            }

            @Override
            public boolean onEdgeLock(int edgeFlags) {

                return super.onEdgeLock(edgeFlags);
            }

            @Override
            public int getOrderedChildIndex(int index) {

                return super.getOrderedChildIndex(index);
            }
        });
        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragger.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragger.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mAutoBackView != null) {
            mAutoBackOriginPos.x = mAutoBackView.getLeft();
            mAutoBackOriginPos.y = mAutoBackView.getTop();
        }
        //获取圆心位置，获取半径，获取坐标
        if (circleMenuLayout != null) {
            radius = circleMenuLayout.getWidth() / 2;
            leftTopX = circleMenuLayout.getLeft();
            leftTopY = circleMenuLayout.getTop();
            rightBottomX = circleMenuLayout.getLeft() + circleMenuLayout.getWidth();
            rightBottomY = circleMenuLayout.getTop() + circleMenuLayout.getWidth();
            centerX = leftTopX + radius;
            centerY = leftTopY + radius - CircleMenuLayout.blankMargin / 2;
        }
    }

    //半径
    private float radius = 0;
    float leftTopX, leftTopY, rightBottomX, rightBottomY, centerX, centerY;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mAutoBackView = findViewById(R.id.new_item);
        circleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 求两个点之间的距离 * * @return
     */
    public static double getDisForTwoSpot(float x1, float y1, float x2, float y2) {
        float width, height;
        if (x1 > x2) {
            width = x1 - x2;
        } else {
            width = x2 - x1;
        }

        if (y1 > y2) {
            height = y2 - y1;
        } else {
            height = y2 - y1;
        }
        return Math.sqrt((width * width) + (height * height));
    }

}

/*
-------------------------->回调顺序
* 12-25 18:14:44.164 6521-6521/com.example.administrator.circlemenu D/回调顺序: getOrderedChildIndex
12-25 18:14:44.166 6521-6521/com.example.administrator.circlemenu D/回调顺序: getOrderedChildIndex
12-25 18:14:44.166 6521-6521/com.example.administrator.circlemenu D/回调顺序: tryCaptureView
12-25 18:14:44.167 6521-6521/com.example.administrator.circlemenu D/回调顺序: onViewCaptured
12-25 18:14:44.321 6521-6521/com.example.administrator.circlemenu D/回调顺序: onViewPositionChanged
12-25 18:14:45.338 6521-6521/com.example.administrator.circlemenu D/回调顺序: onViewPositionChanged
12-25 18:14:45.408 6521-6521/com.example.administrator.circlemenu D/回调顺序: onViewReleased
12-25 18:14:45.338 6521-6521/com.example.administrator.circlemenu D/回调顺序: onViewPositionChanged
12-25 18:14:45.338 6521-6521/com.example.administrator.circlemenu D/回调顺序: onViewPositionChanged

*
* */
