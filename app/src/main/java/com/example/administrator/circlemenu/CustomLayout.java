package com.example.administrator.circlemenu;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.administrator.circlemenu.util.ToastUtil;

import static com.example.administrator.circlemenu.CircleMenuLayout.RADIO_DEFAULT_CENTERITEM_DIMENSION;

/**
 * 可滑动的customlayout
 */
public class CustomLayout extends RelativeLayout {
    private Context context;
    private ViewDragHelper mDragger;
    private View mAutoBackView;
    private Point mAutoBackOriginPos = new Point();
    private CircleMenuLayout circleMenuLayout;//圆圈
    private boolean isRelease = true;
    private boolean isInRing = false;//在圆环内吗？
    private customLayoutListener mCustomLayoutListener;

    public CustomLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                Log.d("回调顺序", "tryCaptureView");
                //开始移动的时候,开始震动
                if (child == mAutoBackView) {
                    isRelease = false;
                    circleMenuLayout.startShake();
                }
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


            //手指释放的时候回调(xvel与yvel是速率)
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //mAutoBackView手指释放时可以自动回去
                //停止震动
                if (releasedChild == mAutoBackView) {
                    circleMenuLayout.stopShake();
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
                Log.d("CustomLayout", "state:" + state);
                super.onViewDragStateChanged(state);
            }

            //移动的时候坐标
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                Log.d("CustomLayout", left + "||||" + top);
                if (changedView == mAutoBackView && isRelease) {//释放的时候点的位置
                    isRelease = false;
                    //点击的是某个扇形；按下点到中心点的距离大于中心圆半径小于大圆半径，那就是点击某个扇形了
                    float sweepAngle = 45;//每个弧形的角度
                    int angle = getRotationBetweenLines(centerX, centerX, left, top);
//                    Log.d("CustomLayout", "angle:" + angle);
                    //这个angle的角度是从正Y轴开始
                    angle = (angle + 360) % 360;
//                    Log.d("CustomLayout", "angle:" + angle);
                    int onClickState = (int) (angle / sweepAngle);//根据角度得出点击的是那个扇形
//                    Log.d("CustomLayout", centerX + "=====centerY========" + centerY);
//                    Log.d("CustomLayout", left + "====top=========" + top);
//                    Log.d("CustomLayout", radius + "======CenterRadius=======" + CenterRadius);
//                    Log.d("CustomLayout", "getDisForTwoSpot(centerX, centerY, left, top):" + getDisForTwoSpot(centerX, centerY, left, top));
                    if (getDisForTwoSpot(centerX, centerY, left, top) <= radius) {
                        if (getDisForTwoSpot(centerX, centerY, left, top) >= CenterRadius - 50) {
                            changedView.setVisibility(INVISIBLE);
                            if (mCustomLayoutListener != null) {
                                mCustomLayoutListener.onRingRelease(onClickState);
                            }
                        } else {
                            changedView.setVisibility(VISIBLE);
                            if (mCustomLayoutListener != null) {
                                mCustomLayoutListener.outRingRelease();
                            }
                        }
                    } else {
                        changedView.setVisibility(VISIBLE);
                        if (mCustomLayoutListener != null) {
                            mCustomLayoutListener.outRingRelease();
                        }
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
            Log.d("CustomLayout", "mAutoBackView.getBottom():" + mAutoBackView.getBottom());
        }
        //获取圆心位置，获取半径，获取坐标
        if (circleMenuLayout != null) {
            //大圆半径
            radius = circleMenuLayout.getWidth() / 2;
            Log.d("CustomLayout", "radius:" + radius);
            //中心圆半径
            CenterRadius = radius * RADIO_DEFAULT_CENTERITEM_DIMENSION;
            Log.d("CustomLayout", "CenterRadius:" + CenterRadius);

            //左上角坐标
            leftTopX = circleMenuLayout.getLeft();
            leftTopY = circleMenuLayout.getTop();
            //右下角坐标
            rightBottomX = circleMenuLayout.getLeft() + circleMenuLayout.getWidth();
            rightBottomY = circleMenuLayout.getTop() + circleMenuLayout.getWidth();
            //圆心坐标
            centerX = (rightBottomX + leftTopX) / 2;
            centerY = (leftTopY + rightBottomY) / 2;
            Log.d("CustomLayout", "leftTopX:" + leftTopX);
            Log.d("CustomLayout", "leftTopY:" + leftTopY);
            Log.d("CustomLayout", "rightBottomX:" + rightBottomX);
            Log.d("CustomLayout", "rightBottomY:" + rightBottomY);
        }
    }

    //半径
    private float radius = 0;
    private float CenterRadius = 0;
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

    /**
     * 获取两条线的夹角 * * @param centerX * @param centerY * @param xInView * @param yInView * @return
     */
    public static int getRotationBetweenLines(float centerX, float centerY, float xInView, float yInView) {
        double rotation = 0;

        double k1 = (double) (centerY - centerY) / (centerX * 2 - centerX);
        double k2 = (double) (yInView - centerY) / (xInView - centerX);
        double tmpDegree = Math.atan((Math.abs(k1 - k2)) / (1 + k1 * k2)) / Math.PI * 180;

        if (xInView > centerX && yInView < centerY) {  //第一象限
            rotation = 90 - tmpDegree;
        } else if (xInView > centerX && yInView > centerY) //第二象限
        {
            rotation = 90 + tmpDegree;
        } else if (xInView < centerX && yInView > centerY) { //第三象限
            rotation = 270 - tmpDegree;
        } else if (xInView < centerX && yInView < centerY) { //第四象限
            rotation = 270 + tmpDegree;
        } else if (xInView == centerX && yInView < centerY) {
            rotation = 0;
        } else if (xInView == centerX && yInView > centerY) {
            rotation = 180;
        }
        return (int) rotation;
    }

    public customLayoutListener getCustomLayoutListener() {
        return mCustomLayoutListener;
    }

    public void setCustomLayoutListener(customLayoutListener customLayoutListener) {
        mCustomLayoutListener = customLayoutListener;
    }


    public interface customLayoutListener {
        void onRingRelease(int location);//在圆环上松手

        void outRingRelease();
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
