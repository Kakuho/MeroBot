package MeroBot.Database;

import MeroBot.Database.IConnectionPool;
import MeroBot.Database.HikariConnectionPool;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class MeroDatabase{
  static private IConnectionPool cpool;

  static{
    cpool = new HikariConnectionPool();
  }

  static public Connection GetConnection() throws SQLException{ return cpool.GetConnection();} 
}
