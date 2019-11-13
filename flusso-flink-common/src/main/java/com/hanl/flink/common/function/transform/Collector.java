package com.hanl.flink.common.function.transform;

import com.google.common.base.Preconditions;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/8/30
 * @desc:
 */
public class Collector implements Command {

    private final List<Record> results = new ArrayList<Record>();

    public List<Record> getRecords() {
        return results;
    }

    public void reset() {
        results.clear();
    }

    public Command getParent() {
        return null;
    }

    public void notify(Record notification) {
    }

    public boolean process(Record record) {
        Preconditions.checkNotNull(record);
        results.add(record);
        return true;
    }
}
