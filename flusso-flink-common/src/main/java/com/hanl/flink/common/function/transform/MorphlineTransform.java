package com.hanl.flink.common.function.transform;

import com.alibaba.fastjson.parser.ParserConfig;
import com.codahale.metrics.SharedMetricRegistries;
import com.hanl.flink.common.ConfigParameters;
import com.hanl.flink.Message;
import com.hanl.flink.common.function.Transform;
import com.hanl.flink.exception.TransformException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.FaultTolerance;
import org.kitesdk.morphline.base.Fields;
import org.kitesdk.morphline.base.Notifications;
import org.kitesdk.morphline.base.Compiler;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * @author: Hanl
 * @date :2019/8/30
 * @desc:
 */
@Data
@Slf4j
public abstract class MorphlineTransform<OUT> implements Transform<Message, OUT> {

    private MorphlineContext morphlineContext;

    private Command mainFlow;

    private Collector finalChild;

    private String mainFlowName;

    protected MorphlineTransform(String transformContextName, String mainFlowName, List<Map<String, Object>> morphFlows) {
        morphlineContext = new MorphlineContext.Builder().setExceptionHandler(new FaultTolerance(false, false))
                .setMetricRegistry(SharedMetricRegistries.getOrCreate(transformContextName)).build();
        this.morphlineContext.getSettings().put("parserConfig", new ParserConfig());
        this.mainFlowName = mainFlowName;
        Compiler compiler = new Compiler();
        for (Map<String, Object> flow : morphFlows) {
            Config config = ConfigFactory.parseMap(flow);
            String id = flow.get("id").toString();
            if (mainFlowName.equals(id)) {
                finalChild = new Collector();
                mainFlow = compiler.compile(config, morphlineContext, finalChild);
                log.info("create mainFlow={},flow={}", id, flow);
            } else {
                compiler.compile(config, morphlineContext, null);
                log.info("create flow={},flow={}", id, flow);
            }
        }
    }

    @Override
    public Map<String, Collection<Object>> process(Message message) {
        try {
            Record record = new Record();
            //FIXME 这里目前就是支持String类型的数据
            record.put(Fields.MESSAGE, message.getValue());
            Notifications.notifyStartSession(mainFlow);
            long start = System.currentTimeMillis();
            if (!mainFlow.process(record)) {

                throw new TransformException("Failed to process record,dataType=" + message + ",message=" + message);
            }
            long end = System.currentTimeMillis();
            if (end - start > 20) {
                log.info("Record process took time={}ms", end - start);
            }
            record = finalChild.getRecords().get(0);
            Map<String, Collection<Object>> result = record.getFields().asMap();
            return result;
        } catch (Exception e) {
            throw new TransformException("Failed to transform Exception ", e);
        } finally {
            if (null != finalChild) {
                finalChild.reset();
            }
            if (null != mainFlow) {
                Notifications.notifyShutdown(mainFlow);
            }
        }
    }

    /**
     * FIXME Update or add command
     * 1.实现Command配置更新可以自己去实现配置方式、发布或订阅或者定时任务去获取变化
     * 2.通过Flink的{@BroadcastProcessFunction}的方式去下发变化
     *
     * @param configParameters
     */
    public void updateFlow(ConfigParameters configParameters) {
        Map<String, Object> flowMap = configParameters.getConfig();
        List<Map<String, Object>> morphFlows = (List<Map<String, Object>>) flowMap.get(mainFlowName);
        Compiler compiler = new Compiler();
        for (Map<String, Object> flow : morphFlows) {
            Config config = ConfigFactory.parseMap(flow);
            String id = flow.get("id").toString();
            if (mainFlowName.equals(id)) {
                mainFlow = compiler.compile(config, morphlineContext, finalChild);
            } else {
                compiler.compile(config, morphlineContext, null);
            }
        }
        log.info("Update Flow config success,config={}", configParameters);
    }
}
