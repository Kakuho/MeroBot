package MeroBot.Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HikariConnectionPool implements IConnectionPool{
  private HikariConfig config;
  private HikariDataSource dataSource;
  private static final Logger logger = LoggerFactory.getLogger(HikariConnectionPool.class);

  public HikariConnectionPool(String username, String password, String connectionString){
    this.config = new HikariConfig();
    this.config.setJdbcUrl(connectionString);
    this.config.setUsername(username);
    this.config.setPassword(password);
    this.config.setConnectionTimeout(250);
    this.config.addDataSourceProperty("cachePrepStmts" , "true");
    this.config.addDataSourceProperty("prepStmtCacheSize" , "250");
    this.config.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");
    try{
      this.dataSource = new HikariDataSource(config);
    }
    catch(HikariPool.PoolInitializationException e){
      logger.error("Failed to initialise the connection pool, reason: " + e.getMessage());
    }
  }

  public HikariConnectionPool(){
    this.config = new HikariConfig();
    this.config.setJdbcUrl("jdbc:postgresql://localhost:8091/merobot_db");
    this.config.setUsername("postgres");
    this.config.setPassword("pw");
    this.config.setConnectionTimeout(250);
    this.config.addDataSourceProperty("cachePrepStmts" , "true");
    this.config.addDataSourceProperty("prepStmtCacheSize" , "250");
    this.config.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");
    try{
      this.dataSource = new HikariDataSource(config);
    }
    catch(HikariPool.PoolInitializationException e){
      logger.error("Failed to initialise the connection pool, reason: " + e.getMessage());
    }
  }

  public Connection GetConnection() throws SQLException {
    try{
      if(dataSource == null){
        dataSource = new HikariDataSource(this.config);
      }
      return dataSource.getConnection();
    }
    catch(HikariPool.PoolInitializationException e){
      logger.error("Failed to initialise the connection pool, reason: " + e.getMessage());
      throw new SQLNonTransientConnectionException();
    }
  }
}
