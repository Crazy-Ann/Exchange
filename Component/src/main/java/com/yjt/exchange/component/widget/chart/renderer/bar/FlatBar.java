package com.hynet.heebit.components.widget.chart.renderer.bar;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;

import com.hynet.heebit.components.utils.LogUtil;
import com.hynet.heebit.components.widget.chart.constant.BarStyle;
import com.hynet.heebit.components.widget.chart.utils.DrawUtil;

public class FlatBar extends Bar {

    //柱形填充色透明度
    private int fillAlpha = 255;
    private LinearGradient linearGradient = null;
    private Path path = null;
    private final int radius = 5; //角半径

    public FlatBar() {
    }

    /**
     * 返回填充透明度
     *
     * @return 透明度
     */
    public int getFillAlpha() {
        return fillAlpha;
    }

    /**
     * 设置填充透明度
     *
     * @param alpha 透明度
     */
    public void setFillAlpha(int alpha) {
        this.fillAlpha = alpha;
    }

    /**
     * 计算同标签多柱形时的Y分隔
     *
     * @param YSteps    Y轴步长
     * @param barNumber 柱形个数
     *
     * @return 返回单个柱形的高度及间距
     */
    public float[] getBarHeightAndMargin(float YSteps, int barNumber) {
        return calcBarHeightAndMargin(YSteps, barNumber);
    }

    /**
     * 计算同标签多柱形时的X分隔
     *
     * @param XSteps    X轴步长
     * @param barNumber 柱形个数
     *
     * @return 返回单个柱形的宽度及间距
     */
    public float[] getBarWidthAndMargin(float XSteps, int barNumber) {
        return calcBarWidthAndMargin(XSteps, barNumber);
    }

    /**
     * 绘制柱形渲染效果
     *
     * @param left   左边X坐标
     * @param top    顶部Y坐标
     * @param right  右边X坐标
     * @param bottom 底部Y坐标
     */
    private void setBarGradient(float left, float top, float right, float bottom) {
        int barColor = getBarPaint().getColor();
        int lightColor = DrawUtil.getInstance().getLightColor(barColor, 150);
        float width = Math.abs(right - left);
        float height = Math.abs(bottom - top);
        Shader.TileMode tm = Shader.TileMode.MIRROR;
        //横向柱形
        if (width > height) {
            linearGradient = new LinearGradient(right, bottom, right, top, new int[]{lightColor, barColor}, null, tm);
        } else {
            linearGradient = new LinearGradient(left, bottom, right, bottom, new int[]{lightColor, barColor}, null, tm);
        }
        getBarPaint().setShader(linearGradient);
    }


    /**
     * 绘制柱形
     *
     * @param left   左边X坐标
     * @param top    顶部Y坐标
     * @param right  右边X坐标
     * @param bottom 底部Y坐标
     * @param canvas 画布
     */
    public boolean renderBar(float left, float top, float right, float bottom, Canvas canvas) {
        BarStyle style = getBarStyle();
        if (Float.compare(top, bottom) == 0) return true;
        if (BarStyle.ROUNDBAR == style) {
            canvas.drawRoundRect(new RectF(left, bottom, right, top), getBarRoundRadius(), getBarRoundRadius(), getBarPaint());
            return true;
        }
        if (null == path) path = new Path();
        if (BarStyle.OUTLINE == style) {
            int barColor = getBarPaint().getColor();
            int lightColor = DrawUtil.getInstance().getLightColor(barColor, outlineAlpha);
            getBarOutlinePaint().setColor(lightColor);
            canvas.drawRect(left, bottom, right, top, getBarOutlinePaint());
            getBarPaint().setStyle(Style.STROKE);
            //getBarPaint().setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            getBarPaint().setStrokeWidth(borderWidth);
            drawPathBar(left, top, right, bottom, canvas);
            getBarPaint().setStrokeWidth(borderWidth); //pWidth);
            return true;
        } else if (BarStyle.TRIANGLE == style) {
            float mid = 0.0f;
            switch (this.getBarDirection()) {
                case HORIZONTAL:
                    mid = top + (bottom - top) / 2;
                    path.moveTo(left, top);
                    path.lineTo(right, mid);
                    path.lineTo(left, bottom);
                    path.close();
                    canvas.drawPath(path, getBarPaint());
                    canvas.drawCircle(right, mid, radius, getBarPaint());
                    break;
                default:
                    mid = left + (right - left) / 2;
                    path.moveTo(left, bottom);
                    path.lineTo(mid, top);
                    path.lineTo(right, bottom);
                    path.close();
                    canvas.drawPath(path, getBarPaint());
                    canvas.drawCircle(mid, top, radius, getBarPaint());
                    break;
            }
            path.reset();
            return true;
        } else {
            //GRADIENT,FILL,STROKE
            switch (style) {
                case GRADIENT:
                    setBarGradient(left, top, right, bottom);
                    break;
                case FILL:
                    getBarPaint().setStyle(Style.FILL);
                    break;
                case STROKE:
                    if (Float.compare(1f, getBarPaint().getStrokeWidth()) == 0)
                        getBarPaint().setStrokeWidth(3);
                    getBarPaint().setStyle(Style.STROKE);
                    break;
                case TRIANGLE:
                case OUTLINE:
                    break;
                default:
                     LogUtil.Companion.getInstance().print("不认识的柱形风格参数.");
                    return false;
            }
            if (getBarStyle() != BarStyle.FILL) {
                setBarGradient(left, top, right, bottom);
            }
            drawPathBar(left, top, right, bottom, canvas);
        }
        return true;
    }

    /**
     * 绘制柱形标签
     *
     * @param text   文本内容
     * @param x      x坐标
     * @param y      y坐标
     * @param canvas 画布
     */
    public void renderBarItemLabel(String text, float x, float y, Canvas canvas) {
        drawBarItemLabel(text, x, y, canvas);
    }

    private void drawPathBar(float left, float top, float right, float bottom, Canvas canvas) {
        path.moveTo(left, bottom);
        path.lineTo(left, top);
        path.lineTo(right, top);
        path.lineTo(right, bottom);
        path.close();
        canvas.drawPath(path, getBarPaint());
        path.reset();
    }

}
