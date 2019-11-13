package com.stream.qlexpress;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.MapDeserializer;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.hanl.data.common.EnhanceCache;
import com.hanl.data.transform.utils.TypeUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;


/**
 * @author: Hanl
 * @date :2019/8/5
 * @desc:
 */
public class QLExpressTest {

    private ExpressRunner runner;

    @Before
    public void setup() {

        runner = new ExpressRunner();
    }

    @Test
    public void testEL() throws Exception {

        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        context.put("age", 12);
        context.put("name", "zhangsan");
        Object result = runner.execute("age==12 and name=='zhangsan'", context, null, false, false);
        System.out.println(result);

        DefaultContext<String, Object> context1 = new DefaultContext<String, Object>();
        context1.put("age", 13);
        context1.put("name", "zhangsan");
        Object result1 = runner.execute("age+1", context1, null, false, false);
        System.out.println(result1);

        DefaultContext<String, Object> context2 = new DefaultContext<String, Object>();
        context2.put("age", -1);
        context2.put("name", "zhangsan");
        Object result2 = runner.execute("age<0||age>200?60:age", context2, null, false, false);
        System.out.println(result2);

        DefaultContext<String, Object> context3 = new DefaultContext<String, Object>();
        context3.put("age", -1);
        context3.put("name", "zhangsan");
        Object result3 = runner.execute("age<0?'年龄非法':'年龄合法'", context2, null, false, false);
        System.out.println(result3);

    }

    @Test
    public void testFunction() throws Exception {
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        context.put("one", 12);
        context.put("tow", 10);
        Object result = runner.execute("one*tow", context, null, false, false);
        System.out.println(result);
    }

    @Test
    public void testSql() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        final Connection dbConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/han", "root", "123456");
        PreparedStatement statement = dbConn.prepareStatement("SELECT * FROM t_etl where name=?");
        Object o = "zhangsan";
        statement.setObject(1, o);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getObject(3));
        }
    }

    @Test
    public void testCacheFromDB() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        final Connection dbConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/han", "root", "123456");
        CacheLoader<String, Integer> cacheLoader = new CacheLoader<String, Integer>() {

            @Nullable
            @Override
            public Integer load(@NonNull String key) throws Exception {
                PreparedStatement statement = dbConn.prepareStatement("SELECT * FROM t_etl where name=?");
                statement.setString(1, key);
                ResultSet rs = statement.executeQuery();
                Map<String, Integer> result = new HashMap<>();
                while (rs.next()) {
                    return rs.getInt(3);
                }
                return 0;
            }
        };
        LoadingCache<String, Integer> loadingCache = EnhanceCache.create(200, cacheLoader, Executors.newSingleThreadScheduledExecutor(), new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                PreparedStatement statement = dbConn.prepareStatement("SELECT * FROM t_etl ");
                ResultSet rs = statement.executeQuery();
                Map<String, Integer> result = new HashMap<>();
                while (rs.next()) {
                    result.put(rs.getString(2), rs.getInt(3));

                }
                return result;
            }
        }, 1, 5);


        new Thread() {
            @Override
            public void run() {
                while (true) {
                    loadingCache.asMap().forEach((k, v) -> {
                        System.out.println(k + " " + v);
                    });
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        System.in.read();
    }

    @Test
    public void testType() throws ClassNotFoundException {
        ParserConfig jcParserConfig = new ParserConfig();
        jcParserConfig.putDeserializer(String.class, MapDeserializer.instance);
        // System.out.println(TypeUtils.convert1("{\"name\":\"zhangsan\"}", "java.util.Map", jcParserConfig,""));
        Object s = "{\"trans_return_code<0 \\\"?\\\" 99999 \\\":\\\"trans_return_code\":\"java.lang.Integer,trans_return_code\"}";
        Object str = "{\"trans_return_code\":\"999\"}";
        Map m = com.alibaba.fastjson.util.TypeUtils.cast(str, Map.class, jcParserConfig);
        System.out.println(m);
    }

    @Test
    public void testClassString() {
        Map<String, String> recordFieldType = new HashMap<>();
        recordFieldType.put("trans_channel_id", TypeUtils.INT);
        recordFieldType.put("trans_end_datetime", TypeUtils.DATE);
        recordFieldType.put("trans_return_code", TypeUtils.INT);
        System.out.println(JSON.toJSONString(recordFieldType));
    }
}
