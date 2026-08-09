package MeroBot.Database.Repository;

import MeroBot.Database.MeroDatabase;
import MeroBot.Database.Models.Role;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class RoleRepository{
  public RoleRepository(){

  }

  public boolean AddRole(String value, boolean isAdmin){
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
      System.out.println("AddRole - SQL EXCEPTION: " + exception);
      return false;
    }
  }

  private Role ExtractRole(PreparedStatement pst){
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
      System.out.println("ExtractRole - SQL EXCEPTION: " + exception);
      return null;
    }
  }

  public Role GetRole(String value){
    String query = "select * from role where value = ?;";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setString(1, value);
      return ExtractRole(pst);
    }
    catch(SQLException exception){
      System.out.println("GetRole - SQL EXCEPTION: " + exception);
      return null;
    }
  }

  public boolean SetRoleAdmin(String value, boolean isAdmin){
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
      System.out.println("SetRoleAdmin - SQL EXCEPTION: " + exception);
      return false;
    }
  }

}
