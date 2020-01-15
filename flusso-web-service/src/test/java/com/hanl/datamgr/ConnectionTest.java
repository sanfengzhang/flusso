package com.hanl.datamgr;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @author: Hanl
 * @date :2019/12/10
 * @desc:
 */
public class ConnectionTest {

    Connection connection;

    PreparedStatement upload;

    @Before
    public void setup() throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/han?characterEncoding=utf-8&useSSL=false", "root", "root");
        connection.setAutoCommit(false);
        upload = connection.prepareStatement("insert into t_channel(channel_id,channel_name) values(?,?)");
    }

    @Test
    public void testNoCommit() throws Exception {
        upload.setInt(1, 12);
        upload.setString(2, "ajjjj");
        upload.addBatch();
        upload.setInt(1, 13);
        upload.setString(2, "ajjjjdd");
        upload.addBatch();
        upload.executeBatch();
        connection.commit();


    }

    @After
    public void destroy() throws Exception {
        if (null != upload) {
            upload.close();
        }
        if (null != connection) {
            connection.close();
        }
    }
}
