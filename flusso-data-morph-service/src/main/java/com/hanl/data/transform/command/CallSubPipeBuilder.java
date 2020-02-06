package com.hanl.data.transform.command;

import org.kitesdk.morphline.api.CommandDescription;
import org.kitesdk.morphline.api.CommandParam;
import com.typesafe.config.Config;
import org.kitesdk.morphline.api.*;
import org.kitesdk.morphline.base.AbstractCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/9/12
 * @desc:
 */
public class CallSubPipeBuilder implements CommandBuilder {

    @Override
    public Collection<String> getNames() {
        return Collections.singletonList("callSubPipe");
    }

    @Override
    public Command build(Config config, Command parent, Command child, MorphlineContext context) {
        try {
            return new CallSubPipe(this, config, parent, child, context);
        } catch (Exception e) {
            throw new MorphlineCompilationException("CallSubPipeBuilder", config, new Throwable(e));
        }
    }

    @CommandDescription(morphName = "callSubPipe",name = "子流程调用节点",cmdType = "流程控制")
    private static final class CallSubPipe extends AbstractCommand {

        private static final Logger logger = LoggerFactory.getLogger(CallSubPipe.class);

        @CommandParam(paramName = "flowId", paramType = "java.lang.String", paramDisplayName = "子流程名称")
        private String subPipeId;

        private Command subPipe;

        private MorphlineContext context;

        public CallSubPipe(CommandBuilder builder, Config config, Command parent, Command child, MorphlineContext context) throws Exception {
            super(builder, config, parent, child, context);
            subPipeId = getConfigs().getString(config, "flowId");
            this.context = context;
            subPipe = context.getCommandById(subPipeId);//从context中获取子流程
            if (null == subPipe) {
                // throw new MorphlineCompilationException("SubPipe null and subPipeId=[" + subPipeId + "]", config, new Throwable());
                logger.warn("Init SubPipe null and subPipeId={}", subPipeId);
            }
        }

        @Override
        protected boolean doProcess(Record record) {
            if (logger.isDebugEnabled()) {
                logger.debug("Start executed subProcess.");
            }
            if (null == subPipe) {
                subPipe = context.getCommandById(subPipeId);
            }
            boolean subProcess = subPipe.process(record);
            if (!subProcess) {
                logger.warn("SubProcess execution failed,subPipeId={},record={}", subPipeId, record);
            }
            return super.doProcess(record);
        }
    }
}
