package com.hanl.flink.sample;

import com.hanl.flink.common.AbstractWatermarkGenerator;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.EventTimeTrigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;

/**
 * @author: Hanl
 * @date :2019/11/22
 * @desc:
 */
public class FlinkWatermarkExample {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);
        env.disableOperatorChaining();
        // env.getConfig().setAutoWatermarkInterval(200);这个参数是可以配置的，默认是200在TimeCharacteristic.EventTime条件下

        DataStream<String> sourceStream = env.socketTextStream("127.0.0.1", 8085);
        DataStream<Tuple3<Long, String, Integer>> dataStream = sourceStream.map(new StringToMapFunction());

        testWatermarkGenerator(dataStream);
        env.execute("Windowed Test Example");
    }

    public static void testDefaultWatermark(DataStream<Tuple3<Long, String, Integer>> dataStream) {
        DataStream<Tuple3<Long, String, Integer>> wmDataStream = dataStream.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Tuple3<Long, String, Integer>>() {
            private long currentWatermark = 0L;

            private long lastCurrentWatermarkSysTime;

            @Nullable
            @Override
            public Watermark getCurrentWatermark() {
                long curSysTime = System.currentTimeMillis();
                if (currentWatermark != 0L && curSysTime - lastCurrentWatermarkSysTime > 10000) {
                    currentWatermark += 5000L;
                    return new Watermark(currentWatermark);
                }
                return new Watermark(currentWatermark - 5000L);
            }

            @Override
            public long extractTimestamp(Tuple3<Long, String, Integer> element, long previousElementTimestamp) {
                long timestamp = element.f0;
                currentWatermark = Math.max(timestamp, currentWatermark);
                lastCurrentWatermarkSysTime = System.currentTimeMillis();
                System.out.println("get timestamp is form record= " + currentWatermark);
                return timestamp;
            }
        });
        AllWindowedStream<Tuple3<Long, String, Integer>, TimeWindow> windowedStream = wmDataStream.timeWindowAll(Time.seconds(5)).trigger(EventTimeTrigger.create());
        DataStream<Tuple3<Long, String, Integer>> sumDataStream = windowedStream.process(new ProcessAllWindowFunction<Tuple3<Long, String, Integer>, Tuple3<Long, String, Integer>, TimeWindow>() {
            @Override
            public void process(Context context, Iterable<Tuple3<Long, String, Integer>> elements, Collector<Tuple3<Long, String, Integer>> out) throws Exception {

                System.out.println(elements.toString());
            }
        });
        sumDataStream.print();
    }

    /**
     *1.想清楚为什么会要做水位自动上涨的功能？
     *
     */
    public static void testWatermarkGenerator(DataStream<Tuple3<Long, String, Integer>> dataStream) {
        DataStream<Tuple3<Long, String, Integer>> wmDataStream = dataStream.assignTimestampsAndWatermarks(new AbstractWatermarkGenerator<Tuple3<Long, String, Integer>>() {
            @Override
            protected long extractCurTimestamp(Tuple3<Long, String, Integer> element) throws Exception {

                return element.f0;
            }
        });
        AllWindowedStream<Tuple3<Long, String, Integer>, TimeWindow> windowedStream = wmDataStream.timeWindowAll(Time.seconds(5)).trigger(EventTimeTrigger.create());
        DataStream<Tuple3<Long, String, Integer>> sumDataStream = windowedStream.process(new ProcessAllWindowFunction<Tuple3<Long, String, Integer>, Tuple3<Long, String, Integer>, TimeWindow>() {
            @Override
            public void process(Context context, Iterable<Tuple3<Long, String, Integer>> elements, Collector<Tuple3<Long, String, Integer>> out) throws Exception {

                System.out.println(elements.toString());
            }
        });
        sumDataStream.print();
    }
}
