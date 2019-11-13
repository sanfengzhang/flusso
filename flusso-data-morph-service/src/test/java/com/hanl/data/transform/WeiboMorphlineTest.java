package com.hanl.data.transform;

import com.alibaba.fastjson.parser.ParserConfig;
import com.codahale.metrics.SharedMetricRegistries;
import com.google.common.base.Preconditions;
import com.typesafe.config.ConfigFactory;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.Compiler;
import org.kitesdk.morphline.base.FaultTolerance;
import org.kitesdk.morphline.base.Fields;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/11/8
 * @desc:
 */
public class WeiboMorphlineTest {

    private MorphlineContext morphlineContext;

    private MorphlineTest.Collector finalChild;


    @Test
    public void testWeibo() {

        Command command = new Compiler().compile(new File("src/test/reources/weibo.conf"), "weibo",
                morphlineContext, finalChild, ConfigFactory.empty());
        Record record = new Record();
        record.put(Fields.MESSAGE, "d38e9bed5d98110dc2489d0d1cac3c2a\t7d45833d9865727a88b960b0603c19f6\t2015-02-23 17:41:29\t0\t0\t10\thao are you,fine,thank you\n");
        command.process(record);
        System.out.println(finalChild.getRecords().get(0));
    }

    @Before
    public void setUp() {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        FaultTolerance faultTolerance = new FaultTolerance(false, false);
        morphlineContext = new MorphlineContext.Builder().setExceptionHandler(faultTolerance)
                .setMetricRegistry(SharedMetricRegistries.getOrCreate("testId")).build();
        this.morphlineContext.getSettings().put("parserConfig", new ParserConfig());
        finalChild = new MorphlineTest.Collector();
    }

    public static final class Collector implements Command {

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
}
