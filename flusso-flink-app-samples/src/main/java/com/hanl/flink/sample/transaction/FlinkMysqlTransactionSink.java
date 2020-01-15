package com.hanl.flink.sample.transaction;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.typeutils.base.VoidSerializer;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.typeutils.runtime.kryo.KryoSerializer;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.TwoPhaseCommitSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.Set;

/*
 @author: Hanl
 @date :2019/12/10
  @desc: Flink 端到端的exactly-once需要外部系统保障，因为Flink将数据提交到外部系统的时候
  并不会负责保障数据是否完全写入了，可能网络故障或其他原因导致数据并没有安全的写入
  外部系统。那这个时候Flink如何保障内部的一致性了？如果commit失败，{@TwoPhaseCommitSinkFunction}
  可以使用保存的状态再次进行commit操作。
 需要理解Flink的两阶段提交到底是怎么回事？
  Flink 端到端的exactly-once需要外部系统保障，因为Flink将数据提交到外部系统的时候
 并不会负责保障数据是否完全写入了，可能网络故障或其他原因导致数据并没有安全的写入
  外部系统。那这个时候Flink如何保障内部的一致性了？如果commit失败，{@TwoPhaseCommitSinkFunction}
 可以使用保存的状态再次进行commit操作。
 需要理解Flink的两阶段提交到底是怎么回事？
 */
/*
 Flink 端到端的exactly-once需要外部系统保障，因为Flink将数据提交到外部系统的时候
 并不会负责保障数据是否完全写入了，可能网络故障或其他原因导致数据并没有安全的写入
  外部系统。那这个时候Flink如何保障内部的一致性了？如果commit失败，{@TwoPhaseCommitSinkFunction}
  可以使用保存的状态再次进行commit操作。
 需要理解Flink的两阶段提交到底是怎么回事？
 */

/**
 * 第一阶段是通过CP Barrier在每个算子进行状态快照完毕之后都会向CP调度器通信汇报CP执行完了，但是此时
 * CP调度器会在收到所有的Operator的CP消息上报之后，会回调{@CheckpointCoordinator}的notifyCheckpointComplete
 * 方法，这样我们就可以通过这样的方式来执行向外部系统commit的操作。
 * 进入第二阶段的提交:
 * 首先我们知道Flink如果只在完成第一阶段的执行，那么直接进行数据提交外部系统。这个时候Flink系统内部状态是没有
 * 问题的。但是如果将外部系统和Flink看成一个数据流业务整体就存在问题了，就是在commit外部系统失败了！！！
 * 那是不就不能保证数据只消费一次了。
 * <p>
 * 解决办法是：外部系统必须支持事物机制。在向外部系统提交数据执行时机应该放在哪里？就是在CP调度器回调的notifyCheckpointComplete
 * 的时候，因为这个时候Flink已经保障自己内部状态的一致性，那么在的notifyCheckpointComplete里面就可以执行
 * 数据提交到外部系统的操作。             *
 * 并且Flink在该算子的时候会记录当前批次的数据状态，这样在commit失败了
 * 重启的时候Flink会读取本地状态尝试再次commit直至成功。这样就保障了数据end-to-end的时候的一致性
 */
public class FlinkMysqlTransactionSink extends TwoPhaseCommitSinkFunction<Tuple2<String, Integer>, FlinkMysqlTransactionSink.MYSQLTransactionState, Void> {

    private transient Connection connection;

    private final String username;

    private final String password;

    private final String drivername;

    protected final String dbURL;

    protected final String query;

    private PreparedStatement upload;

    private transient ListState<Set<Tuple2<String, Integer>>> listState;


    public FlinkMysqlTransactionSink(String username, String password, String drivername, String dbURL, String query) {
        super(new KryoSerializer<MYSQLTransactionState>(MYSQLTransactionState.class, new ExecutionConfig()), VoidSerializer.INSTANCE);
        this.username = username;
        this.password = password;
        this.drivername = drivername;
        this.dbURL = dbURL;
        this.query = query;
    }


    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    protected void invoke(MYSQLTransactionState transactionState, Tuple2<String, Integer> record, Context context) throws Exception {
        transactionState.invoke(record);
    }

    @Override
    protected FlinkMysqlTransactionSink.MYSQLTransactionState beginTransaction() throws Exception {
        /**
         * 初始化数据库连接的时候需要放在这里面，为啥不放在open里面？因为Operator初始化的时候会先调用
         * initializeState方法，在TwoPhaseCommitSinkFunction中中会对失败或记录的事物进行commit。所以需要
         * 在这个时候进行初始化赋值
         */
        if (null == connection) {
            Class.forName(drivername);
            if (username == null) {
                connection = DriverManager.getConnection(dbURL);
            } else {
                connection = DriverManager.getConnection(dbURL, username, password);
            }
            connection.setAutoCommit(false);
            upload = connection.prepareStatement(query);
        }
        MYSQLTransactionState transactionState = new FlinkMysqlTransactionSink.MYSQLTransactionState(connection, upload);
        return transactionState;
    }


    @Override
    protected void preCommit(MYSQLTransactionState transaction) throws Exception {

    }

    @Override
    protected void recoverAndCommit(MYSQLTransactionState transaction) {

        System.out.println("recoverAndCommit...");
    }

    @Override
    protected void commit(MYSQLTransactionState transactionState) {
        try {
            Random r = new Random();
            int a = r.nextInt(2);
            if (1 == a) {
                throw new IllegalArgumentException("commit exception");
            }
            transactionState.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void abort(MYSQLTransactionState transactionState) {
        try {
            transactionState.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static final class MYSQLTransactionState {

        private final transient Connection connection;

        private final transient PreparedStatement upload;

        MYSQLTransactionState(Connection connection, PreparedStatement upload) {
            this.connection = connection;
            this.upload = upload;
        }

        public void commit() throws SQLException {
            upload.executeBatch();
            connection.commit();
        }

        public void rollback() throws SQLException {
            connection.rollback();
        }

        public void invoke(Tuple2<String, Integer> record) throws SQLException {
            upload.setInt(1, record.f1);
            upload.setString(2, record.f0);
            upload.addBatch();///这里只是将记录添加到statement中
        }
    }
}
