package com.hynet.heebit.components.widget.chart;import android.graphics.Color;import java.util.LinkedList;import java.util.List;public class BarData {    //线上每个点的值    private List<Double> doubles;    //用于为每个柱形定制颜色    private List<Integer> integers;    //值    private String key;    //线上的颜色    private Integer color;    public BarData() {    }    /**     * 构成一条完整的数据集合     *     * @param key        键值     * @param color      颜色     * @param dataSeries 对应的数据集     */    public BarData(String key, List<Double> dataSeries, Integer color) {        setKey(key);        setColor(color);        setDataSet(dataSeries);    }    /**     * 构成一条完整的数据集合     *     * @param key        键值     * @param dataSeries 对应的数据集     */    public BarData(String key, Double dataSeries) {        setKey(key);        List<Double> valueList = new LinkedList<>();        valueList.add(dataSeries);        setDataSet(valueList);        setColor(Color.BLACK);    }    /**     * 可用于处理单独针对某些柱子指定颜色的情况，常见于标签单柱的情况     *     * @param key        键值     * @param dataSeries 对应的数据集     * @param dataColor  每个数据柱形所对应的显示颜色     * @param color      柱形颜色     */    public BarData(String key, List<Double> dataSeries, List<Integer> dataColor, Integer color) {        setKey(key);        setColor(color);        setDataSet(dataSeries);        setDataColor(dataColor);    }    /**     * 设置每个数据柱形所对应的显示颜色     *     * @param dataColor 柱形颜色集     */    public void setDataColor(List<Integer> dataColor) {        if (null != integers) integers.clear();        integers = dataColor;    }    /**     * 每个数据柱形所对应的显示颜色     *     * @return 柱形颜色集     */    public List<Integer> getDataColor() {        return integers;    }    /**     * 设置数据源     *     * @param dataSeries 数据集合序列     */    public void setDataSet(List<Double> dataSeries) {        if (null != doubles) doubles.clear();        doubles = dataSeries;    }    /**     * 设置Key值     *     * @param value Key值     */    public void setKey(String value) {        key = value;    }    /**     * 设置颜色     *     * @param value 颜色     */    public void setColor(Integer value) {        color = value;    }    /**     * 返回数据集合序列     *     * @return 集合序列     */    public List<Double> getDataSet() {        return doubles;    }    /**     * 返回Key值     *     * @return Key值     */    public String getKey() {        return key;    }    /**     * 返回颜色     *     * @return 颜色     */    public Integer getColor() {        return color;    }}