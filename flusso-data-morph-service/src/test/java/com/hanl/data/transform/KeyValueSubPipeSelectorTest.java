package com.hanl.data.transform;

import com.alibaba.fastjson.parser.ParserConfig;
import com.codahale.metrics.SharedMetricRegistries;
import com.hanl.data.transform.api.CommandBuildService;
import com.hanl.data.transform.command.SubPipeSelector;
import com.hanl.data.transform.model.CommandPipeline;
import com.hanl.data.transform.utils.TypeUtils;
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
 * @date :2019/9/12
 * @desc:
 */
public class KeyValueSubPipeSelectorTest {

    private MorphlineContext morphlineContext;

    @Before
    public void setUp() {
        System.out.println(String[].class.getName());
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        FaultTolerance faultTolerance = new FaultTolerance(false, false);
        morphlineContext = new MorphlineContext.Builder().setExceptionHandler(faultTolerance)
                .setMetricRegistry(SharedMetricRegistries.getOrCreate("testId")).build();
        this.morphlineContext.getSettings().put("parserConfig", new ParserConfig());
    }

    @Test
    public void testEL(){
        Map<String, String> expressMap = new HashMap<>();
        expressMap.put("trans_return_code<0 \"?\" 99999 \":\"trans_return_code", "java.lang.Integer,trans_return_code");
        Map<String, Object> cacheWarmingData = new HashMap<>();
        cacheWarmingData.put("trans_return_code", "999");
        Map<String, Object> expressCommand = CommandBuildService.elExpress(expressMap, cacheWarmingData);
        System.out.println(expressCommand.toString());

    }


    @Test
    public void testKeyValueSubPipeSelector() throws Exception {
        Class clzz = KeyValueSubPipeSelector.class;
        //KeyValueSubPipeSelector<String> keyValueSubPipeSelector = (KeyValueSubPipeSelector) clzz.newInstance();
        Set<Integer> ageSet = new HashSet<>();
        Map<String, Command> commandMap = null;
        KeyValueSubPipeSelector keyValueSubPipeSelector = (KeyValueSubPipeSelector) clzz.getConstructor(new Class[]{String.class, Set.class, Map.class}).
                newInstance("name", ageSet, commandMap);
        System.out.println(keyValueSubPipeSelector.select(null,morphlineContext));
    }

    @Test
    public void testSubCommands() throws Exception {
        List<String> outputFields = new ArrayList<>();
        outputFields.add("trans_date");
        outputFields.add("trans_code");
        outputFields.add("trans_channel_id");
        outputFields.add("trans_start_datetime");
        Map<String, Object> splitCommand = CommandBuildService.spilt("message", outputFields, "|", false, false, false, 4);

        Map<String, String> recordFieldType = new HashMap<>();
        recordFieldType.put("trans_channel_id", TypeUtils.INT);
        recordFieldType.put("trans_date", Map.class.getName());
        Map<String, Object> recordFieldTypeCommand = CommandBuildService.recordFieldType(recordFieldType);


        Map<String, Object> callSubCommand = CommandBuildService.callSubPipe("keyValueSubPipeSelector", true);

        List<String> imports = new ArrayList<>();
        imports.add("com.stream.data.transform.command.*");
        CommandPipeline commands = CommandPipeline.build("trad_conf", imports).addCommand(splitCommand).addCommand(recordFieldTypeCommand).addCommand(callSubCommand);


        MorphlineTest.Collector finalChid1 = new MorphlineTest.Collector();
        Map<String, Object> javaCommand = CommandBuildService.java(null,
                "System.out.println(\"Execute subprocess!\"); return child.process(record);");
        CommandPipeline subcommands = CommandPipeline.build("trad_conf_sub", imports).addCommand(javaCommand);
        Config config1 = ConfigFactory.parseMap(subcommands.get());
        Command subcmd = new Compiler().compile(config1, morphlineContext, finalChid1);

        Map<Integer, Command> subCmdMap = new HashMap<>();
        subCmdMap.put(12, subcmd);
        Set<Integer> set = new HashSet<>();
        set.add(12);
        SubPipeSelector subPipeSelector = new KeyValueSubPipeSelector("trans_channel_id", set, subCmdMap);
        this.morphlineContext.getSettings().put("keyValueSubPipeSelector", subPipeSelector);


        Config config = ConfigFactory.parseMap(commands.get());
        Command cmd = new Compiler().compile(config, morphlineContext, finalChid1);
        Notifications.notifyStartSession(cmd);
        Record record = new Record();
        String msg = "{\"start_date\":\"2018-03-25\",\"end_date\":\"2018-03-26\"}|zhangsan|12|武汉市";
        record.put(Fields.MESSAGE, msg);
        cmd.process(record);
        record = finalChid1.getRecords().get(0);
        System.out.println(record);

    }
}
