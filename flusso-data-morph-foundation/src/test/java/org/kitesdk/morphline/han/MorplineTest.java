package org.kitesdk.morphline.han;

import com.codahale.metrics.SharedMetricRegistries;
import com.google.common.base.Splitter;
import com.typesafe.config.ConfigFactory;
import org.junit.Before;
import org.junit.Test;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.*;
import org.kitesdk.morphline.base.Compiler;

import java.io.File;

/**
 * @author: Hanl
 * @date :2019/8/30
 * @desc:
 */
public class MorplineTest {

    private MorphlineContext morphlineContext;

    Collector finalChild = new Collector();

    @Before
    public void setup() {
        morphlineContext = new MorphlineContext.Builder().setExceptionHandler(new FaultTolerance(false, false))
                .setMetricRegistry(SharedMetricRegistries.getOrCreate("test")).build();
    }

    @Test
    public void testSpiltGuava() {
        Splitter splitter = Splitter.on(',');
        for (int i = 0; i < 1000000; i++) {
            String msg = "zhangsan" + i + "|li-si,1234";
            splitter.split("|");

        }
    }

    @Test
    public void testSplit() {

        Command command = new Compiler().compile(new File("src/test/resources/test-morphlines/split.conf"), "morphline1",
                morphlineContext, finalChild, ConfigFactory.empty());

        for (int i = 0; i < 1000000; i++) {
            //  Notifications.notifyStartSession(command);
            String msg = "zhangsan" + i + "|li-si,1234";
            final Record record = new Record();
            record.put(Fields.MESSAGE, msg);
            command.process(record);
//            System.out.println(msg.split("|")[0]);
            //  Notifications.notifyShutdown(command);
        }
        System.out.println("end");
//        record = finalChild.getRecords().get(0);
//        System.out.println(record);
//        System.out.println(record.get("output").size());
//        System.out.println(record.get("output1").size());
    }

    @Test
    public void testSplitKeyValue() {

        morphlineContext.getSettings().put(MorphlineContext.COMMAND_CLASS_EXPORT_ID, "C:\\Users\\hanlin01\\Desktop\\jar\\");
        morphlineContext.getSettings().put(AbstractCommand.SKIP_COMMAND_SELECTOR, new KeySkipCommandSelector());
        Command command = new Compiler().compile(new File("src/test/resources/test-morphlines/split1.conf"), "morphline1",
                morphlineContext, finalChild, ConfigFactory.empty());
        Notifications.notifyStartSession(command);
        String msg = "zhangsan|123456";
        Record record = new Record();
        record.put(Fields.MESSAGE, msg);
        command.process(record);

        record = finalChild.getRecords().get(0);
        System.out.println(record);

    }
}
