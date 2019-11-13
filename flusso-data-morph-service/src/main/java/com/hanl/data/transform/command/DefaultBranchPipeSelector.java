package com.hanl.data.transform.command;

import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.Fields;

import java.util.Collections;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/11/1
 * @desc:
 */
public class DefaultBranchPipeSelector implements BranchPipeSelector {

    @Override
    public Set<Command> select(Record record, MorphlineContext context) {

        String fieldValue = record.getFirstValue(Fields.MESSAGE).toString();
        Command pipe = null;
        if (fieldValue.contains("1.1.1.1")) {
            pipe = context.getCommandById("|解析流程");
        } else  {
            pipe = context.getCommandById("-解析流程");
        }

        if (null == pipe) {
            throw new NullPointerException("Field corresponding value[" + fieldValue + "],then pipe is null.");
        }
        return Collections.singleton(pipe);
    }
}
