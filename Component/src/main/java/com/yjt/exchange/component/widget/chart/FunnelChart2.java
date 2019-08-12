/** * Copyright 2014  XCL-Charts * <p> * Licensed under the Apache License, Version 2.0 (the "License"); * you may not use this file except in compliance with the License. * You may obtain a copy of the License at * <p> * http://www.apache.org/licenses/LICENSE-2.0 * <p> * Unless required by applicable law or agreed to in writing, software * distributed under the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. * See the License for the specific language governing permissions and * limitations under the License. * * @Project XCL-Charts * @Description Android图表基类库 * @author XiongChuanLiang<br               />(xcl_168@aliyun.com) * @license http://www.apache.org/licenses/  Apache v2 License * @version 3.2 */package com.hynet.heebit.components.widget.chart;import android.graphics.Canvas;import android.graphics.Color;import android.graphics.Paint;import android.graphics.Paint.Style;import android.graphics.Path;import android.graphics.PointF;import com.hynet.heebit.components.widget.chart.constant.ChartType;import com.hynet.heebit.components.widget.chart.renderer.EventChart;import com.hynet.heebit.components.widget.chart.utils.PointUtil;import java.util.ArrayList;import java.util.List;public class FunnelChart2 extends EventChart {    private Paint paint = null;    private Paint labelPaint = null;    private List<Funnel2Data> funnel2Data;    private int backgroundColor = Color.WHITE;    public FunnelChart2() {    }    @Override    public ChartType getType() {        return ChartType.FUNNEL;    }    /**     * 返回图的数据源     *     * @return 数据源     */    public List<Funnel2Data> getDataSource() {        return funnel2Data;    }    /**     * 设置数据源     *     * @param dataSet 数据集     */    public void setDataSource(List<Funnel2Data> dataSet) {        funnel2Data = dataSet;    }    public Paint getPaint() {        if (null == paint) paint = new Paint(Paint.ANTI_ALIAS_FLAG);        return paint;    }    public Paint getPaintLabel() {        if (null == labelPaint) {            labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);            labelPaint.setColor(Color.BLACK);        }        return labelPaint;    }    public void setBgColor(int color) {        backgroundColor = color;    }    protected void renderPlot(Canvas canvas) {        if (funnel2Data.size() == 0) return;        float fHalfTopWidth = div(plotAreaRender.getPlotWidth(), 5);        float funnelTopWidth = mul(fHalfTopWidth, 2);        float funnelTopStartX = sub(plotAreaRender.getCenterX(), fHalfTopWidth);        float funnelTopStopX = add(plotAreaRender.getCenterX(), fHalfTopWidth);        //打底        Path cPath = new Path();        cPath.moveTo(plotAreaRender.getLeft(), plotAreaRender.getBottom());        cPath.lineTo(plotAreaRender.getRight(), plotAreaRender.getBottom());        cPath.lineTo(funnelTopStopX, plotAreaRender.getTop());        cPath.lineTo(funnelTopStartX, plotAreaRender.getTop());        cPath.close();        getPaint().setStyle(Style.FILL);        getPaint().setColor(funnel2Data.get(0).getColor());        canvas.drawPath(cPath, getPaint());        List<PointF> lstTop = new ArrayList<>();        List<PointF> lstCenter = new ArrayList<>();        List<PointF> lstRight = new ArrayList<>();        int dataSize = funnel2Data.size();        float fx = funnelTopStartX; //sub(plotAreaRender.getCenterX() , fHalfTopWidth );        float fy = plotAreaRender.getTop();        // 依chartPercentData 得到上面的线的比例        for (int i = 0; i < dataSize; i++) {            fx = add(fx, mul(funnelTopWidth, funnel2Data.get(i).getPercentData()));            lstTop.add(new PointF(fx, fy));        }        // 中间控制点位置        fy = plotAreaRender.getBottom();        float fSpaddWidth = div(sub(plotAreaRender.getPlotWidth(), funnelTopWidth), 2);        float rx, leftX, rightX, cx;        for (int i = 0; i < dataSize; i++) {            fy = sub(fy, mul(plotAreaRender.getPlotHeight(), funnel2Data.get(i).getBaseData()));            rx = mul(fSpaddWidth, sub(1, div(fy, plotAreaRender.getPlotHeight())));            leftX = plotAreaRender.getLeft() + rx;            rightX = plotAreaRender.getRight() - rx;            cx = leftX + ((rightX - leftX) * funnel2Data.get(i).getPercentData());            lstCenter.add(new PointF(cx, fy));        }        // 右边结束位置及标签位置        List<Float> lstLabel = new ArrayList<>();        float labelY = 0.f;        float labelX = add(funnelTopStopX, div(fSpaddWidth, 2));        fy = plotAreaRender.getBottom();        for (int i = 0; i < dataSize; i++) {            fy = sub(fy, mul(plotAreaRender.getPlotHeight(), funnel2Data.get(i).getPercentData()));            lstRight.add(new PointF(plotAreaRender.getRight(), fy));            if (i == 0) {                lstLabel.add(sub(plotAreaRender.getBottom(), div(sub(plotAreaRender.getBottom(), fy), 2)));            } else {                labelY = div(sub(fy, lstRight.get(i - 1).y), 2);                labelY = sub(fy, labelY);                lstLabel.add(labelY);            }        }        //填充区域        Path bezierPath = new Path();        for (int i = 0; i < lstTop.size(); i++) {            if (i + 1 >= lstTop.size()) continue;            getPaint().setColor(funnel2Data.get(i + 1).getColor());            bezierPath.moveTo(lstTop.get(i).x, lstTop.get(i).y);            PointF ctl2 = PointUtil.percent(lstTop.get(i), 0.7f, lstCenter.get(i), 0.5f);            bezierPath.quadTo(ctl2.x, ctl2.y, lstCenter.get(i).x, lstCenter.get(i).y);            PointF ctl3 = PointUtil.percent(lstCenter.get(i), 0.1f, lstRight.get(i), 0.9f);            bezierPath.quadTo(ctl3.x, ctl3.y, lstRight.get(i).x, lstRight.get(i).y);            bezierPath.lineTo(funnelTopStopX, plotAreaRender.getTop());            bezierPath.close();            canvas.drawPath(bezierPath, getPaint());            bezierPath.reset();        }        // 盖掉多出的区域.(本来有更好的算位置的方法，不小心删了，没心情再搞了)        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));        cPath.reset();        cPath.moveTo(plotAreaRender.getRight(), plotAreaRender.getBottom());        cPath.lineTo(plotAreaRender.getRight(), plotAreaRender.getTop());        cPath.lineTo(funnelTopStopX, plotAreaRender.getTop());        cPath.close();        getPaint().setColor(backgroundColor);        canvas.drawPath(cPath, getPaint());        cPath.reset();        cPath.moveTo(plotAreaRender.getLeft(), plotAreaRender.getTop());        cPath.lineTo(funnelTopStartX, plotAreaRender.getTop());        cPath.lineTo(plotAreaRender.getLeft(), plotAreaRender.getBottom());        cPath.close();        canvas.drawPath(cPath, getPaint());        //画上标签        getPaintLabel();        for (int i = 0; i < lstLabel.size(); i++) {            try {                canvas.drawText(funnel2Data.get(i).getLabel(), labelX, lstLabel.get(i), labelPaint);            } catch (Exception ex) {            }        }    }    @Override    protected boolean postRender(Canvas canvas) {        super.postRender(canvas);        //计算主图表区范围        calcPlotRange();        canvas.drawColor(backgroundColor);        //画Plot Area背景        plotAreaRender.render(canvas);        //绘制标题        renderTitle(canvas);        //绘制图表        renderPlot(canvas);        //显示焦点        renderFocusShape(canvas);        //响应提示        renderToolTip(canvas);        return true;    }}