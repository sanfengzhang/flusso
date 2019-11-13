package com.hanl.data.transform.command;

import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;

import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/11/1
 * @desc:
 */
public interface BranchPipeSelector {

    public Set<Command> select(Record record, MorphlineContext context);
}
