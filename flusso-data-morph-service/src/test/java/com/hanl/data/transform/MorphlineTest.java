package com.hanl.data.transform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.codahale.metrics.SharedMetricRegistries;
import com.hanl.data.transform.api.CommandBuildService;
import com.hanl.data.transform.model.CommandPipeline;
import com.hanl.data.transform.utils.TypeUtils;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
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
import org.kitesdk.morphline.base.Notifications;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/8/3
 * @desc:
 */
public class MorphlineTest {

    private MorphlineContext morphlineContext;

    private Collector finalChid;

    @Test
    public void testCommands() throws Exception {
        Map<String, Object> readLineMap = new HashMap<>();
        Map<String, Object> charsetMap = new HashMap<>();
        charsetMap.put("charset", "UTF-8");
        readLineMap.put("readLine", charsetMap);
        List<String> outputFields = new ArrayList<>();
        outputFields.add("trans_date");
        outputFields.add("trans_code");
        outputFields.add("trans_channel_id");
        outputFields.add("trans_start_datetime");
        outputFields.add("trans_end_datetime");
        outputFields.add("trans_cust_time");
        outputFields.add("trans_org_id");
        outputFields.add("trans_clerk");
        outputFields.add("trans_return_code");
        outputFields.add("trans_err_msg");
        outputFields.add("trans_tuexdo_name");

        Map<String, Object> splitCommand = CommandBuildService.spilt("message", outputFields, "|", false, false, false, 11);

        Map<String, Object> javaCommand = CommandBuildService.java(null,
                "logger.debug(\"printing my log info\"); return child.process(record);");
        Map<String, Object> javaMethodCommand = CommandBuildService.javaMethod("com.stream.data.transform.utils.IpaddressUtil", "getIplongValue",
                "trans_tuexdo_name", "ipLongValue", "java.lang.String");


        Map<String, String> expressMap = new HashMap<>();
        expressMap.put("trans_return_code<0\"?\"'失败'\":\"'成功'", "returnCodeDesc");
        Map<String, Object> cacheWarmingData = new HashMap<>();
        cacheWarmingData.put("trans_return_code", "999");
        Map<String, Object> expressCommand = CommandBuildService.elExpress(expressMap, cacheWarmingData);


        Map<String, Object> jdbcCommand = CommandBuildService.jdbcEnrich("trans_channel_id", "trans_channel_name",
                "jdbc:mysql://localhost:3306/han", "com.mysql.jdbc.Driver",
                "root", "123456", 0, true, "SELECT channel_name FROM t_channel where channel_id=?",
                "没有渠道", "select channel_id,channel_name from t_channel");


        Map<String, String> recordFieldType = new HashMap<>();
        recordFieldType.put("trans_channel_id", TypeUtils.INT);
        recordFieldType.put("trans_end_datetime", TypeUtils.DATE);
        recordFieldType.put("trans_return_code", TypeUtils.INT);
        Map<String, Object> recordFieldTypeCommand = CommandBuildService.recordFieldType(recordFieldType);

        List<String> imports = new ArrayList<>();
        imports.add("com.stream.data.transform.command.*");
        CommandPipeline commands = CommandPipeline.build("trad_conf", imports).addCommand(splitCommand).addCommand(recordFieldTypeCommand).
                addCommand(javaMethodCommand).addCommand(expressCommand).addCommand(jdbcCommand);

        Map<String, Object> configMap = commands.get();

        System.out.println(JSON.toJSONString(configMap));
        Config config = ConfigFactory.parseMap(configMap);
        System.out.println(config);
        Collector finalChid = new Collector();
        Command cmd = new Compiler().compile(config, morphlineContext, finalChid);

        Notifications.notifyStartSession(cmd);
        long start = System.currentTimeMillis();
        int total = 1;
        for (int i = 0; i < total; i++) {
            String msg = i + "|801507|234|2018-04-17 17:05:08.478679|2018-04-17 17:05:08.483580|0.00|8020800|020777|-100|读取保函注销接口表失败[BHZX201803251590217],记录不存在|1.1.1.1";
            Record record = new Record();
            record.put(Fields.MESSAGE, msg);
            cmd.process(record);
            record = finalChid.getRecords().get(0);
            System.out.println(record.getFirstValue("trans_end_datetime"));
            System.out.println(record);
        }
        long end = System.currentTimeMillis();
        double cust = (end - start);
        System.out.println(cust / 1000);

    }

    @Test
    public void testInteger() {
        Integer a = 234;
        System.out.println(a.hashCode());
    }


    @Test
    public void testParseConfig() {

        Map<String, Object> configMap = new HashMap<>();
        configMap.put("id", "trad_conf");
        List<String> importCommands = new ArrayList<>();
        importCommands.add("org.kitesdk.**");
        configMap.put("importCommands", importCommands);

        List commandList = new ArrayList<>();

        Map<String, Object> readLineMap = new HashMap<>();
        Map<String, Object> charsetMap = new HashMap<>();
        charsetMap.put("charset", "UTF-8");
        readLineMap.put("readLine", charsetMap);
//        commandList.add(readLineMap);


        List<String> outputFields = new ArrayList<>();
        outputFields.add("trans_date");
        outputFields.add("trans_code");
        outputFields.add("trans_channel_id");
        outputFields.add("trans_start_datetime");
        outputFields.add("trans_end_datetime");
        outputFields.add("trans_cust_time");
        outputFields.add("trans_org_id");
        outputFields.add("trans_clerk");
        outputFields.add("trans_return_code");
        outputFields.add("trans_err_msg");
        outputFields.add("trans_tuexdo_name");
        Map<String, Object> splitCommand = CommandBuildService.spilt("message", outputFields, "|", false, false, false, 11);

        commandList.add(splitCommand);


        List<String> outputFields1 = new ArrayList<>();
        outputFields1.add("trans_date_year");
        outputFields1.add("trans_date_month");
        outputFields1.add("trans_date_date");
        Map<String, Object> splitCommand1 = CommandBuildService.spilt("trans_date", outputFields1, "-", false, false, false, 3);

        commandList.add(splitCommand1);


        configMap.put("commands", commandList);
        Config config = ConfigFactory.parseMap(configMap);
        System.out.println(config);

        Collector finalChid = new Collector();
        Command cmd = new Compiler().compile(config, morphlineContext, finalChid);
        Notifications.notifyStartSession(cmd);

        Record record = new Record();
        String msg = "2018-03-25|801507|12|2018-04-17 17:05:08.478679|2018-04-17 17:05:08.483580|0.00|8020800|020777|999996|读取保函注销接口表失败[BHZX201803251590217],记录不存在|";
        record.put(Fields.MESSAGE, msg);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            cmd.process(record);
            record = finalChid.getRecords().get(0);
            System.out.println(record);
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }


    @Before
    public void setUp() {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        FaultTolerance faultTolerance = new FaultTolerance(false, false);
        morphlineContext = new MorphlineContext.Builder().setExceptionHandler(faultTolerance)
                .setMetricRegistry(SharedMetricRegistries.getOrCreate("testId")).build();
        this.morphlineContext.getSettings().put("parserConfig", new ParserConfig());
        finalChid = new Collector();
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
