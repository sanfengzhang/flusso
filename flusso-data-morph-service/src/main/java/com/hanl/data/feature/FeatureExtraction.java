package com.hanl.data.feature;

import com.hanl.data.exception.FeatureExtractException;

import java.util.Iterator;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc: 定义一个实时数据特征提取接口操作
 * 定义这个接口的目的是希望在流式计算中一些简单、单一的特征提取计算可以实时
 * 的计算出来、可以考虑支持调用Python的一些方法
 */
public interface FeatureExtraction<IN, OUT> {


    public Iterator<OUT> extractFeature(Iterator<IN> input) throws FeatureExtractException;

    public OUT extractFeature(IN input) throws FeatureExtractException;

}
