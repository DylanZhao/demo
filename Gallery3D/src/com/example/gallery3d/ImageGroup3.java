package com.example.gallery3d;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.example.gallery3d.Rotate3dAnimation.OnEndListener;

/**
 * 图片3D浏览效果组件。 实现要点： <li/>
 * 1.派生自RelativeLayout，重载onMeasure(),onLayout(),dispatchDraw
 * (),dispatchTouchEvent()事件.<li/>
 * 2.图片静态旋转方法为 ImageView.setRotation(),依赖API-11。<li/>
 * 3.图片切换时的动画，使用自定义的3D效果，主要用到Camera类。<li/>
 * 4.拦截触屏事件，响应左右滑动以切换图片。<li/>
 * 5.图片数据由绑定的Adapter提供，浏览为循环模式。
 * 
 * @author Yang
 * 
 */
public class ImageGroup3 extends RelativeLayout {
    private static final String TAG = "ImageGroup3";

    private final static int DEFAULT_CHILD_COUNT = 3;// 用于切换动画的图片组件数量
    private final static int ANIM_DURATION = 300;// 动画时长 ms
    private final static float DEFAULT_ROTATION = 30.0f;// 旋转角度

    private final static float CW_TO_FW = 0.6f;// 图片宽度占父控件宽度比例
    private final static float CH_TO_FW = 0.85f;// 图片高度占父控件宽度比例
    private final static float M_LEFT = (1 - CW_TO_FW) / 2;// 中间图片左边位置
    private final static float M_RIGHT = (1 + CW_TO_FW) / 2;// 中间图片右边位置
    private final static float R_LEFT = (1 - CW_TO_FW);// 右边图片左边位置
    private final static float DIS_M_R = (1 - CW_TO_FW) / 2;// 中间图片左边位置

    private int _childCount = DEFAULT_CHILD_COUNT;// 图片数量
    private int _middle = _childCount / 2;// 初始中间图片的下标

    private float _rotationY = DEFAULT_ROTATION;// 立体旋转角度

    private ImageAdapter _adapter;// 图片适配器
    private int _adapterPos = _middle;// 适配器游标

    public ImageGroup3(Context context) {
        this(context, null);
    }

    public ImageGroup3(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
        addChildren();
    }

    /**
     * 添加子view，共5个。门面左中右3张图，背后隐藏2张图。
     */
    private void addChildren() {
        for (int i = 0; i < _childCount + 2; i++) {
            ImageView iView = new ImageView(getContext());
            iView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            iView.setScaleType(ScaleType.FIT_XY);
            addView(iView);
        }
    }

