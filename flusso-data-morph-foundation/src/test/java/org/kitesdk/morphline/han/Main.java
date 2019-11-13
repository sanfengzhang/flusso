package org.kitesdk.morphline.han;

import com.codahale.metrics.SharedMetricRegistries;
import com.typesafe.config.ConfigFactory;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.Compiler;
import org.kitesdk.morphline.base.FaultTolerance;
import org.kitesdk.morphline.base.Fields;
import org.kitesdk.morphline.base.Notifications;

import java.io.File;

/**
 * @author: Hanl
 * @date :2019/9/19
 * @desc:
 */
public class Main {


    static MorphlineContext morphlineContext = new MorphlineContext.Builder().setExceptionHandler(new FaultTolerance(false, false))
            .setMetricRegistry(SharedMetricRegistries.getOrCreate("test")).build();

    static Collector finalChild = new Collector();
    static Command command = new Compiler().compile(new File("src/test/resources/test-morphlines/split.conf"), "morphline1",
            morphlineContext, finalChild, ConfigFactory.empty());


    public static void main(String[] args) {

        Record record = new Record();
        for (int i = 0; i < 100000000; i++) {
            main(i,record);
        }
        System.out.println("end");

    }

    static void main(int i,Record record) {
        String msg = "zhangsan" + i + "|li-si,1234";
        Notifications.notifyStartSession(command);
        record.put(Fields.MESSAGE, msg);
        command.process(record);
        finalChild.reset();
        Notifications.notifyShutdown(command);

    }
}
