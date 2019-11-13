package com.hanl.data.transform;

import com.hanl.data.transform.command.SubPipeSelector;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/9/12
 * @desc:
 */
public class KeyValueSubPipeSelector implements SubPipeSelector {

    private String key;

    private Set<Integer> valueSelector;

    private Map<Integer, Command> subPipeMap;

    public KeyValueSubPipeSelector(String key, Set<Integer> valueSelector, Map<Integer, Command> subPipes) {
        this.key = key;
        this.valueSelector = valueSelector;
        this.subPipeMap = subPipes;
    }

    @Override
    public Set<Command> select(Record record, MorphlineContext context) {
        Object v = record.getFirstValue(key);
        if (valueSelector.contains(v)) {
            return Collections.singleton(subPipeMap.get(v));
        }
        return Collections.EMPTY_SET;
    }
}
