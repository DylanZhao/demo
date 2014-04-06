package com.example.gallery3d;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * An animation that rotates the view on the Y axis between two specified
 * angles. This animation also adds a translation on the Z axis (depth) to
 * improve the effect.
 */
public class Rotate3dAnimation extends Animation {

    private final float _fromDegrees;
    private final float _toDegrees;

    private float _pivotX = 0;
    private float _pivotY = 0;

    private float _transX = 0;
    private float _transY = 0;

    private Camera _camera;

    private OnEndListener _endListener;
    private AnimationListener listener = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (null != _endListener) {
                try {
                    _endListener.onEnd();
                } catch (Exception e) {
                }
            }
        }
    };

    public Rotate3dAnimation(final float fromDegrees, final float toDegrees) {
        _fromDegrees = fromDegrees;
        _toDegrees = toDegrees;
        setAnimationListener(listener);
    }

    public Rotate3dAnimation setPivot(final float pivotX, final float pivotY) {
        _pivotX = pivotX;
        _pivotY = pivotY;
        return this;
    }

    public Rotate3dAnimation setTrans(
            final float transX, final float transY) {
        _transX = transX;
        _transY = transY;
        return this;
    }

    public Rotate3dAnimation setEndListener(final OnEndListener endListener) {
        _endListener = endListener;
        return this;
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
            int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        _camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        final Matrix matrix = t.getMatrix();
        _camera.save();
        _camera.translate(
                _transX * interpolatedTime, _transY * interpolatedTime, 0);
        _camera.rotateY(
                _fromDegrees + ((_toDegrees - _fromDegrees) * interpolatedTime));
        _camera.getMatrix(matrix);

        matrix.preTranslate(-_pivotX, -_pivotY);
        matrix.postTranslate(_pivotX, _pivotY);

        _camera.restore();

    }

    public interface OnEndListener {
        void onEnd();
    }

}