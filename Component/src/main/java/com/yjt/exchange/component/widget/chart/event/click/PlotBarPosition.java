package com.hynet.heebit.components.widget.chart.event.click;

import android.graphics.RectF;

public class PlotBarPosition extends BarPosition {

    public PlotBarPosition() {
    }

    //当前记录在数据源中行号
    public void savePlotDataID(int num) {
        saveDataID(num);
    }

    //当前记录所属数据集的行号
    public void savePlotDataChildID(int num) {
        saveDataChildID(num);
    }

    public void savePlotRectF(float left, float top, float right, float bottom) {
        saveRectF(left, top, right, bottom);
    }

    public void savePlotRectF(RectF r) {
        saveRectF(r);
    }

    public boolean compareF(float x, float y) {
        return compareRange(x, y);
    }
}