    public void setAdapter(ImageAdapter adapter) {
        this._adapter = adapter;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure ");
        // 动态计算本组件高度。
        final int sw = getContext().getResources().getDisplayMetrics().widthPixels;
        this.getLayoutParams().height = (int) (sw * CH_TO_FW);
        for (int i = 0; i < _childCount + 2; i++) {
            getChildAt(i).getLayoutParams().width = (int) (getWidth() * CW_TO_FW);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 获取中间图片前一个图片的下标
     */
    private int getPre() {
        return (_middle + _childCount - 1) % _childCount;
    }

    /**
     * 获取中间图片后一个图片的下标
     */
    private int getNext() {
        return (_middle + 1) % _childCount;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout ");

        super.onLayout(changed, l, t, r, b);

        View child;
        LayoutParams params;

        // left
        child = getChildAt(getPre());
        params = (LayoutParams) child.getLayoutParams();
        child.layout(0, child.getTop(), child.getWidth(), child.getBottom());
        params.addRule(ALIGN_PARENT_LEFT);

        // middle
        child = getChildAt(_middle);
        params = (LayoutParams) child.getLayoutParams();
        child.layout((int) (M_LEFT * getWidth()), child.getTop(),
                (int) (M_RIGHT * getWidth()), child.getBottom());
        params.addRule(CENTER_HORIZONTAL);

        // right
        child = getChildAt(getNext());
        params = (LayoutParams) child.getLayoutParams();
        child.layout((int) (R_LEFT * getWidth()), child.getTop(),
                getWidth(), child.getBottom());
        params.addRule(ALIGN_PARENT_RIGHT);

        // behind right
        child = getChildAt(_childCount);
        params = (LayoutParams) child.getLayoutParams();
        child.layout((int) (R_LEFT * getWidth()), child.getTop(),
                getWidth(), child.getBottom());
        params.addRule(ALIGN_PARENT_RIGHT);

        // behind left
        child = getChildAt(_childCount + 1);
        params = (LayoutParams) child.getLayoutParams();
        child.layout(0, child.getTop(), child.getWidth(), child.getBottom());
        params.addRule(ALIGN_PARENT_LEFT);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.d(TAG, "dispatchDraw ");
        if (needCustomDraw()) {
            customDraw();
        }
        super.dispatchDraw(canvas);
    }

    /**
     * 是否需要重新绘制，滑动动画执行过程中不要重绘。
     */
    private boolean needCustomDraw() {
        return !isScrolling;
    }

    private void customDraw() {
        ImageView child;

        // left image rotation
        child = (ImageView) getChildAt(getPre());
        _adapter.fillImage(child, _adapterPos - 1);
        child.setPivotX(0);
        child.setPivotY(child.getHeight() / 2);
        child.setRotationY(_rotationY);

        // middle image
        child = (ImageView) getChildAt(_middle);
        _adapter.fillImage(child, _adapterPos);
        child.setRotationX(0);

        // right image rotation
        child = (ImageView) getChildAt(getNext());
        _adapter.fillImage(child, _adapterPos + 1);
        child.setPivotX(child.getWidth());
        child.setPivotY(child.getHeight() / 2);
        child.setRotationY(-1 * _rotationY);

        // behind right
        child = (ImageView) getChildAt(_childCount);
        _adapter.fillImage(child, _adapterPos + 2);
        child.setPivotX(child.getWidth());
        child.setPivotY(child.getHeight() / 2);
        child.setRotationY(-1.2f * _rotationY);

        // behind left
        child = (ImageView) getChildAt(_childCount + 1);
        _adapter.fillImage(child, _adapterPos - 2);
        child.setPivotX(0);
        child.setPivotY(child.getHeight() / 2);
        child.setRotationY(1.2f * _rotationY);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        // 自定义图片渲染顺序，避免中间的图片被遮盖。
        if (i == 0) {
            return childCount - 1;
        } else if (i == 1) {
            return childCount - 2;
        } else if (i == 2) {
            return getNext();
        } else if (i == 3) {
            return getPre();
        } else if (i == 4) {
            return _middle;
        }
        return i;
    }

    private boolean isMoving = false;// 正在手势移动
    private float downX;// 记录触摸事件DOWN时的位置

    private final static int LEFT = -1;
    private final static int RIGHT = 1;

    private boolean isScrolling = false;// 正在执行动画
    private final int MIN_SLIDE = 10;// 手势移动超过此值后，触发滑动动画。

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {

        case MotionEvent.ACTION_DOWN:
            downX = ev.getX();
            break;

        case MotionEvent.ACTION_MOVE:
            if (isMoving || isScrolling) {
                break;
            }
            final float curX = ev.getX();
            if (Math.abs(curX - downX) >= MIN_SLIDE) {
                isMoving = true;
                slide((curX < downX) ? LEFT : RIGHT);
            }
            break;

        case MotionEvent.ACTION_UP:
            isMoving = false;
            break;

        }
        return true;
    }

    /**
     * 触发滑动动画。
     */
    private void slide(final int direction) {
        isScrolling = true;
        if (direction == LEFT) {
            slideLeft();
        } else if (direction == RIGHT) {
            slideRight();
        }
    }

    /**
     * 向左滑动。中间移到左边，右边移到中间。
     */
    private void slideLeft() {
        Log.d(TAG, "slideLeft, _middle=" + _middle);
        middleToLeft();
        rightToMiddle();
    }

    /**
     * 向右滑动。中间移到右边，左边移到中间。
     */
    private void slideRight() {
        Log.d(TAG, "slideRight, _middle=" + _middle);
        middleToRight();
        leftToMiddle();
    }

    /**
     * 中间移到左边，
     */
    private void middleToLeft() {
        final View child = getChildAt(_middle);
        final Rotate3dAnimation rotation = new Rotate3dAnimation(0, _rotationY)
                .setPivot(0, child.getHeight() / 2)
                .setTrans(-child.getLeft(), 0);
        rotation.setFillAfter(true);
        rotation.setDuration(ANIM_DURATION);
        rotation.setInterpolator(new AccelerateInterpolator());
        child.startAnimation(rotation);
    }

    /**
     * 中间移到右边，
     */
    private void middleToRight() {
        final View child = getChildAt(_middle);
        final Rotate3dAnimation rotation = new Rotate3dAnimation(0,
                0 - _rotationY)
                .setPivot(child.getWidth(), child.getHeight() / 2)
                .setTrans(DIS_M_R * getWidth(), 0);
        rotation.setFillAfter(true);
        rotation.setDuration(ANIM_DURATION);
        rotation.setInterpolator(new AccelerateInterpolator());

        child.startAnimation(rotation);
    }

    /**
     * 右边移到中间，结束后改变中心下标，并重绘界面。
     */
    private void rightToMiddle() {

        final View child = getChildAt(getNext());
        child.setRotationY(0);
        final Rotate3dAnimation rotation = new Rotate3dAnimation(-_rotationY, 0)
                .setPivot(child.getWidth(), child.getHeight() / 2)
                .setTrans(-DIS_M_R * getWidth(), 0);
        rotation.setFillAfter(true);
        rotation.setDuration(ANIM_DURATION);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setEndListener(new OnEndListener() {

            @Override
            public void onEnd() {
                selectChange(RIGHT);
                isScrolling = false;
                reset();
            }
        });
        child.startAnimation(rotation);

    }

    /**
     * 左边移到中间，结束后改变中心下标，并重绘界面。
     */
    private void leftToMiddle() {
        final View child = getChildAt(getPre());
        child.setRotationY(0);
        final Rotate3dAnimation rotation = new Rotate3dAnimation(_rotationY, 0)
                .setPivot(0, child.getHeight() / 2)
                .setTrans(M_LEFT * getWidth(), 0);
        rotation.setFillAfter(true);
        rotation.setDuration(ANIM_DURATION);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setEndListener(new OnEndListener() {

            @Override
            public void onEnd() {
                selectChange(LEFT);
                isScrolling = false;
                reset();
            }

        });
        child.startAnimation(rotation);
    }

    /**
     * 改变中心位置，适配器游标左/右移
     */
    private void selectChange(final int dx) {
        _middle = (dx > 0 ? getNext() : getPre());
        _adapterPos += dx;
    }

    /**
     * 界面刷新重绘
     */
    private void reset() {
        removeAllViews();
        addChildren();
        requestLayout();
    }
}
