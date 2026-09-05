package MeroBot.Database;

import MeroBot.Config;

import java.lang.RuntimeException;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  static private Logger logger = LoggerFactory.getLogger(MeroDatabase.class);

  static public void StartDatabase(){
    if(!Config.GetInitialised()){
      throw new RuntimeException("Configuration is not initialised!");
    }
    else{
      InitConnectionPool(Config.GetDbUsername(), Config.GetDbPassword(), Config.GetDbConnectionString());
    }
  }

  static public void InitConnectionPool(String username, String password, String connectionString){
    cpool = new HikariConnectionPool(username, password, connectionString);
  }

  static public Connection GetConnection() throws SQLException{ 
    try{
      return cpool.GetConnection();
    }
    catch(SQLException e){
      logger.error("Failed to initialise the connection pool, reason: " + e.getMessage());
      throw e;
    }
  } 
}
