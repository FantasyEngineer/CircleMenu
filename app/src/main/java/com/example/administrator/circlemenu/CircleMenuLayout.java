package com.example.administrator.circlemenu;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * <pre>
 */
public class CircleMenuLayout extends ViewGroup {
    private int mRadius;

    private float mRadiusCenter;
    /**
     * 该容器内child item的默认尺寸
     */
    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;
    /**
     * 菜单的中心child的默认尺寸
     */
    public static float RADIO_DEFAULT_CENTERITEM_DIMENSION = 0.66f;
    /**
     * 该容器的内边距,无视padding属性，如需边距请用该变量
     */
    private static final float RADIO_PADDING_LAYOUT = 1 / 12f;
    /**
     * 该容器的内边距,无视padding属性，如需边距请用该变量
     */
    private float mPadding;
    /**
     * 布局时的开始角度
     */
    public static double mStartAngle = 20.5 - 90d;
    /**
     * 菜单项的文本
     */
    private String[] mItemTexts;
    /**
     * 菜单项的图标
     */
    private int[] mItemImgs;
    /**
     * 菜单项的按压图标
     */
    private int[] mItemImgsPress;

    /**
     * 菜单的个数
     */
    private int mMenuItemCount;

    private int mMenuItemLayoutId = R.layout.circle_menu_item;
    //使用屏幕的宽度减去下面这个值为layout的实际宽度
    public static int blankMargin = 0;

    private ArrayList<View> imageViewList = new ArrayList<>();

