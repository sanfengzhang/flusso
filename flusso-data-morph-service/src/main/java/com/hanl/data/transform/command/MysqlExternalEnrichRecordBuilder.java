package com.hanl.data.transform.command;

import com.typesafe.config.Config;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.CommandBuilder;
import org.kitesdk.morphline.api.MorphlineCompilationException;
import org.kitesdk.morphline.api.MorphlineContext;

import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/8
 * @desc:
 */
public class MysqlExternalEnrichRecordBuilder implements CommandBuilder {

    @Override
    public Collection<String> getNames() {

        return Collections.singletonList("mysqlEnrich");
    }

    @Override
    public Command build(Config config, Command parent, Command child, MorphlineContext context) {
        try {
            return new MysqlExternalEnrichRecord(this, config, parent, child, context);
        } catch (Exception e) {
            throw new MorphlineCompilationException("MysqlExternalEnrichRecord init failed", config, e);
        }
    }

    private static final class MysqlExternalEnrichRecord extends ExternalEnrichRecord {

        private String jdbcURL;

        private String userName;

        private String password;

        private String driverClassName;

        private Connection dbConn;

        private String querySQL;

        private String defaultValue;

        private String initCacheQuery;

        protected MysqlExternalEnrichRecord(CommandBuilder builder, Config config, Command parent, Command child, MorphlineContext context) throws Exception {

            super(builder, config, parent, child, context);
            jdbcURL = getConfigs().getString(config, "jdbcUrl");
            userName = getConfigs().getString(config, "userName");
            password = getConfigs().getString(config, "password");
            driverClassName = getConfigs().getString(config, "driverName");
            querySQL = getConfigs().getString(config, "querySqlKey");
            defaultValue = getConfigs().getString(config, "defaultValue");
            initCacheQuery = getConfigs().getString(config, "initCacheQuery");
            Class.forName(driverClassName);
            validateArguments();
            dbConn = DriverManager.getConnection(jdbcURL, userName, password);
            initializingAfter();
        }

        @Override
        protected Object loadObjectFromExternal(Object key) throws SQLException {
            PreparedStatement statement = dbConn.prepareStatement(querySQL);
            statement.setObject(1, key);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {

                return rs.getObject(1);
            }
            return defaultValue;
        }

        @Override
        protected Map<Object, Object> initCache() {
            if (null == initCacheQuery || "".equals(initCacheQuery)) {
                return null;
            }
            Map<Object, Object> result = new HashMap<>();
            try {
                PreparedStatement statement = dbConn.prepareStatement(initCacheQuery);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    result.put(rs.getObject(1), rs.getObject(2));
                }
            } catch (Exception e) {
                throw new MorphlineCompilationException("init cache failed", null, e);
            }
            return result;
        }

    }
}

