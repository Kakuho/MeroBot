package MeroBot.Database;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;

public class HikariConnectionPool implements IConnectionPool{
  private HikariConfig config;
  private HikariDataSource dataSource;

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
      // log that the data source fails
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
      // log that hikair cannot initialise the connection pools
      throw new SQLNonTransientConnectionException();
    }
  }
}
