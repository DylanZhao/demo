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
 * 图片3D浏览及交互组件，未完待续。 <br>
 * 实现要点： <li/>
 * 1.派生自RelativeLayout，重载onMeasure(),onLayout(),dispatchDraw
 * (),dispatchTouchEvent()事件. <li/>
 * 2.图片静态旋转方法为 ImageView.setRotation(),依赖API-11。 <li/>
 * 3.拦截触屏事件，左右滑动时图片实时跟随手指滑动，手指放开后，图片回归原位或者左右切换。 <li/>
 * 4.触屏手势onMove时，计算移动方向及距离，实时刷新相关图片的位置及旋转角。 <li/>
 * 5.图片数据由绑定的Adapter提供，浏览为循环模式。 <br>
 * 待完善:<li/>
 * 1.手指来回左右滑动时多张图片的联动效果。<li/>
 * 2。手指松开时，如果滑动距离较短，图片回归原位，这个过程的动画效果。<li/>
 * 3.手指松开时，如果滑动距离较长，图片左右切换，这个过程的动画效果<li/>
 * 4.多个动画同时执行时的性能优化。
 */
public class ImageGroup2 extends RelativeLayout {
    private static final String TAG = "ImageGroup2";

    private final static int DEFAULT_CHILD_COUNT = 3;
    private final static float DEFAULT_ROTATION = 30.0f;

    private int _childCount = DEFAULT_CHILD_COUNT;// 图片数量
    private int _middle = _childCount / 2;

    private float _rotationY = DEFAULT_ROTATION;// 立体旋转角度

    private ImageAdapter _adapter;// 图片适配器
    private int _adapterPos = _middle;// 适配器游标

    public ImageGroup2(Context context) {
        this(context, null);
    }

    public ImageGroup2(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
        addChildren();
    }

    private void addChildren() {
        for (int i = 0; i < _childCount; i++) {
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
        for (int i = 0; i < _childCount; i++) {
            getChildAt(i).getLayoutParams().width = getWidth() / 2;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int getPre() {
        return (_middle + _childCount - 1) % _childCount;
    }

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
        child.layout(0, child.getTop(), getWidth() / 2, child.getBottom());
        params.addRule(CENTER_HORIZONTAL, 0);
        params.addRule(ALIGN_PARENT_RIGHT, 0);
        params.addRule(ALIGN_PARENT_LEFT);

        // middle
        child = getChildAt(_middle);
        params = (LayoutParams) child.getLayoutParams();
        child.layout(getWidth() / 4, child.getTop(), getWidth() * 3 / 4,
                child.getBottom());
        params.addRule(ALIGN_PARENT_LEFT, 0);
        params.addRule(ALIGN_PARENT_RIGHT, 0);
        params.addRule(CENTER_HORIZONTAL);

        // right
        child = getChildAt(getNext());
        params = (LayoutParams) child.getLayoutParams();
        child.layout(getWidth() / 2, child.getTop(), getWidth(),
                child.getBottom());
        params.addRule(ALIGN_PARENT_LEFT, 0);
        params.addRule(CENTER_HORIZONTAL, 0);
        params.addRule(ALIGN_PARENT_RIGHT);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.d(TAG, "dispatchDraw ");
        if (needCustomDraw()) {
            customDraw();
        }
        super.dispatchDraw(canvas);
    }

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
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (i == childCount - 1) {
            return _middle;
        } else if (i == _middle) {
            return childCount - 1;
        }
        return i;
    }

    // private final static int MIN_X = 5;
    private boolean isMoving = false;// 正在手势移动
    private float downX;// 记录触摸事件DOWN时的位置
    // private float downY;// 记录触摸事件DOWN时的位置
    private float moveX;// 记录触摸事件MOVE时的位置

    // private float moveY;// 记录触摸事件MOVE时的位置
    private final static int LEFT = -1;
    private final static int RIGHT = 1;

    private boolean isScrolling = false;// 正在执行动画
    private final int MIN_SLIDE = 10;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {

        case MotionEvent.ACTION_DOWN:
            moveX = downX = ev.getX();
            Log.d(TAG, "dispatchTouchEvent action:ACTION_DOWN, downX=" + downX);

            break;

        case MotionEvent.ACTION_MOVE:
            Log.d(TAG, "dispatchTouchEvent action:ACTION_MOVE");
            if (isMoving || isScrolling) {
                break;
            }
            final float curX = ev.getX();
            if (Math.abs(curX - downX) < MIN_SLIDE) {
                moveX = curX;
            } else {
                isMoving = true;
                slide((curX < downX) ? LEFT : RIGHT);
            }
            break;

        case MotionEvent.ACTION_UP:
            Log.d(TAG, "dispatchTouchEvent action:ACTION_UP");
            isMoving = false;
            break;

        case MotionEvent.ACTION_CANCEL:

            Log.d(TAG, "dispatchTouchEvent action:ACTION_CANCEL");

            break;

        }
        return true;
    }

    private void slide(final int direction) {
        isScrolling = true;
        if (direction == LEFT) {
            slideLeft();
        } else if (direction == RIGHT) {
            slideRight();
        }
    }

    private void slideLeft() {
        Log.d(TAG, "slideLeft, _middle=" + _middle);
        middleToLeft();
        rightToMiddle();
    }

    private void slideRight() {
        Log.d(TAG, "slideRight, _middle=" + _middle);
        middleToRight();
        leftToMiddle();
    }

    private void middleToLeft() {
        final View child = getChildAt(_middle);
        final Rotate3dAnimation rotation = new Rotate3dAnimation(0, _rotationY)
                .setPivot(0, child.getHeight() / 2)
                .setTrans(-child.getWidth() / 2, 0);
        rotation.setFillAfter(true);
        rotation.setDuration(500);
        rotation.setInterpolator(new AccelerateInterpolator());
        child.startAnimation(rotation);
    }

    private void rightToMiddle() {

        final View child = getChildAt(getNext());
        child.setRotationY(0);
        final Rotate3dAnimation rotation = new Rotate3dAnimation(-_rotationY, 0)
                .setPivot(child.getWidth(), child.getHeight() / 2)
                .setTrans(-child.getWidth() / 2, 0);
        rotation.setFillAfter(true);
        rotation.setDuration(500);
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

    private void leftToMiddle() {
        final View child = getChildAt(getPre());
        child.setRotationY(0);
        final Rotate3dAnimation rotation = new Rotate3dAnimation(_rotationY, 0)
                .setPivot(0, child.getHeight() / 2)
                .setTrans(child.getWidth() / 2, 0);
        rotation.setFillAfter(true);
        rotation.setDuration(500);
        rotation.setInterpolator(new AccelerateInterpolator());
        child.startAnimation(rotation);

    }

    private void middleToRight() {
        final View child = getChildAt(_middle);
        final Rotate3dAnimation rotation = new Rotate3dAnimation(0,
                0 - _rotationY)
                .setPivot(child.getWidth(), child.getHeight() / 2)
                .setTrans(child.getWidth() / 2, 0);
        rotation.setFillAfter(true);
        rotation.setDuration(500);
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

    private void selectChange(final int dx) {
        _middle = (dx > 0 ? getNext() : getPre());
        _adapterPos += dx;
    }

    private void reset() {
        removeAllViews();
        addChildren();
        requestLayout();
    }
}
