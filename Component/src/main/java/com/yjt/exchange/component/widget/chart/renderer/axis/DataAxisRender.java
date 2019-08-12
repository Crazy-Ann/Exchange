package com.hynet.heebit.components.widget.chart.renderer.axis;import android.graphics.Canvas;import com.hynet.heebit.components.widget.chart.constant.OddEven;import com.hynet.heebit.components.widget.chart.utils.MathUtil;public class DataAxisRender extends DataAxis {    //当前刻度线ID    private int id = 0;    public DataAxisRender() {    }    /**     * 返回轴值的范围(即最大-最小值).     *     * @return 轴值范围     */    public float getAxisRange() {        return MathUtil.getInstance().sub(getAxisMax(), getAxisMin());    }    /**     * 数据轴值范围(最大与最小之间的范围)  /  传的的步长  = 显示的Tick总数     *     * @return 显示的刻度标记总数     */    public int getAixTickCount() {        return (int) Math.ceil(getAxisRange() / getAxisSteps());    }    /**     * 设置当前刻度线在轴上的序号ID,注意,此序号与轴的值与关，仅用来说明是轴上的第几个标识     *     * @param id 刻度线ID     */    public void setAxisTickCurrentID(int id) {        this.id = id;    }    /**     * 依据当前id序号与steps的比较来区分当前是否为主tick     *     * @return 是否为主tick     */    public boolean isPrimaryTick() {        return isPrimaryTick(id);    }    public boolean isPrimaryTick(int id) {        if (isDetailMode()) {            if (0 == id && showFirstTick) return true;            if (id < getDetailModeSteps() || id % getDetailModeSteps() != 0)                return false;        }        return true;    }    /**     * 用于处理明细横式下，细分部份的轴刻度线长度缩短为正常的一半，用来突出主明细刻度     *     * @return 刻度线长度     */    @Override    public int getTickMarksLength() {        int len = super.getTickMarksLength();        return (isPrimaryTick() ? len : len / 2);    }    /**     * 用于处理明细模式下，细分部份的标签不显示出来     */    @Override    public boolean isShowAxisLabels() {        return (isPrimaryTick() && super.isShowAxisLabels());    }    /**     * 绘制横向刻度标记     *     * @param chatLeft      图最左边     * @param plotLeft      绘图区最左边     * @param canvas        画布     * @param centerX       中心点X坐标     * @param centerY       中心点Y坐标     * @param text          文本     * @param isTickVisible 是否显示     */    public void renderAxisHorizontalTick(float chatLeft, float plotLeft, Canvas canvas, float centerX, float centerY, String text, boolean isTickVisible) {        renderHorizontalTick(chatLeft, plotLeft, canvas, centerX, centerY, text, centerX, centerY, isTickVisible);    }    /**     * 绘制竖向刻度标记     *     * @param canvas  画布     * @param centerX 中心点X坐标     * @param centerY 中心点Y坐标     * @param text    文本     */    public void renderAxisVerticalTick(Canvas canvas, float centerX, float centerY, String text, boolean isTickVisible, OddEven oddEven) {        renderVerticalTick(canvas, centerX, centerY, text, centerX, centerY, isTickVisible, oddEven);    }    /**     * 绘制轴     *     * @param canvas 画布     * @param startX 起始点X坐标     * @param startY 起始点Y坐标     * @param stopX  结束点X坐标     * @param stopY  结束点Y坐标     */    public void renderAxis(Canvas canvas, float startX, float startY, float stopX, float stopY) {        if (isShow() && isShowAxisLine()) {            drawAxisLine(canvas, startX, startY, stopX, stopY);        }    }    public void renderAxisLine(Canvas canvas, float startX, float startY, float stopX, float stopY) {        drawAxisLine(canvas, startX, startY, stopX, stopY);    }}