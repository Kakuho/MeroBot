package MeroBot.Database.Repository;

import MeroBot.Database.MeroDatabase;
import MeroBot.Database.Models.Role;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoleRepository{
  final Logger logger = LoggerFactory.getLogger(RoleRepository.class);

  public RoleRepository(){

  }

  public boolean AddRole(String value, boolean isAdmin) throws SQLException{
    String query = "insert into role(value, is_admin) values(?, ?);";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setString(1, value);
      pst.setBoolean(2, isAdmin);
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
        value,
        isAdmin,
        exception
      );
      throw exception;
    }
  }

  private Role ExtractRole(PreparedStatement pst) throws SQLException{
    try(ResultSet rs = pst.executeQuery()){
      if(!rs.next()){
        return null;
      }
      Role role = new Role(
          rs.getInt(1),
          rs.getString(2),
          rs.getBoolean(3),
          rs.getDate(4)
      );
      return role;
    }
    catch(SQLException exception){
      throw exception;
    }
  }

  public Role GetRole(String value) throws SQLException{
    String query = "select * from role where value = ?;";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setString(1, value);
      return ExtractRole(pst);
    }
    catch(SQLException exception){
      logger.error("input: [query: `{}`, arg1: `{}`], error: `{}`}",
        query,
        value,
        exception
      );

      throw exception;
    }
  }

  public boolean SetRoleAdmin(String value, boolean isAdmin) throws SQLException{
    String query = "update role set is_admin = ? where value = ?;";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setBoolean(1, isAdmin);
      pst.setString(2, value);
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
        isAdmin,
        value,
        exception
      );
      throw exception;
    }
  }

  private boolean IsRoleAdminExtractor(PreparedStatement pst) throws SQLException{
    try(ResultSet rs = pst.executeQuery()){
      if(!rs.next()){
        return false;
      }
      return rs.getBoolean(1);
    }
    catch(SQLException exception){
      throw exception;
    }
  }

  public boolean IsRoleAdmin(String value) throws SQLException{
    String query = "select is_admin from role where value = ?;";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setString(1, value);
      return IsRoleAdminExtractor(pst);
    }
    catch(SQLException exception){
      logger.error("input: [query: `{}`, arg1: `{}`], error: `{}`}",
        query,
        value,
        exception
      );
      throw exception;
    }
  }
}
