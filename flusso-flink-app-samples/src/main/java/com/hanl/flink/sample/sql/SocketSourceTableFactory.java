package com.hanl.flink.sample.sql;

import org.apache.flink.table.factories.StreamTableSourceFactory;
import org.apache.flink.table.factories.TableFactory;
import org.apache.flink.table.sources.StreamTableSource;
import org.apache.flink.table.sources.TableSource;
import org.apache.flink.types.Row;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hanl.flink.sample.sql.SocketSourceValidator.CONNECTOR_SOCKET_TYPE;
import static org.apache.flink.table.descriptors.ConnectorDescriptorValidator.CONNECTOR_PROPERTY_VERSION;
import static org.apache.flink.table.descriptors.ConnectorDescriptorValidator.CONNECTOR_TYPE;
import static org.apache.flink.table.descriptors.FormatDescriptorValidator.FORMAT_PROPERTY_VERSION;
import static org.apache.flink.table.descriptors.FormatDescriptorValidator.FORMAT_TYPE;
import static org.apache.flink.table.descriptors.OldCsvValidator.FORMAT_TYPE_VALUE;


/**
 * @author: Hanl
 * @date :2019/11/28
 * @desc:
 */
public class SocketSourceTableFactory implements StreamTableSourceFactory<Row> {

    @Override
    public Map<String, String> requiredContext() {
        Map<String, String> context = new HashMap<>();
        context.put(CONNECTOR_TYPE, CONNECTOR_SOCKET_TYPE);
        context.put(FORMAT_TYPE, FORMAT_TYPE_VALUE);
        context.put(CONNECTOR_PROPERTY_VERSION, "1");
        context.put(FORMAT_PROPERTY_VERSION, "1");
        return context;
    }

    @Override
    public List<String> supportedProperties() {
        return null;
    }

    @Override
    public StreamTableSource<Row> createStreamTableSource(Map<String, String> map) {
        return null;
    }

}
