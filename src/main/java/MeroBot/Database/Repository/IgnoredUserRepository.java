package MeroBot.Database.Repository;

import MeroBot.Database.MeroDatabase;
import MeroBot.Database.Models.IgnoredUser;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class IgnoredUserRepository{
  public IgnoredUserRepository(){

  }

  private List<IgnoredUser> ExtractIgnoredUsers(PreparedStatement pst){
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

  public List<IgnoredUser> GetIgnoredUsers(){
    // the public facing method performs preparedstatement setup, the private method for extraction
    // just extracts the results from the result set
    String SQL_QUERY = "select * from ?;";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
    ){
      pst.setString(0, "ignored_user");
      return ExtractIgnoredUsers(pst);
    }
    catch(SQLException exception){
      return null;
    }
  }

  private IgnoredUser ExtractIgnoredUser(PreparedStatement pst){
    try(ResultSet rs = pst.executeQuery()){
      if(!rs.next()){
        return null;
      }
      IgnoredUser user = new IgnoredUser(
          rs.getString(1),
          rs.getBoolean(2),
          rs.getDate(3)
      );
      return user;
    }
    catch(SQLException exception){
      return null;
    }
  }

  public boolean AddIgnoredUser(String userId){
    String query = "insert into ignored_user(user_id, ignored) values(?, 'true');";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setString(1, userId);
      if(pst.executeUpdate() >= 1){
        return true;
      }
      else{
        return false;
      }
    }
    catch(SQLException exception){
      return false;
    }
  }

  public IgnoredUser GetIgnoredUser(String userId){
    String query = "select * from ignored_user where user_id = ?;";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setString(1, userId);
      return ExtractIgnoredUser(pst);
    }
    catch(SQLException exception){
      return null;
    }
  }

  public boolean SetIgnoredUser(String userId, boolean ignored){
    String query = "update ignored_user set ignored = ? where user_id = ?;";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setBoolean(1, ignored);
      pst.setString(2, userId);
      if(pst.executeUpdate() >= 1){
        return true;
      }
      else{
        return false;
      }
    }
    catch(SQLException exception){
      return false;
    }
  }
}
