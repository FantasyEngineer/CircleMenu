package com.example.administrator.circlemenu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class RoundView extends View {
    //宽高
    private int coreX, coreY;
    private double radiusDistance;//半径的长度比（中心圆半径=大圆半径*radiusDistance）
    private int roundRadius;//中心圆的半径

    private List<RoundMenu> roundMenus;//菜单列表
    private float deviationDegree;//偏移角度
    private float blackDegree = 1;//每个扇形之间的空白角度
    private int onClickState = -2;//-2是无点击，-1是点击中心圆，其他是点击菜单
    private boolean isCoreMenu = false;//是否画中心圆

    //中心圆属性
    private float coreMenuStrokeSize;//中心圆的线宽
    private int coreMenuSelectColor;//中心圆按压时的颜色
    private int coreMenuColor;//按钮颜色
    private int coreMenuStrokeColor;//线条颜色
    private Bitmap coreBitmap;//中心圆的图片
    private OnClickListener onCoreClickListener;//中心圆点击事件
    private Context mContext;
    private long touchTime;//按下的时间
    private String coreMenuCenterText;//中心处的文字

    //上一个扇形的点
    double lastX1 = 0;
    double lastY1 = 0;
    //最后一个扇形绘制字的位置
    float endTextX = 0;
    float endTextY = 0;
    //解决一个bug
    int i = 0;
    private Paint paint;

    public RoundView(Context context) {
        super(context);
        this.mContext = context;
    }

    public RoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

    }

    public RoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        i++;
        deviationDegree = 0;//偏移角度(以x轴正方向的位置为起始点，顺时针画扇形)
        coreX = getWidth();
        coreY = getWidth();
        roundRadius = (int) (getWidth() / 2 * radiusDistance);//计算中心圆圈半径

        //画图片背景
        paint = new Paint();
        Bitmap bitmap = drawable2Bitmap(getResources().getDrawable(R.drawable.bg5));
        Bitmap newBmp = Bitmap.createScaledBitmap(bitmap, coreX, coreX, true);
        //初始化BitmapShader，传入bitmap对象
        BitmapShader bitmapShader = new BitmapShader(newBmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        //计算缩放比例
//        float mScale = (coreX) / Math.min(bitmap.getHeight(), bitmap.getWidth());
//        Matrix matrix = new Matrix();
//        matrix.setScale(mScale + 1, mScale + 1);
//        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        //画圆形，指定好中心点坐标、半径、画笔
        canvas.drawCircle(coreX / 2, coreX / 2, coreX / 2, paint);


        RectF rect = new RectF(0, 0, coreX, coreY);
        //画菜单
        if (roundMenus != null && roundMenus.size() > 0) {
            float sweepAngle = (360 - blackDegree * roundMenus.size()) / roundMenus.size();//每个弧形的角度（需要减去空白的区域）
            for (int i = 0; i < roundMenus.size(); i++) {
                RoundMenu roundMenu = roundMenus.get(i);
                //填充
                paint = new Paint();
                //抗锯齿
                paint.setAntiAlias(true);
                if (onClickState == i) {
                    //选中
                    paint.setColor(roundMenu.selectSolidColor);
                } else {
                    //未选中
                    paint.setColor(roundMenu.solidColor);
                }
                canvas.drawArc(rect, deviationDegree + i * (sweepAngle + blackDegree), sweepAngle, true, paint);//画圆的起始位置为x轴正坐标
                //画描边
//                paint.setAntiAlias(true);
//                paint.setStrokeWidth(roundMenu.strokeSize);
//                paint.setStyle(Paint.Style.STROKE);
//                paint.setColor(roundMenu.strokeColor);
//                canvas.drawArc(rect, deviationDegree + (i * sweepAngle), sweepAngle, roundMenu.useCenter, paint);

                //绘制扇形上文字
//                drawText(canvas, sweepAngle, i, paint);
                //画图案
//                Matrix matrix = new Matrix();
//                matrix.postTranslate((float) ((coreX + getWidth() / 2 * roundMenu.iconDistance) - (drawable2Bitmap(roundMenu.resID).getWidth() / 2)), coreY - (drawable2Bitmap(roundMenu.resID).getHeight() / 2));
//                matrix.postRotate(((i + 1) * sweepAngle), coreX, coreY);
//                canvas.drawBitmap(drawable2Bitmap(roundMenu.resID), matrix, null);
            }
        }
//        //绘制最后一个扇形区域内的文字
//        if (endTextX != 0 && endTextY != 0) {
//            canvas.drawText("名字", endTextX, endTextY, paint);    //绘制文字
//        }


        //画中心圆圈
        if (isCoreMenu) {
            //填充
            RectF rect1 = new RectF(coreX / 2 - roundRadius, coreY / 2 - roundRadius, coreX / 2 + roundRadius, coreY / 2 + roundRadius);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(coreMenuStrokeSize);
            if (onClickState == -1) {
                paint.setColor(coreMenuSelectColor);
            } else {
                paint.setColor(coreMenuColor);
            }
            canvas.drawArc(rect1, 0, 360, true, paint);

            //画描边
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(coreMenuStrokeSize);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(coreMenuStrokeColor);
            canvas.drawArc(rect1, 0, 360, true, paint);
            if (coreBitmap != null) {
                //画中心圆圈的“OK”图标
                canvas.drawBitmap(coreBitmap, coreX - coreBitmap.getWidth() / 2, coreY - coreBitmap.getHeight() / 2, null);//在 0，0坐标开始画入src
            }
            //绘制中心处的文字
            if (coreMenuCenterText != null) {
                paint.setColor(Color.WHITE); //文字颜色
                paint.setTextSize(19);//中心文字大小
                Rect rectText = new Rect();//用来测量文字宽高的。
                paint.getTextBounds(coreMenuCenterText, 0, coreMenuCenterText.length(), rectText);
                canvas.drawText(coreMenuCenterText, coreX / 2 - rectText.width() / 2, coreY / 2 + rectText.height() / 2, paint);    //中心点绘制文字
            }
        }

        //画扇形上的字
        if (roundMenus != null && roundMenus.size() > 0) {
            float sweepAngle = 360 / roundMenus.size();//每个弧形的角度
            for (int i = 0; i < roundMenus.size(); i++) {
                //绘制扇形上文字
                if (roundMenus.get(i).text != null) {
                    drawText(roundMenus.get(i).text, canvas, sweepAngle, i, paint);
                }
            }
        }
        //解决一个文字不展示的bug
        if (i == 1) {
            invalidate();//重新刷新一遍布局就会出现。 暂不知为何
        }
    }

    private void drawText(String text, Canvas canvas, float sweepAngle, int i, Paint paint) {
        int radius = getWidth() / 2;
        //圆心坐标
        int centerX = coreX / 2;
        int centerY = coreY / 2;
        Log.d("RoundViewcenterX", centerX + "=====" + centerY);

        float textAngle = deviationDegree + (i * sweepAngle);    //计算文字位置角度
        Log.d("RoundView", "textAngle:" + textAngle);
        double x1 = centerX + radius * Math.cos(textAngle * Math.PI / 180);
        double y1 = centerY + radius * Math.sin(textAngle * Math.PI / 180);
        paint.setColor(Color.BLACK);        //文字颜色
        paint.setTextSize(19);
        float textCurrentX = (float) (x1 + lastX1) / 2;
        float textCurrentY = (float) (y1 + lastY1) / 2;
        Log.d("textCurrent", textCurrentX + "=====" + textCurrentY);
        Rect rectText = new Rect();//用来测量文字宽高的。
        if (textCurrentX >= coreX * 3 / 4) {//如果在x正方向(并且处于3/4最右边)，绘制文字的时候往左移动
            paint.getTextBounds(text, 0, text.length(), rectText);
            textCurrentX -= rectText.width();
        } else if (textCurrentX <= coreX / 2 && textCurrentX >= coreX * 1 / 4) {
            paint.getTextBounds(text, 0, text.length(), rectText);
            textCurrentX -= rectText.width() / 2;
        }
        Log.d("14", "textCurrentY:" + textCurrentY + ">===============" + (double) coreY * 1 / 4);

        if (textCurrentY <= coreY * 1 / 4) {//处于y轴正方向最上方
            paint.getTextBounds(text, 0, text.length(), rectText);
            textCurrentY += rectText.height() * 2;//坐标校正
        } else if (textCurrentY >= coreY * 3 / 4) {
            textCurrentY -= rectText.height() * 2;//坐标校正
        }

        //给每个扇形上增加图
        canvas.drawText(text, textCurrentX, textCurrentY, paint);    //绘制文字
        Matrix matrix = new Matrix();
        matrix.postTranslate(textCurrentX, textCurrentY);
        canvas.drawBitmap(drawable2Bitmap(getResources().getDrawable(R.drawable.icon_sun)), matrix, null);

        lastX1 = x1;
        lastY1 = y1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchTime = new Date().getTime();
                float textX = event.getX();
                float textY = event.getY();
                int distanceLine = (int) getDisForTwoSpot(coreX / 2, coreY / 2, textX, textY);//距离中心点之间的直线距离
                if (distanceLine <= roundRadius) {
                    //点击的是中心圆；按下点到中心点的距离小于中心园半径，那就是点击中心园了
                    onClickState = -1;
                } else if (distanceLine <= getWidth() / 2) {
                    //点击的是某个扇形；按下点到中心点的距离大于中心圆半径小于大圆半径，那就是点击某个扇形了
                    float sweepAngle = 360 / roundMenus.size();//每个弧形的角度
                    int angle = getRotationBetweenLines(coreX / 2, coreY / 2, textX, textY);
                    //这个angle的角度是从正Y轴开始，而我们的扇形是从正X轴开始，再加上偏移角度，所以需要计算一下
                    angle = (angle + 360 - 90 - (int) deviationDegree) % 360;
                    onClickState = (int) (angle / sweepAngle);//根据角度得出点击的是那个扇形
                } else {
                    //点击了外面
                    onClickState = -2;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if ((new Date().getTime() - touchTime) < 300) {
                    //点击小于300毫秒算点击
//                    OnClickListener onClickListener = null;
                    if (onClickState == -1) {
                        if (mOnMenuItemClickListener != null) {
                            mOnMenuItemClickListener.itemCenterClick();
                        }
//                        onClickListener = onCoreClickListener;
                    } else if (onClickState >= 0 && onClickState < roundMenus.size()) {
//                        onClickListener = roundMenus.get(onClickState).onClickListener;
                        if (mOnMenuItemClickListener != null) {
                            mOnMenuItemClickListener.itemClick(onClickState);
                        }
                    }
//                    if (onClickListener != null) {
//                        onClickListener.onClick(this);
//                    }
                }
                onClickState = -2;
                invalidate();
                break;
        }
        return true;

    }

    /**
     * 添加菜单 * * @param roundMenu
     */
    public void addRoundMenu(RoundMenu roundMenu) {
        if (roundMenu == null) {
            return;
        }
        if (roundMenus == null) {
            roundMenus = new ArrayList<>();
        }
        roundMenus.add(roundMenu);
        invalidate();
    }


    /**
     * 扇形的对象类
     */
    public static class RoundMenu {
        public boolean useCenter = true;//扇形是否画连接中心点的直线
        public int solidColor = 0x00000000;//背景颜色,默认透明
        public int selectSolidColor = 0x33000000;//背景颜色,默认透明
        public int strokeColor = 0x00000000;//描边颜色,默认透明
        public int strokeSize = 1;//描边的宽度,默认1
        public int resID;//菜单的res R.drawable.
        //        public OnClickListener onClickListener;//点击监听
        public double iconDistance = 0.63;//图标距离中心点的距离
        public String text;//扇形区域上的字
    }

