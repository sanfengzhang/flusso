package com.hanl.data.transform.command;

import com.typesafe.config.Config;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.CommandBuilder;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.AbstractCommand;

import java.util.Collection;
import java.util.Collections;

/**
 * @author: Hanl
 * @date :2019/9/19
 * @desc:
 */
public class SplitBuilder implements CommandBuilder {


    @Override
    public Collection<String> getNames() {
        return Collections.singletonList("customSplit");
    }

    @Override
    public Command build(Config config, Command parent, Command child, MorphlineContext context) {
        return new SplitBuilder.MySplit(this, config, parent, child, context);
    }

    private static final class MySplit extends AbstractCommand {

        public MySplit(CommandBuilder builder, Config config, Command parent, Command child, MorphlineContext context) {
            super(builder, config, parent, child, context);
        }

        @Override
        protected boolean doProcess(Record record) {

            System.out.println("hello mysplit");
            return super.doProcess(record);
        }
    }
}
