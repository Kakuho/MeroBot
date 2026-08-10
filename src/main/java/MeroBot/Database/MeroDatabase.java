package MeroBot.Database;

import MeroBot.Database.IConnectionPool;
import MeroBot.Database.HikariConnectionPool;

import java.util.List;

import com.zaxxer.hikari.pool.HikariPool;

import java.util.ArrayList;
import java.util.Date;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

// Merodatabase Error cases:
//
// Case:
//  Start: Merodb is running, before merobot is running
//
//         connection pool is initialised and connected is true
//
//  Db Failure: Merodb becomes offline
//              connections will give a java.sql.SQLTransientConnectionException
//
//  at this point, set MeroDatabase.connected to false
//
// Case:
//  Start: Merodb is not running
//         connection pool is null and connected is false
//
//  Db Back online: Requires polling to see if 
//                  connections will give a java.sql.SQLTransientConnectionException
//
//  at this point, set MeroDatabase.connected to false


public class MeroDatabase{
  static private IConnectionPool cpool;

  static public void StartDatabase(){
    InitConnectionPool();
  }

  static public void InitConnectionPool(){
    cpool = new HikariConnectionPool();
  }

  static public Connection GetConnection() throws SQLException{ 
    try{
      return cpool.GetConnection();
    }
    catch(SQLException e){
      // log the occured exception
      throw e;
    }
  } 
}
