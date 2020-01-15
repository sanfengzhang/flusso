package com.hanl.flink.sample.state;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

/**
 * @author: Hanl
 * @date :2019/12/10
 * @desc:
 */
@Slf4j
public class TwoPhaseVarMapStatFunction extends RichMapFunction<Tuple2<String, Integer>, Tuple2<String, Integer>> {

    private transient MapState<String, Integer> state;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        state = this.getRuntimeContext().getMapState(new MapStateDescriptor<String, Integer>("Total consumption", String.class, Integer.class));
    }

    @Override
    public Tuple2<String, Integer> map(Tuple2<String, Integer> value) throws Exception {
        if (null != value) {
            int total = state.get(value.f0) == null ? 0 : state.get(value.f0);
            log.info("Get state={} by name={}", total, value.f0);
            total += value.f1;
            state.put(value.f0, total);
        }
        return value;
    }
}
