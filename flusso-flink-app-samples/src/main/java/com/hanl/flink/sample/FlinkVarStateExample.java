package com.hanl.flink.sample;

import com.hanl.flink.sample.state.VarMapStateFunction;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.apache.flink.runtime.minicluster.MiniCluster;
import org.apache.flink.runtime.minicluster.MiniClusterConfiguration;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.graph.StreamGraph;

/**
 * @author: Hanl
 * @date :2019/11/26
 * @desc:
 */
public class FlinkVarStateExample {

    public static void main(String[] args) throws Exception {
        final Configuration config = new Configuration();

        StreamExecutionEnvironment env = new StreamExecutionEnvironment() {
            @Override
            public JobExecutionResult execute(String jobName) throws Exception {
                // transform the streaming program into a JobGraph
                StreamGraph streamGraph = getStreamGraph();
                streamGraph.setJobName(jobName);

                JobGraph jobGraph = streamGraph.getJobGraph();
                jobGraph.setAllowQueuedScheduling(true);
                //在本地IDE进行state状态从checkPoint恢复的案例，可以自己按照{@ LocalStreamEnvironment}的实现，加上这一行代码即可。
                //暂时没有找到其他的配置方法,囧
                jobGraph.setSavepointRestoreSettings(SavepointRestoreSettings.forPath("file:///D:/checkpoint/9ec8b02a4f2f89bd9be1d5d6af64e34a/chk-7", true));

                Configuration configuration = new Configuration();
                configuration.addAll(jobGraph.getJobConfiguration());
                configuration.setString(TaskManagerOptions.MANAGED_MEMORY_SIZE, "0");

                // add (and override) the settings with what the user defined
                configuration.addAll(config);

                if (!configuration.contains(RestOptions.BIND_PORT)) {
                    configuration.setString(RestOptions.BIND_PORT, "0");
                }

                int numSlotsPerTaskManager = configuration.getInteger(TaskManagerOptions.NUM_TASK_SLOTS, jobGraph.getMaximumParallelism());

                MiniClusterConfiguration cfg = new MiniClusterConfiguration.Builder()
                        .setConfiguration(configuration)
                        .setNumSlotsPerTaskManager(numSlotsPerTaskManager)
                        .build();


                MiniCluster miniCluster = new MiniCluster(cfg);

                try {
                    miniCluster.start();
                    configuration.setInteger(RestOptions.PORT, miniCluster.getRestAddress().get().getPort());

                    return miniCluster.executeJobBlocking(jobGraph);
                } finally {
                    transformations.clear();
                    miniCluster.close();
                }
            }

            @Override
            public JobExecutionResult execute(StreamGraph streamGraph) throws Exception {
                return null;
            }
        };
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);
        env.getCheckpointConfig().setCheckpointInterval(5000L);
        StateBackend stateBackend = new FsStateBackend("file:///D:/checkpoint");
        env.setStateBackend(stateBackend);
        env.disableOperatorChaining();

        DataStream<String> sourceStream = env.socketTextStream("127.0.0.1", 8085);
        DataStream<Tuple3<Long, String, Integer>> dataStream = sourceStream.map(new StringToMapFunction());
        testMapState(dataStream);

        env.execute("Var State Test Example");
    }

    public static void testMapState(DataStream<Tuple3<Long, String, Integer>> dataStream) {
        dataStream.keyBy(1).map(new VarMapStateFunction()).print();
    }
}
