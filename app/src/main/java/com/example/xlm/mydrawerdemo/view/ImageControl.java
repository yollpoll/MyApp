package com.example.xlm.mydrawerdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ImageControl extends android.support.v7.widget.AppCompatImageView {
    public ImageControl(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ImageControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ImageControl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    // ImageView img;
    Matrix imgMatrix = null; // 定义图片的变换矩阵

    static final int DOUBLE_CLICK_TIME_SPACE = 300; // 双击时间间隔
    static final int DOUBLE_POINT_DISTANCE = 10; // 两点放大两点间最小间距
    static final int NONE = 0;
    static final int DRAG = 1; // 拖动操作
    static final int ZOOM = 2; // 放大缩小操作
    private int mode = NONE; // 当前模式

    float bigScale = 3f; // 默认放大倍数
    Boolean isBig = false; // 是否是放大状态
    long lastClickTime = 0; // 单击时间
    float startDistance; // 多点触摸两点距离
    float endDistance; // 多点触摸两点距离

    float topHeight; // 状态栏高度和标题栏高度
    Bitmap primaryBitmap = null;

    float contentW; // 屏幕内容区宽度
    float contentH; // 屏幕内容区高度

    float primaryW; // 原图宽度
    float primaryH; // 原图高度

    float scale; // 适合屏幕缩放倍数
    Boolean isMoveX = true; // 是否允许在X轴拖动
    Boolean isMoveY = true; // 是否允许在Y轴拖动
    float startX;
    float startY;
    float endX;
    float endY;
    float subX;
    float subY;
    float limitX1;
    float limitX2;
    float limitY1;
    float limitY2;
    ICustomMethod mCustomMethod = null;

    /**
     * 初始化图片
     *
     * @param bitmap    要显示的图片
     * @param contentW  内容区域宽度
     * @param contentH  内容区域高度
     * @param topHeight 状态栏高度和标题栏高度之和
     */
    public void imageInit(Bitmap bitmap, int contentW, int contentH,
                          int topHeight, ICustomMethod iCustomMethod) {
        this.primaryBitmap = bitmap;
        this.contentW = contentW;
        this.contentH = contentH;
        this.topHeight = topHeight;
        mCustomMethod = iCustomMethod;
        primaryW = primaryBitmap.getWidth();
        primaryH = primaryBitmap.getHeight();
        float scaleX = (float) contentW / primaryW;
        float scaleY = (float) contentH / primaryH;
        scale = scaleX < scaleY ? scaleX : scaleY;
        if (scale < 1 && 1 / scale < bigScale) {
            bigScale = (float) (1 / scale + 0.5);
        }

        imgMatrix = new Matrix();
        subX = (contentW - primaryW * scale) / 2;
        subY = (contentH - primaryH * scale) / 2;
        this.setImageBitmap(primaryBitmap);
        this.setScaleType(ScaleType.MATRIX);
        imgMatrix.postScale(scale, scale);
        imgMatrix.postTranslate(subX, subY);
        this.setImageMatrix(imgMatrix);
    }

    /**
     * 按下操作
     *
     * @param event
     */
    public void mouseDown(MotionEvent event) {
        mode = NONE;
        startX = event.getRawX();
        startY = event.getRawY();
        if (event.getPointerCount() == 1) {
            // 如果两次点击时间间隔小于一定值，则默认为双击事件
            if (event.getEventTime() - lastClickTime < DOUBLE_CLICK_TIME_SPACE) {
                changeSize(startX, startY);
            } else if (isBig) {
                mode = DRAG;
            }
        }

        lastClickTime = event.getEventTime();
    }

    /**
     * 非第一个点按下操作
     *
     * @param event
     */
    public void mousePointDown(MotionEvent event) {
        startDistance = getDistance(event);
        if (startDistance > DOUBLE_POINT_DISTANCE) {
            mode = ZOOM;
        } else {
            mode = NONE;
        }
    }

    /**
     * 移动操作
     *
     * @param event
     */
    public void mouseMove(MotionEvent event) {
        if ((mode == DRAG) && (isMoveX || isMoveY)) {
            float[] XY = getTranslateXY(imgMatrix);
            float transX = 0;
            float transY = 0;
            if (isMoveX) {
                endX = event.getRawX();
                transX = endX - startX;
                if ((XY[0] + transX) <= limitX1) {
                    transX = limitX1 - XY[0];
                }
                if ((XY[0] + transX) >= limitX2) {
                    transX = limitX2 - XY[0];
                }
            }
            if (isMoveY) {
                endY = event.getRawY();
                transY = endY - startY;
                if ((XY[1] + transY) <= limitY1) {
                    transY = limitY1 - XY[1];
                }
                if ((XY[1] + transY) >= limitY2) {
                    transY = limitY2 - XY[1];
                }
            }

            imgMatrix.postTranslate(transX, transY);
            startX = endX;
            startY = endY;
            this.setImageMatrix(imgMatrix);
        } else if (mode == ZOOM && event.getPointerCount() > 1) {
            endDistance = getDistance(event);
            float dif = endDistance - startDistance;
            if (Math.abs(endDistance - startDistance) > DOUBLE_POINT_DISTANCE) {
                if (isBig) {
                    if (dif < 0) {
                        changeSize(0, 0);
                        mode = NONE;
                    }
                } else if (dif > 0) {
                    float x = event.getX(0) / 2 + event.getX(1) / 2;
                    float y = event.getY(0) / 2 + event.getY(1) / 2;
                    changeSize(x, y);
                    mode = NONE;
                }
            }
        }
    }

    /**
     * 鼠标抬起事件
     */
    public void mouseUp() {
        mode = NONE;
    }

    /**
     * 图片放大缩小
     *
     * @param x 点击点X坐标
     * @param y 点击点Y坐标
     */
    private void changeSize(float x, float y) {
        if (isBig) {
            // 如果处于最大状态，则还原
            imgMatrix.reset();
            imgMatrix.postScale(scale, scale);
            imgMatrix.postTranslate(subX, subY);
            isBig = false;
        } else {
            imgMatrix.postScale(bigScale, bigScale); // 在原有矩阵后乘放大倍数
            float transX = -((bigScale - 1) * x);
            float transY = -((bigScale - 1) * (y - topHeight)); // (bigScale-1)(y-statusBarHeight-subY)+2*subY;
            float currentWidth = primaryW * scale * bigScale; // 放大后图片大小
            float currentHeight = primaryH * scale * bigScale;
            // 如果图片放大后超出屏幕范围处理
            if (currentHeight > contentH) {
                limitY1 = -(currentHeight - contentH); // 平移限制
                limitY2 = 0;
                isMoveY = true; // 允许在Y轴上拖动
                float currentSubY = bigScale * subY; // 当前平移距离
                // 平移后，内容区域上部有空白处理办法
                if (-transY < currentSubY) {
                    transY = -currentSubY;
                }
                // 平移后，内容区域下部有空白处理办法
                if (currentSubY + transY < limitY1) {
                    transY = -(currentHeight + currentSubY - contentH);
                }
            } else {
                // 如果图片放大后没有超出屏幕范围处理，则不允许拖动
                isMoveY = false;
            }

            if (currentWidth > contentW) {
                limitX1 = -(currentWidth - contentW);
                limitX2 = 0;
                isMoveX = true;
                float currentSubX = bigScale * subX;
                if (-transX < currentSubX) {
                    transX = -currentSubX;
                }
                if (currentSubX + transX < limitX1) {
                    transX = -(currentWidth + currentSubX - contentW);
                }
            } else {
                isMoveX = false;
            }

            imgMatrix.postTranslate(transX, transY);
            isBig = true;
        }

        this.setImageMatrix(imgMatrix);
        if (mCustomMethod != null) {
            mCustomMethod.customMethod(isBig);
        }
    }

    /**
     * 获取变换矩阵中X轴偏移量和Y轴偏移量
     *
     * @param matrix 变换矩阵
     * @return
     */
    private float[] getTranslateXY(Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        float[] floats = new float[2];
        floats[0] = values[Matrix.MTRANS_X];
        floats[1] = values[Matrix.MTRANS_Y];
        return floats;
    }

    /**
     * 获取两点间的距离
     *
     * @param event
     * @return
     */
    private float getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * @author Administrator 用户自定义方法
     */
    public interface ICustomMethod {
        public void customMethod(Boolean currentStatus);
    }
}