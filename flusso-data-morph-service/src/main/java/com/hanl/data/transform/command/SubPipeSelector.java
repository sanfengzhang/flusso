package com.hanl.data.transform.command;

import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;

import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/9/12
 * @desc: 子流程选择器, 用户可以通过定义子流程选择器去执行对应的流程
 */
public interface SubPipeSelector {

    public Set<Command> select(Record record, MorphlineContext context);
}
