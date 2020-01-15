package com.hanl.flink.sample.state;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;

/**
 * @author: Hanl
 * @date :2019/11/26
 * @desc:
 */
@Slf4j
public class VarMapStateFunction extends RichMapFunction<Tuple3<Long, String, Integer>, Tuple3<Long, String, Integer>> {

    private transient MapState<String, Integer> state;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        state = this.getRuntimeContext().getMapState(new MapStateDescriptor<String, Integer>("Total consumption", String.class, Integer.class));
    }

    @Override
    public Tuple3<Long, String, Integer> map(Tuple3<Long, String, Integer> value) throws Exception {
        if (null != value) {
            int total = state.get(value.f1) == null ? 0 : state.get(value.f1);
            log.info("Get state={} by name={}", total, value.f1);
            total += value.f2;
            state.put(value.f1, total);
        }
        return value;
    }

}
