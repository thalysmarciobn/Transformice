package com.transformice.database;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.transformice.logging.Logging;
import com.transformice.config.Config;
import jdbchelper.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Database {
    private JdbcHelper jdbc;
    private ConnectionPool pool;
    private MysqlConnectionPoolDataSource source;
    private ScheduledExecutorService tasks = Executors.newScheduledThreadPool(1);

    public boolean connect() {
        if (Config.database()) {
            long start = System.currentTimeMillis();
            this.source = new MysqlConnectionPoolDataSource();
            this.source.setServerName(Config.database.host);
            this.source.setPort(Config.database.port);
            this.source.setUser(Config.database.user);
            this.source.setPassword(Config.database.pass);
            this.source.setDatabaseName(Config.database.data);
            this.source.setAutoReconnectForConnectionPools(true);
            this.pool = new ConnectionPool(this.source, Config.database.pool);
            this.jdbc = new JdbcHelper(new PooledDataSource(this.pool));
            this.scheduleTask(() -> this.freeIdleConnections(), 30, TimeUnit.MINUTES, true);
            Logging.print("Database: Pools " + Config.database.pool, "info");
            Logging.print("Database: Connected in " + (System.currentTimeMillis() - start) + "ms.", "info");
            return true;
        }
        return false;
    }

    public ScheduledFuture<?> scheduleTask(Runnable task, long delay, TimeUnit tu, boolean repeat) {
        return repeat ? this.tasks.scheduleAtFixedRate(task, delay, delay, tu) : this.tasks.schedule(task, delay, tu);
    }

    public QueryResult query(String query, Object... params) {
        QueryResult result = null;
        try {
            this.jdbc.beginTransaction();
            result = this.jdbc.query(query, params);
        } catch (JdbcException error) {
            if (this.jdbc.isInTransaction()) {
                this.jdbc.rollbackTransaction();
            }
        } finally {
            if (this.jdbc.isInTransaction() ) {
                this.jdbc.commitTransaction();
            }
        }
        return result;
    }

    public void execute(String query, Object... params) {
        this.jdbc.execute(query, params);
    }

    public void run(String query, Object... params) {
        this.jdbc.run(query, params);
    }

    public void freeIdleConnections() {
        this.pool.freeIdleConnections();
    }

    public int getActiveConnections() {
        return this.pool.getActiveConnections();
    }
}
