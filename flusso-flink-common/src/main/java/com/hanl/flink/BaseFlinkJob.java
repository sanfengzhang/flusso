package com.hanl.flink;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hanl.flink.utils.HttpClientUtils;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
public abstract class BaseFlinkJob implements BaseJob {

    public static final String Restart_Attempts = "restartAttempts";

    public static final String DelayBetween_Attempts = "delayBetweenAttempts";

    public static final String Time_Characteristic = "timeCharacteristic";

    public static final String Checkpoint_Interval = "checkpointInterval";

    public static final String Checkpoint_Timeout = "checkpointTimeout";

    public static final String Parallelism = "parallelism";

    protected JobConfigContext jobConfigContext = new JobConfigContext();


    public void initJobConfigContextByHttp(String url) {
        String config = HttpClientUtils.get(url);
        Map<String, Object> configMap = JSON.parseObject(config, new TypeReference<Map<String, Object>>() {
        });
        getJobConfigContext().getJobConfigParams().putAll( (Map<String, Object>)configMap.get("data"));
    }


    public StreamExecutionEnvironment creatStreamExecutionEnvironment() throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // job失败重启的策略
        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(getJobConfigContext().getInt(Restart_Attempts), getJobConfigContext().getInt(DelayBetween_Attempts)));
        String timeCharacteristic = getJobConfigContext().getString(Time_Characteristic);
        if ("EventTime".equals(timeCharacteristic)) {
            env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        } else if ("ProcessingTime".equals(timeCharacteristic)) {
            env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        }

        env.disableOperatorChaining();

        // 设置合理的CP的时间也是需要考量的
        env.getCheckpointConfig().setCheckpointInterval(getJobConfigContext().getLong(Checkpoint_Interval));
        env.getCheckpointConfig().setCheckpointTimeout(getJobConfigContext().getLong(Checkpoint_Timeout));
        env.setParallelism(getJobConfigContext().getInt(Parallelism));
        return env;
    }


    public JobConfigContext getJobConfigContext() {

        return jobConfigContext;
    }

}