//
//    public Bitmap drawable2Bitmap(int resID) {
//        Resources res = getResources();
//        Bitmap bmp = BitmapFactory.decodeResource(res, resID);
//        return bmp;
//    }

    /**
     * 添加中心菜单按钮 * * @param coreMenuColor * @param coreMenuSelectColor * @param onClickListener
     */
    public void setCoreMenu(int coreMenuColor, int coreMenuSelectColor, int coreMenuStrokeColor,
                            int coreMenuStrokeSize, double radiusDistance, Bitmap bitmap, String coreMenuCenterText, OnClickListener onClickListener) {
        isCoreMenu = true;
        this.coreMenuColor = coreMenuColor;
        this.radiusDistance = radiusDistance;
        this.coreMenuSelectColor = coreMenuSelectColor;
        this.coreMenuStrokeColor = coreMenuStrokeColor;
        this.coreMenuStrokeSize = coreMenuStrokeSize;
        this.coreMenuCenterText = coreMenuCenterText;
        coreBitmap = bitmap;
        this.onCoreClickListener = onClickListener;
        invalidate();
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
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
     * MenuItem的点击事件接口
     *
     * @author zhy
     */
    public interface OnMenuItemClickListener {
        void itemClick(int pos);

        void itemCenterClick();
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
    public void setOnMenuItemClickListener(OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置自定义view的宽高相等
        heightMeasureSpec = widthMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //写一个drawble转BitMap的方法
    private Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}
