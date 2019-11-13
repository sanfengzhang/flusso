package org.kitesdk.morphline.api;

import java.util.Collection;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/24
 * @desc:
 */
public class AllSubFlowSelector implements FindSubFlowSelector {

    private Map<String, Command> subFlows;

    @Override
    public Collection<Command> select(Record record) {

        return subFlows.values();
    }

    @Override
    public void setCommands(Map<String, Command> subFlows) {
        this.subFlows = subFlows;
    }

    @Override
    public void setCommandInstanceId(String commandInstanceId) {

    }
}