    public CircleMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 无视padding
        setPadding(0, 0, 0, 0);
    }

    private Paint paint;
    private float blackDegree = 1;//每个扇形之间的空白角度
    private float deviationDegree;//偏移角度
    //宽高
    private int coreX, coreY;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint();
        deviationDegree = 0;//偏移角度(以x轴正方向的位置为起始点，顺时针画扇形)
        coreX = getWidth();
        coreY = getWidth();
        //画圆形，指定好中心点坐标、半径、画笔
        canvas.drawCircle(coreX / 2, coreX / 2, coreX / 2, paint);


        RectF rect = new RectF(0, 0, coreX, coreY);
        //画菜单
        float sweepAngle = 45;//每个弧形的角度（需要减去空白的区域）
        for (int i = 0; i < 8; i++) {
            //填充
            paint = new Paint();
            //抗锯齿
            paint.setAntiAlias(true);
            paint.setColor(0x00000000);
            canvas.drawArc(rect, deviationDegree + i * (sweepAngle + blackDegree), sweepAngle, true, paint);//画圆的起始位置为x轴正坐标
        }

    }

    /**
     * 设置布局的宽高，并策略menu item宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        /**
//         * 根据传入的参数，分别获取测量模式和测量值
//         */
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//
//        /**
//         * 如果宽或者高的测量模式非精确值
//         */
//        if (widthMode != MeasureSpec.EXACTLY
//                || heightMode != MeasureSpec.EXACTLY) {
//            // 主要设置为背景图的高度
////            resWidth = getSuggestedMinimumWidth();
//            // 如果未设置背景图片，则设置为屏幕宽高的默认值
//            resWidth = resWidth == 0 ? getDefaultWidth() - blankMargin : resWidth;
//
////            resHeight = getSuggestedMinimumHeight();
//            // 如果未设置背景图片，则设置为屏幕宽高的默认值
//            resHeight = resHeight == 0 ? getDefaultWidth() - blankMargin : resHeight;
//        } else {
//            // 如果都设置为精确值，则直接取小值；
//            resWidth = resHeight = Math.min(width, height);
//        }
        heightMeasureSpec = widthMeasureSpec;
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);//确定viewgroup的大小

        // 获得半径
        mRadius = Math.min(getMeasuredWidth(), getMeasuredHeight());

        // menu item数量
        final int count = getChildCount();
        // menu item尺寸
        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        // menu item测量模式
        int childMode = MeasureSpec.EXACTLY;

        // 迭代测量
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            // 计算menu item的尺寸；以及和设置好的模式，去对item进行测量
            int makeMeasureSpec = -1;

            if (child.getId() == R.id.id_circle_menu_item_center) {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(
                        (int) (mRadius * RADIO_DEFAULT_CENTERITEM_DIMENSION),
                        childMode);
            } else {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize,
                        childMode);
            }
            child.measure(makeMeasureSpec, makeMeasureSpec);
        }

        mPadding = RADIO_PADDING_LAYOUT * mRadius;

    }

    public void setMenuItemIconsPress(int[] itemImgs) {
        mItemImgsPress = itemImgs;
    }

    /**
     * MenuItem的点击事件接口
     *
     * @author zhy
     */
    public interface OnMenuItemClickListener {
        void itemClick(View view, int pos);

        void itemCenterClick(View view);

    }

    /**
     * MenuItem的点击事件接口
     */
    private OnMenuItemClickListener mOnMenuItemClickListener;

    /**
     * 设置MenuItem的点击事件接口
     *
     * @param mOnMenuItemClickListener
     */
    public void setOnMenuItemClickListener(
            OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }

    /**
     * 设置menu item的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int layoutRadius = mRadius;

        // Laying out the child views
        final int childCount = getChildCount();

        int left, top;
        // menu item 的尺寸
        int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        // 根据menu item的个数，计算角度
        float angleDelay = 360 / (getChildCount() - 1);

        // 遍历去设置menuitem的位置
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getId() == R.id.id_circle_menu_item_center)
                continue;

            if (child.getVisibility() == GONE) {
                continue;
            }

            mStartAngle %= 360;

            // 计算，中心点到menu item中心的距离
//            float tmp = layoutRadius / 2f - cWidth / 15 - mPadding;
            float tmp = layoutRadius / 2f - mPadding;
            Log.d("CircleMenuLayout", "tmp:" + tmp);

            // tmp cosa 即menu item中心点的横坐标
            left = layoutRadius
                    / 2
                    + (int) Math.round(tmp
                    * Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f
                    * cWidth);
            // tmp sina 即menu item的纵坐标
            top = layoutRadius
                    / 2
                    + (int) Math.round(tmp
                    * Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f
                    * cWidth);

            child.layout(left, top, left + cWidth, top + cWidth);
            // 叠加尺寸
            mStartAngle += angleDelay;
        }

        // 找到中心的view，如果存在设置onclick事件
        View cView = findViewById(R.id.id_circle_menu_item_center);
        if (cView != null) {
            cView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.itemCenterClick(v);
                    }
                }
            });
            // 设置center item位置
            int cl = layoutRadius / 2 - cView.getMeasuredWidth() / 2;
            int cr = cl + cView.getMeasuredWidth();
            cView.layout(cl, cl, cr, cr);//正方形
            mRadiusCenter = (cr - cl) / 2;//内圆圆心
        }
    }

//    /**
//     * 记录上一次的x，y坐标
//     */
//    private float mLastX;
//    private float mLastY;
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//
//                mLastX = x;
//                mLastY = y;
//                mTmpAngle = 0;
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                /**
//                 * 获得开始的角度
//                 */
//                float start = getAngle(mLastX, mLastY);
//                /**
//                 * 获得当前的角度
//                 */
//                float end = getAngle(x, y);
//
//                // Log.e("TAG", "start = " + start + " , end =" + end);
//                // 如果是一、四象限，则直接end-start，角度值都是正值
//                if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
//                    mStartAngle += end - start;
//                    mTmpAngle += end - start;
//                } else
//                // 二、三象限，色角度值是付值
//                {
//                    mStartAngle += start - end;
//                    mTmpAngle += start - end;
//                }
//                // 重新布局
////			requestLayout();
//                mLastX = x;
//                mLastY = y;
//                break;
//            case MotionEvent.ACTION_UP:
//                // 如果当前旋转角度超过NOCLICK_VALUE屏蔽点击
//                if (Math.abs(mTmpAngle) > NOCLICK_VALUE) {
//                    return true;
//                }
//                break;
//        }
//        return super.dispatchTouchEvent(event);
//    }

    /**
     * 主要为了action_down时，返回true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * 根据触摸的位置，计算角度
     *
     * @param xTouch
     * @param yTouch
     * @return
     */
    private float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mRadius / 2d);
        double y = yTouch - (mRadius / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    /**
     * 根据当前位置计算象限
     *
     * @param x
     * @param y
     * @return
     */
    private int getQuadrant(float x, float y) {
        int tmpX = (int) (x - mRadius / 2);
        int tmpY = (int) (y - mRadius / 2);
        if (tmpX >= 0) {
            return tmpY >= 0 ? 4 : 1;
        } else {
            return tmpY >= 0 ? 3 : 2;
        }

    }

    /**
     * 设置菜单条目的图标和文本
     *
     * @param resIds
     */
    public void setMenuItemIconsAndTexts(int[] resIds, String[] texts) {
        mItemImgs = resIds;
        mItemTexts = texts;

        // 参数检查
        if (resIds == null && texts == null) {
            throw new IllegalArgumentException("菜单项文本和图片至少设置其一");
        }

        // 初始化mMenuCount
        mMenuItemCount = resIds == null ? texts.length : resIds.length;

        if (resIds != null && texts != null) {
            mMenuItemCount = Math.min(resIds.length, texts.length);
        }

        addMenuItems();

    }

    /**
     * 设置MenuItem的布局文件，必须在setMenuItemIconsAndTexts之前调用
     *
     * @param mMenuItemLayoutId
     */
    public void setMenuItemLayoutId(int mMenuItemLayoutId) {
        this.mMenuItemLayoutId = mMenuItemLayoutId;
    }

    /**
     * 添加菜单项
     */
    private void addMenuItems() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());

        /**
         * 根据用户设置的参数，初始化view
         */
        for (int i = 0; i < mMenuItemCount; i++) {
            final int j = i;
            View view = mInflater.inflate(mMenuItemLayoutId, this, false);
            final ImageView iv = (ImageView) view
                    .findViewById(R.id.id_circle_menu_item_image);
            TextView tv = (TextView) view
                    .findViewById(R.id.id_circle_menu_item_text);

            if (iv != null && mItemImgs[i] != 0) {//不能为零
                //将显示出来的imageview添加到list中，遍历用于震动
                imageViewList.add(iv);
                iv.setVisibility(View.VISIBLE);
                iv.setImageResource(mItemImgs[i]);
                final int finalI1 = i;
                iv.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.d("CircleMenuLayout", "finalI1:" + finalI1);
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                iv.setImageResource(R.drawable.home_mbank_1_clicked);
                                break;
                            case MotionEvent.ACTION_UP:
                                iv.setImageResource(mItemImgs[finalI1]);
                                break;
                        }
                        return false;
                    }
                });
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mOnMenuItemClickListener != null) {
                            mOnMenuItemClickListener.itemClick(v, j);
                        }
                    }
                });
            }
            if (tv != null) {
//				tv.setVisibility(View.VISIBLE);
//				tv.setText(mItemTexts[i]);
            }

            // 添加view到容器中
            addView(view);
        }
    }


    /**
     * 设置内边距的比例
     *
     * @param mPadding
     */
    public void setPadding(float mPadding) {
        this.mPadding = mPadding;
    }

    /**
     * 获得默认该layout的尺寸
     *
     * @return
     */
    private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        Log.d("CircleMenuLayout", "Math.min(outMetrics.widthPixels, outMetrics.heightPixels):" + Math.min(outMetrics.widthPixels, outMetrics.heightPixels));
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }

    //动画各个震动view的ObjectAnimator，用于遍历停止动画
    ArrayList<ObjectAnimator> animatorList = new ArrayList<>();
    //是否在震动
    boolean isShake = false;

    /**
     * 开始震动
     */
    public void startShake() {
        //当正在震动时，再调用此方法，直接返回
        if (isShake) {
            return;
        }
        isShake = true;
        //存放停止震动的view
        animatorList.clear();
        if (imageViewList != null && imageViewList.size() != 0) {
            for (View view : imageViewList) {
                ObjectAnimator animator = tada(view);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.start();
                //用于停止动画
                animatorList.add(animator);
            }
        }
    }

    /**
     * 停止震动
     */
    public void stopShake() {
        //当已经停止，再调用这个方法直接返回
        if (isShake) {
            isShake = false;
        } else {
            return;
        }
        if (animatorList != null && animatorList.size() != 0) {
            for (ObjectAnimator objectAnimator : animatorList) {
                objectAnimator.pause();
            }
        }

    }

    public float getCenterRadius() {
        return mRadiusCenter;
    }


    public ObjectAnimator tada(View view) {
        return tada(view, 1f);
    }

    public ObjectAnimator tada(View view, float shakeFactor) {

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).
                setDuration(1000);
    }

}
