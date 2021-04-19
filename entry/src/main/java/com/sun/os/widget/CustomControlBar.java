package com.sun.os.widget;

import com.sun.os.ResourceTable;
import com.sun.os.tool.Utils;
import ohos.agp.components.Component;
import ohos.agp.render.Arc;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

public class CustomControlBar extends Component implements Component.DrawTask,
        Component.EstimateSizeListener, Component.TouchEventListener {
    private final static float CIRCLE_ANGLE = 360.0f;

    private final static int DEF_UNFILL_COLOR = 0xFF808080;

    private final static int DEF_FILL_COLOR = 0xFF1E90FF;

    // 圆环轨道颜色
    private Color unFillColor;

    // 圆环覆盖颜色
    private Color fillColor;

    // 圆环宽度
    private int circleWidth;

    // 画笔
    private Paint paint;

    // 个数
    private int count;

    // 当前进度
    private int currentCount;

    // 间隙值
    private int splitSize;

    // 内圆的正切方形
    private RectFloat centerRectFloat;

    // 中心绘制的图片
    private PixelMap image;

    // 原点坐标
    private Point centerPoint;

    // 进度改变的事件响应
    private ProgressChangeListener listener;

    public CustomControlBar(Context context) {
        super(context);
        paint = new Paint();
        initData();
        setEstimateSizeListener(this);
        setTouchEventListener(this);
        addDrawTask(this);
    }

    // 初始化属性值
    private void initData() {
        unFillColor = new Color(DEF_UNFILL_COLOR);
        fillColor = new Color(DEF_FILL_COLOR);
        count = 10;
        currentCount = 2;
        splitSize = 15;
        circleWidth = 60;
        centerRectFloat = new RectFloat();
        image = Utils.createPixelMapByResId(ResourceTable.Media_icon, getContext()).get();
        listener = null;
    }

    @Override
    public boolean onEstimateSize(int widthEstimateConfig, int heightEstimateConfig) {
        int width = Component.EstimateSpec.getSize(widthEstimateConfig);
        int height = Component.EstimateSpec.getSize(heightEstimateConfig);
        setEstimatedSize(
                Component.EstimateSpec.getChildSizeWithMode(width, width, Component.EstimateSpec.PRECISE),
                Component.EstimateSpec.getChildSizeWithMode(height, height, Component.EstimateSpec.PRECISE)
        );
        return true;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(circleWidth);
        paint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);
        paint.setStyle(Paint.Style.STROKE_STYLE);

        int width = getWidth();
        int center = width / 2;
        centerPoint = new Point(center, center);
        int radius = center - circleWidth / 2;
        drawCount(canvas, center, radius);

        int inRadius = center - circleWidth;
        double length = inRadius - Math.sqrt(2) * 1.0f / 2 * inRadius;
        centerRectFloat.left = (float) (length + circleWidth);
        centerRectFloat.top = (float) (length + circleWidth);
        centerRectFloat.bottom = (float) (centerRectFloat.left + Math.sqrt(2) * inRadius);
        centerRectFloat.right = (float) (centerRectFloat.left + Math.sqrt(2) * inRadius);

        // 如果图片比较小，那么根据图片的尺寸放置到正中心
        Size imageSize = image.getImageInfo().size;
        if (imageSize.width < Math.sqrt(2) * inRadius) {
            centerRectFloat.left = (float) (centerRectFloat.left + Math.sqrt(2) * inRadius * 1.0f / 2 - imageSize.width * 1.0f / 2);
            centerRectFloat.top = (float) (centerRectFloat.top + Math.sqrt(2) * inRadius * 1.0f / 2 - imageSize.height * 1.0f / 2);
            centerRectFloat.right = centerRectFloat.left + imageSize.width;
            centerRectFloat.bottom = centerRectFloat.top + imageSize.height;
        }
        canvas.drawPixelMapHolderRect(new PixelMapHolder(image), centerRectFloat, paint);
    }

    private void drawCount(Canvas canvas, int centre, int radius) {
        float itemSize = (CIRCLE_ANGLE - count * splitSize) / count;

        RectFloat oval = new RectFloat(centre - radius, centre - radius, centre + radius, centre + radius);

        paint.setColor(unFillColor);
        for (int i = 0; i < count; i++) {
            Arc arc = new Arc((i * (itemSize + splitSize)) - 90, itemSize, false);
            canvas.drawArc(oval, arc, paint);
        }

        paint.setColor(fillColor);
        for (int i = 0; i < currentCount; i++) {
            Arc arc = new Arc((i * (itemSize + splitSize)) - 90, itemSize, false);
            canvas.drawArc(oval, arc, paint);
        }
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
            case TouchEvent.POINT_MOVE: {
                this.getContentPositionX();
                MmiPoint absPoint = touchEvent.getPointerPosition(touchEvent.getIndex());
                Point point = new Point(absPoint.getX() - getContentPositionX(),
                        absPoint.getY() - getContentPositionY());
                double angle = calcRotationAngleInDegrees(centerPoint, point);
                double multiple = angle / (CIRCLE_ANGLE / count);
                if ((multiple - (int) multiple) > 0.4) {
                    currentCount = (int) multiple + 1;
                } else {
                    currentCount = (int) multiple;
                }
                if (listener != null) {
                    listener.onProgressChangeListener(currentCount);
                }
                invalidate();
                break;
            }
        }
        return false;
    }

    public interface ProgressChangeListener {
        void onProgressChangeListener(int Progress);
    }

    // 计算centerPt到targetPt的夹角，单位为度。返回范围为[0, 360)，顺时针旋转。
    private double calcRotationAngleInDegrees(Point centerPt, Point targetPt) {
        double theta = Math.atan2(targetPt.getPointY()
                - centerPt.getPointY(), targetPt.getPointX()
                - centerPt.getPointX());
        theta += Math.PI / 2.0;
        double angle = Math.toDegrees(theta);
        if (angle < 0) {
            angle += CIRCLE_ANGLE;
        }
        return angle;
    }

    public Color getUnFillColor() {
        return unFillColor;
    }

    public CustomControlBar setUnFillColor(Color unFillColor) {
        this.unFillColor = unFillColor;
        return this;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public CustomControlBar setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public int getCircleWidth() {
        return circleWidth;
    }

    public CustomControlBar setCircleWidth(int circleWidth) {
        this.circleWidth = circleWidth;
        return this;
    }

    public int getCount() {
        return count;
    }

    public CustomControlBar setCount(int count) {
        this.count = count;
        return this;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public CustomControlBar setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
        return this;
    }

    public int getSplitSize() {
        return splitSize;
    }

    public CustomControlBar setSplitSize(int splitSize) {
        this.splitSize = splitSize;
        return this;
    }

    public PixelMap getImage() {
        return image;
    }

    public CustomControlBar setImage(PixelMap image) {
        this.image = image;
        return this;
    }

    public void build() {
        invalidate();
    }

    public void setProgressChangerListener(ProgressChangeListener listener) {
        this.listener = listener;
    }
}
