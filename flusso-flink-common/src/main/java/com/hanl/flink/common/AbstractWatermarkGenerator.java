package com.hanl.flink.common;

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/11/25
 * @desc:
 */
public abstract class AbstractWatermarkGenerator<T> implements AssignerWithPeriodicWatermarks<T> {

    private static final long serialVersionUID = -2006930231735705083L;

    private static final Logger logger = LoggerFactory.getLogger(AbstractWatermarkGenerator.class);

    private final long maxOutOfOrderness; // 10 seconds

    private long windowSize;

    private long currentMaxTimestamp;

    private long lastTimestamp = 0;

    private long lastWatermarkChangeTime = 0;

    private long windowPurgeTime = 0;

    public AbstractWatermarkGenerator() {
        this(5000, 5000);
    }


    public AbstractWatermarkGenerator(long maxOutOfOrderness, long windowSize) {
        this.maxOutOfOrderness = maxOutOfOrderness;
        this.windowSize = windowSize;
    }

    protected abstract long extractCurTimestamp(T element) throws Exception;

    public long extractTimestamp(T element, long previousElementTimestamp) {
        try {
            long curTimestamp = extractCurTimestamp(element);//这个是数据的EventTime,Flink也会按照这个时间进行窗口的计算
            lastWatermarkChangeTime = new Date().getTime();//这个是系统时间
            currentMaxTimestamp = Math.max(curTimestamp, currentMaxTimestamp);//将该数据流这段时间内的最大数据时间（因为数据会乱序，所以需要独立保存最大时间）
            windowPurgeTime = Math.max(windowPurgeTime, getWindowExpireTime(currentMaxTimestamp));//这行代码，是针对已处理数据中最大时间戳那条数据所在窗口的清除或过期时间
            if (logger.isDebugEnabled()) {
                logger.debug("Extracting timestamp: {}", currentMaxTimestamp);
            }
            return curTimestamp;
        } catch (Exception e) {
            logger.error("Error extracting timestamp", e);
        }

        return 0;
    }

    protected long getWindowExpireTime(long currentMaxTimestamp) {
        long windowStart = TimeWindow.getWindowStartWithOffset(currentMaxTimestamp, 0, windowSize);
        long windowEnd = windowStart + windowSize;
        return windowEnd + maxOutOfOrderness;
    }

    public Watermark getCurrentWatermark() {
        long curTime = new Date().getTime();
        if (currentMaxTimestamp > lastTimestamp) {
            if (logger.isDebugEnabled()) {
                logger.debug("Current max timestamp has been increased since last");
            }
            lastTimestamp = currentMaxTimestamp;
            lastWatermarkChangeTime = curTime;
        } else {
            long diff = windowPurgeTime - currentMaxTimestamp;
            //我们最终判断需要涨水位的时机是？当前系统时间-最后一次wm改变时间>?==窗口过期时间-当前最大的事件时间？
            if (diff > 0 && curTime - lastWatermarkChangeTime > diff) {
                logger.info("Increase current MaxTimestamp once");
                currentMaxTimestamp = windowPurgeTime;
                lastTimestamp = currentMaxTimestamp;
                lastWatermarkChangeTime = curTime;
            }
        }

        return new Watermark(currentMaxTimestamp - maxOutOfOrderness);
    }
}