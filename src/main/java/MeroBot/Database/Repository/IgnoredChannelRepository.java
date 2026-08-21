package MeroBot.Database.Repository;

import MeroBot.Database.MeroDatabase;
import MeroBot.Database.Models.IgnoredChannel;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// probably should be made into a generic repository class to avoid repeating code...

public class IgnoredChannelRepository{
  final Logger logger = LoggerFactory.getLogger(TrackedUserRepository.class);

  public IgnoredChannelRepository(){

  }

  private IgnoredChannel ExtractIgnoredChannel(PreparedStatement pst) throws SQLException{
    try(ResultSet rs = pst.executeQuery()){
      if(!rs.next()){
        return null;
      }
      IgnoredChannel channel = new IgnoredChannel(
          rs.getString(1),
          rs.getBoolean(2),
          rs.getDate(3)
      );
      return channel;
    }
    catch(SQLException exception){
      throw exception;
    }
  }

  public boolean AddIgnoredChannel(String channelId) throws SQLException{
    String query = "insert into ignored_channel(channel_id, ignored) values(?, 'true');";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setString(1, channelId);
      if(pst.executeUpdate() >= 1){
        return true;
      }
      else{
        return false;
      }
    }
    catch(SQLException exception){
      logger.error("input: [query: `{}`, arg1: `{}`], error: `{}`}",
        query,
        channelId,
        exception
      );
      throw exception;
    }
  }

  public IgnoredChannel GetIgnoredChannel(String channelId) throws SQLException{
    String query = "select * from ignored_channel where channel_id = ?;";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setString(1, channelId);
      return ExtractIgnoredChannel(pst);
    }
    catch(SQLException exception){
      logger.error("input: [query: `{}`, arg1: `{}`], error: `{}`}",
        query,
        channelId,
        exception
      );
      throw exception;
    }
  }

  public boolean SetIgnoredChannel(String channelId, boolean ignored) throws SQLException{
    String query = "update ignored_channel set ignored = ? where channel_id = ?;";
    try(Connection con = MeroDatabase.GetConnection();
        PreparedStatement pst = con.prepareStatement(query);
    ){
      pst.setBoolean(1, ignored);
      pst.setString(2, channelId);
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
        channelId,
        exception
      );
      throw exception;
    }
  }
}
