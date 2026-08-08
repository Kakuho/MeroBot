package MeroBot.Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;;

public class HikariConnectionPool implements IConnectionPool{
  private HikariConfig config;
  private HikariDataSource dataSource;

  public HikariConnectionPool(){
    this.config = new HikariConfig();
    this.config.setJdbcUrl("jdbc:postgresql://localhost:8091/merobot_db");
    this.config.setUsername("postgres");
    this.config.setPassword("pw");
    this.config.addDataSourceProperty("cachePrepStmts" , "true");
    this.config.addDataSourceProperty("prepStmtCacheSize" , "250");
    this.config.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");

    this.dataSource = new HikariDataSource(config);
  }

  public Connection GetConnection() throws SQLException {
      return dataSource.getConnection();
  }
}
