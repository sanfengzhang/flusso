package com.hanl.flink.common.function;


import com.hanl.flink.Message;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @author: Hanl
 * @date :2019/9/2
 * @desc:
 * 原始消息可能会包含一些子消息的功能，那么ETL的流程：
 * message--->MultipleMessages--Translate-->message,所以这一部分没有好办法集成到
 * Command解析中去，因为这里涉及到一条数据衍生成多条数据的情况，这个功能也不太适合
 * 集成到Command里面。所以就单独起一个function去处理这种情况
 */
public class ResolveMessageMultipleFunction  extends ProcessFunction<Message,Message> {

    @Override
    public void processElement(Message value, Context ctx, Collector<Message> out) throws Exception {

    }
}
