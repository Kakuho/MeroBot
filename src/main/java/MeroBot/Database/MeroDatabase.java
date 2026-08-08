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

  record IgnoredUser(String userId,  boolean ignored, Date dateAdded) { }

  private static List<IgnoredUser> ExtractIgnoredUsers(PreparedStatement pst){
    try(ResultSet rs = pst.executeQuery()){
      List<IgnoredUser> users = new ArrayList<>();
      while (rs.next()) {
        IgnoredUser user = new IgnoredUser(
            rs.getString(0),
            rs.getBoolean(1),
            rs.getDate(2)
        );
        users.add(user);
      }
      return users;
    }
    catch(SQLException exception){
      return null;
    }
  }

  public static List<IgnoredUser> GetIgnoredUsers(){
    // the public facing method performs preparedstatement setup, the private method for extraction
    // just extracts the results from the result set
    String SQL_QUERY = "select * from ?;";
    try(Connection con = GetConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
    ){
      pst.setString(0, "ignored_user");
      return ExtractIgnoredUsers(pst);
    }
    catch(SQLException exception){
      return null;
    }
  }
}
