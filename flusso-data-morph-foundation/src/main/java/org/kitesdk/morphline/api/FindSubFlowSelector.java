package org.kitesdk.morphline.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/10/24
 * @desc: 获取执行节点需要执行的子流程
 */
public interface FindSubFlowSelector {

    public Collection<Command> select(Record record);

    public void setCommands(Map<String, Command> subFlows);

    public void setCommandInstanceId(String commandInstanceId);
}
