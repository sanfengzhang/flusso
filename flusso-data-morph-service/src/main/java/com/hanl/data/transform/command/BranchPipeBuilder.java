package com.hanl.data.transform.command;

import org.kitesdk.morphline.api.CommandDescription;
import org.kitesdk.morphline.api.CommandParam;
import com.typesafe.config.Config;
import org.kitesdk.morphline.api.*;
import org.kitesdk.morphline.base.AbstractCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/11/1
 * @desc:
 */
public class BranchPipeBuilder implements CommandBuilder {
    @Override
    public Collection<String> getNames() {
        return Collections.singletonList("branchPipe");
    }

    @Override
    public Command build(Config config, Command parent, Command child, MorphlineContext context) {
        try {
            return new BranchPipeBuilder.BranchPipe(this, config, parent, child, context);
        } catch (Exception e) {
            throw new MorphlineCompilationException("BranchPipeBuilder", config, new Throwable(e));
        }
    }

    @CommandDescription(morphName = "branchPipe",name = "分支程调用节点",cmdType = "流程控制")
    private static final class BranchPipe extends AbstractCommand {

        private static final Logger logger = LoggerFactory.getLogger(BranchPipeBuilder.BranchPipe.class);

        private BranchPipeSelector branchPipeSelector;

        @CommandParam(paramName = "branchPipeSelector", paramType = "java.lang.String", paramDisplayName = "分支流程选择器")
        private String branchPipelineSelectorClazz;

        @CommandParam(paramName = "continueParentPipe", paramType = "java.lang.Boolean", paramDisplayName = "是否执行父流程")
        private boolean continueParentPipe;

        //这个分支节点包含哪些分支流程，如果不配置则所有的流程都可以成为分支流程的选择
        @CommandParam(paramName = "branchFlowIds", paramType = "java.util.List", paramDisplayName = "分支流程列表")
        private List<String> branchFlowIds;

        private MorphlineContext context;

        public BranchPipe(CommandBuilder builder, Config config, Command parent, Command child, MorphlineContext context) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
            super(builder, config, parent, child, context);
            continueParentPipe = getConfigs().getBoolean(config, "continueParentPipe");
            branchPipelineSelectorClazz = getConfigs().getString(config, "branchPipeSelector");
            branchFlowIds = getConfigs().getStringList(config, "branchFlowIds");
            branchPipeSelector = (BranchPipeSelector) Class.forName(branchPipelineSelectorClazz).newInstance();
            this.context = context;
        }

        @Override
        protected boolean doProcess(Record record) {
            Set<Command> commandPipelineSet = branchPipeSelector.select(record, context);
            for (Command cmd : commandPipelineSet) {
                boolean subProcess = cmd.process(record);
                if (!subProcess) {
                    logger.warn("BranchProcess execution failed,record={}", record);
                }
            }
            if (!continueParentPipe) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Do not continue with the parent process");
                }

                return true;
            }
            return super.doProcess(record);
        }
    }
}
