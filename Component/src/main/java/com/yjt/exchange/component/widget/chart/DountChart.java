package com.hynet.heebit.components.widget.chart;import android.graphics.Canvas;import android.graphics.Color;import android.graphics.Paint;import android.graphics.Paint.Align;import android.graphics.PointF;import android.text.TextUtils;import com.hynet.heebit.components.widget.chart.constant.ChartType;import com.hynet.heebit.components.widget.chart.constant.SliceLabelStyle;import com.hynet.heebit.components.widget.chart.renderer.plot.PlotAttrInfo;import com.hynet.heebit.components.widget.chart.renderer.plot.PlotAttrInfoRender;import com.hynet.heebit.components.widget.chart.utils.DrawUtil;import com.hynet.heebit.components.widget.chart.utils.MathUtil;public class DountChart extends PieChart {    //内环半径    private int fillRadius = 0;    private float innerRadius = 0.8f;    //内环填充颜色    private Paint fillPaint = null;    private Paint centerTextPaint = null;    private String centerText;    //附加信息类    private PlotAttrInfoRender plotAttrInfoRender = null;    public DountChart() {        initChart();    }    @Override    public ChartType getType() {        return ChartType.DOUNT;    }    private void initChart() {        int fillColor = Color.BLACK;        if (null != plotAreaRender)            fillColor = plotAreaRender.getBackgroundPaint().getColor();        if (null == fillPaint) {            fillPaint = new Paint();            fillPaint.setColor(fillColor);            fillPaint.setAntiAlias(true);        }        if (null == plotAttrInfoRender)            plotAttrInfoRender = new PlotAttrInfoRender();        this.setLabelStyle(SliceLabelStyle.OUTSIDE);    }    private void initCenterTextPaint() {        if (null == centerTextPaint) {            centerTextPaint = new Paint();            centerTextPaint.setAntiAlias(true);            centerTextPaint.setTextSize(28);            centerTextPaint.setTextAlign(Align.CENTER);        }    }    /**     * 附加信息绘制处理类     *     * @return 信息基类     */    public PlotAttrInfo getPlotAttrInfo() {        return plotAttrInfoRender;    }    /**     * 环内部填充画笔     *     * @return 画笔     */    public Paint getInnerPaint() {        return fillPaint;    }    /**     * 设置环内部填充相对于环所占的比例     *     * @param precentage 环所占比例     */    public void setInnerRadius(float precentage) {        innerRadius = precentage;    }    /**     * 计算出环内部填充圆的半径     *     * @return 环的半径     */    public float calcInnerRadius() {        return (int) MathUtil.getInstance().round(mul(getRadius(), innerRadius), 2);    }    /**     * 开放绘制中心文字的画笔     *     * @return 画笔     */    public Paint getCenterTextPaint() {        initCenterTextPaint();        return centerTextPaint;    }    /**     * 设置中心点文字     *     * @param text 文字     */    public void setCenterText(String text) {        centerText = text;    }    /**     * 绘制中心点     *     * @param canvas 画布     */    private void renderCenterText(Canvas canvas) {        if (centerText.length() > 0) {            if (centerText.indexOf("\n") > 0) {                float textY = plotAreaRender.getCenterY();                float textHeight = DrawUtil.getInstance().getPaintFontHeight(getCenterTextPaint());                String[] arr = centerText.split("\n");                for (int i = 0; i < arr.length; i++) {                    canvas.drawText(arr[i], plotAreaRender.getCenterX(), textY, getCenterTextPaint());                    textY += textHeight;                }            } else {                canvas.drawText(centerText, plotAreaRender.getCenterX(), plotAreaRender.getCenterY(), getCenterTextPaint());            }        }    }    @Override    protected PointF renderLabelInside(Canvas canvas, String text, float itemAngle, float cirX, float cirY, float radius, float calcAngle, boolean showLabel) {        if (TextUtils.isEmpty(text)) return null;        //显示在扇形的中心        float calcRadius = fillRadius + (radius - fillRadius) / 2;        //计算百分比标签        PointF point = MathUtil.getInstance().calcArcEndPointXY(cirX, cirY, calcRadius, calcAngle);        //标识        if (showLabel)            DrawUtil.getInstance().drawRotateText(text, point.x, point.y, itemAngle, canvas, getLabelPaint());        return (new PointF(point.x, point.y));    }    protected void renderInnderCircle(Canvas canvas) {        //中心点坐标        float cirX = plotAreaRender.getCenterX();        float cirY = plotAreaRender.getCenterY();        canvas.drawCircle(cirX, cirY, fillRadius, fillPaint);        //边框线        if (null != arcBorderPaint) {            canvas.drawCircle(cirX, cirY, fillRadius, arcBorderPaint);        }    }    protected void renderDount(Canvas canvas) {        //内部        renderInnderCircle(canvas);        //绘制附加信息        plotAttrInfoRender.renderAttrInfo(canvas, plotAreaRender.getCenterX(), plotAreaRender.getCenterY(), this.getRadius());        //中心文本        renderCenterText(canvas);    }    /**     * 绘制图 -- 环形图的标签处理待改进 ***     */    @Override    protected boolean renderPlot(Canvas canvas) {        calcInnerRadius();        super.renderPlot(canvas);        renderDount(canvas);        return true;    }}