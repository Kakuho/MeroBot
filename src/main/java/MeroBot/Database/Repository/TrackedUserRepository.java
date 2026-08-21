package MeroBot.Database.Repository;

import MeroBot.Database.MeroDatabase;
import MeroBot.Database.Models.TrackedUser;

import java.util.List;
import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class TrackedUserRepository{
  public TrackedUserRepository(){

  }

  private List<TrackedUser> ExtractTrackedUsers(PreparedStatement pst) throws SQLException{
    try(ResultSet rs = pst.executeQuery()){
      List<TrackedUser> users = new ArrayList<>();
      while (rs.next()) {
        TrackedUser user = new TrackedUser(
            rs.getString(1),
            rs.getBoolean(2),
            rs.getBoolean(3),
            rs.getString(4),
            rs.getDate(5)
        );

        users.add(user);
      }
      return users;
    }
    catch(SQLException exception){
      throw exception;
    }
  }

  public List<TrackedUser> GetTrackedUsers() throws SQLException{
    // the public facing method performs preparedstatement setup, the private method for extraction
    // just extracts the results from the result set
    String SQL_QUERY = "select * from ?;";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
    ){
      pst.setString(0, "tracked_user");
      return ExtractTrackedUsers(pst);
    }
    catch(SQLException exception){
      throw exception;
    }
  }

  private TrackedUser ExtractTrackedUser(PreparedStatement pst) throws SQLException{
    try(ResultSet rs = pst.executeQuery()){
      if(!rs.next()){
        return null;
      }
      TrackedUser user = new TrackedUser(
          rs.getString(1),
          rs.getBoolean(2),
          rs.getBoolean(3),
          rs.getString(4),
          rs.getDate(5)
      );
      return user;
    }
    catch(SQLException exception){
      System.out.println(exception);
      throw exception;
    }
  }

  public boolean AddTrackedUser(String userId, boolean ignored) throws SQLException{
    String query = "insert into tracked_user(user_id, ignored) values(?, ?);";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setString(1, userId);
      pst.setBoolean(2, ignored);
      if(pst.executeUpdate() >= 1){
        return true;
      }
      else{
        return false;
      }
    }
    catch(SQLException exception){
      System.out.println(exception);
      throw exception;
    }
  }

  public boolean AddTrackedUserAdmin(String userId, boolean ignored, String adminId) throws SQLException{
    String query = "insert into tracked_user(user_id, ignored, admin_ignored, admin_id) values(?, ?, 'true', ?);";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setString(1, userId);
      pst.setBoolean(2, ignored);
      pst.setString(3, adminId);
      if(pst.executeUpdate() >= 1){
        return true;
      }
      else{
        return false;
      }
    }
    catch(SQLException exception){
      System.out.println(exception);
      throw exception;
    }
  }


  public TrackedUser GetTrackedUser(String userId) throws SQLException{
    String query = "select * from tracked_user where user_id = ?;";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setString(1, userId);
      return ExtractTrackedUser(pst);
    }
    catch(SQLException exception){
      System.out.println(exception);
      throw exception;
    }
  }

  public boolean SetTrackedUser(String userId, boolean ignored) throws SQLException{
    String query = "update tracked_user set ignored = ? where user_id = ?;";
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
      System.out.println(exception);
      throw exception;
    }
  }

  public boolean SetTrackedUserAdmin(String userId, boolean ignored, String adminId) throws SQLException{
    String query = "update tracked_user set ignored = ?, admin_ignored = ?, admin_id = ? where user_id = ?;";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setBoolean(1, ignored);
      pst.setBoolean(2, ignored);
      pst.setString(3, adminId);
      pst.setString(4, userId);
      if(pst.executeUpdate() >= 1){
        return true;
      }
      else{
        return false;
      }
    }
    catch(SQLException exception){
      System.out.println(exception);
      throw exception;
    }
  }

}
