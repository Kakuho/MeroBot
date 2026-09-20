package MeroBot.Database.Repository;

import MeroBot.Database.MeroDatabase;
import MeroBot.Database.Models.User;

import java.util.List;
import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRepository{
  final Logger logger = LoggerFactory.getLogger(UserRepository.class);

  public UserRepository(){

  }

  private List<User> ExtractUsers(PreparedStatement pst) throws SQLException{
    try(ResultSet rs = pst.executeQuery()){
      List<User> users = new ArrayList<>();
      while (rs.next()) {
        User user = new User(
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

  public List<User> GetUsers() throws SQLException{
    // the public facing method performs preparedstatement setup, the private method for extraction
    // just extracts the results from the result set
    String SQL_QUERY = "select * from ?;";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY);
    ){
      pst.setString(0, "merouser");
      return ExtractUsers(pst);
    }
    catch(SQLException exception){
      logger.error("input: [query: `{}`, arg1: `{}`], error: `{}`}",
        SQL_QUERY,
        "user",
        exception
      );
      throw exception;
    }
  }

  private User ExtractUser(PreparedStatement pst) throws SQLException{
    try(ResultSet rs = pst.executeQuery()){
      if(!rs.next()){
        return null;
      }
      User user = new User(
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

  public boolean AddUser(String userId, boolean ignored) throws SQLException{
    String query = "insert into merouser(user_id, ignored) values(?, ?);";
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
      logger.error("input: [query: `{}`, arg1: `{}`, arg2: `{}`], error: `{}`}",
        query,
        userId,
        ignored,
        exception
      );

      throw exception;
    }
  }

  public boolean AddUserAdmin(String userId, boolean ignored, String adminId) throws SQLException{
    String query = "insert into merouser(user_id, ignored, admin_ignored, admin_id) values(?, ?, 'true', ?);";
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
      logger.error("input: [query: `{}`, arg1: `{}`, arg2: `{}`, arg3: `{}`], error: `{}`}",
        query,
        userId,
        ignored,
        adminId,
        exception
      );
      throw exception;
    }
  }

  public User GetUser(String userId) throws SQLException{
    String query = "select * from merouser where user_id = ?;";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setString(1, userId);
      return ExtractUser(pst);
    }
    catch(SQLException exception){
      logger.error("input: [query: `{}`, arg1: `{}`], error: `{}`}",
        query,
        userId,
        exception
      );

      throw exception;
    }
  }

  public boolean SetUser(String userId, boolean ignored) throws SQLException{
    String query = "update merouser set ignored = ? where user_id = ?;";
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
      logger.error("input: [query: `{}`, arg1: `{}`, arg2: `{}`], error: `{}`}",
        query,
        ignored,
        userId,
        exception
      );

      throw exception;
    }
  }

  public boolean SetUserAdmin(String userId, boolean ignored, String adminId) throws SQLException{
    String query = "update merouser set ignored = ?, admin_ignored = ?, admin_id = ? where user_id = ?;";
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
      logger.error("input: [query: `{}`, arg1: `{}`, arg2: `{}`, arg3: `{}`, arg4: `{}`], error: `{}`}",
        query,
        ignored,
        ignored,
        adminId,
        userId,
        exception
      );
      throw exception;
    }
  }
}
