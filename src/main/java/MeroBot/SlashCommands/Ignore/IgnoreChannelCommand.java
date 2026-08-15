package MeroBot.SlashCommands.Ignore;

import MeroBot.Database.Repository.IgnoredChannelRepository;
import MeroBot.Database.Repository.RoleRepository;
import MeroBot.Database.Models.IgnoredChannel;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.lang.NumberFormatException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Permissions: Admin only

public class IgnoreChannelCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "ignore_channel";
  private static IgnoredChannelRepository ignoredChannelRepo = new IgnoredChannelRepository();
  private static RoleRepository roleRepo = new RoleRepository();
  private static final Logger logger = LoggerFactory.getLogger(IgnoreChannelCommand.class);

  static private boolean MemberIsAdmin(Member member) throws SQLException{
    // there really should be a more efficient way for this other than the O(n) loop...
    try{
      List<Role> roles = member.getRoles();
      for(Role role: roles){
        if(roleRepo.IsRoleAdmin(role.getName())){
          return true;
        }
      }
      return false;
    }
    catch(SQLException e){
      throw e;
    }
  }

  static private void DoReply(SlashCommandInteractionEvent event, String channelId, boolean ignoreValue){
    String channelName = event.getGuild().getGuildChannelById(channelId).getName();
    if(ignoreValue == true){
      event.getHook().sendMessage("Mero will start ignoring channel " + channelName + " mero!!").queue();
    }
    else{
      event.getHook().sendMessage("Mero will stop ignoring channel " + channelName + " mero!!").queue();
    }
  }

  void HandleSqlException(SQLException e, SlashCommandInteractionEvent event){
    if(e instanceof java.sql.SQLTransientConnectionException){
      event.getHook().sendMessage("Meroron failed to connect to the database mero, could the database be offline mero?")
                     .queue();
    }
    else if(e instanceof java.sql.SQLNonTransientConnectionException){
      event.getHook().sendMessage("Meroron failed to connect to the database mero, could the database be offline mero?")
                     .queue();
    }
  }

  /*
  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    event.deferReply().setEphemeral(true).queue();
    String channelId = event.getOption("channel_id").getAsString();
    // input channel_id checking
    try{
      if(event.getGuild().getGuildChannelById(channelId) == null){
        event.getHook().sendMessage("Sorry the channel with the id " + channelId + " does not exist mero!")
        .queue();
        return;
      }
    }
    catch(NumberFormatException e){
        event.getHook().sendMessage("Sorry that id is invalid mero!").queue();
        return;
    }
    // Start of command logic
    try{
      if(!MemberIsAdmin(event.getMember())){
        // should this be logged so admins can see if there's anyone suspiciously using this command?
        event.getHook().sendMessage("Sorry you cannot use that command, you're not an admin mero!").queue();
        return;
      }
      boolean ignoredValue = true;
      IgnoredChannel channel = ignoredChannelRepo.GetIgnoredChannel(channelId);
      if(channel == null){
        ignoredValue = true;
        ignoredChannelRepo.AddIgnoredChannel(channelId);
      }
      else{
        ignoredValue = !channel.GetIgnored();
        ignoredChannelRepo.SetIgnoredChannel(channelId, ignoredValue);
      }
      DoReply(event, channelId, ignoredValue);
    }
    catch(SQLException e){
      HandleSqlException(e, event);
    }
  }
  */

  private record ChannelExistReturnValue(boolean numberException, boolean exists){}

  static private ChannelExistReturnValue ChannelExists(SlashCommandInteractionEvent event, String channelId){
    try{
      if(event.getGuild().getGuildChannelById(channelId) == null){
        event.getHook().sendMessage("Sorry the channel with the id " + channelId + " does not exist mero!")
        .queue();
        return new ChannelExistReturnValue(false, false);
      }
    }
    catch(NumberFormatException e){
        event.getHook().sendMessage("Sorry that id is invalid mero!").queue();
        return new ChannelExistReturnValue(true, false);
    }
    return new ChannelExistReturnValue(false, true);
  }

  private enum FailureCondition{
    ChannelExistError,      
    SnowflakeIdInvalid,
    AdminPrivilageError
  }

  static private void HandleFailure(SlashCommandInteractionEvent event, FailureCondition condition, String channelId){
    switch(condition){
      case ChannelExistError:
        event.getHook().sendMessage("Sorry the user with the id " + channelId + " does not exist mero!").queue();
        break;
      case SnowflakeIdInvalid:
        event.getHook().sendMessage("Sorry that id is invalid mero!").queue();
        break;
      case AdminPrivilageError:
        event.getHook().sendMessage("Sorry you cannot use that command, you're not an admin mero!").queue();
        break;
    }
    logger.error("{author_id: '{}', input: [channelId: '{}'], reason: '{}'}",
      event.getMember().getId(),
      channelId,
      condition.toString()
    );
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    event.deferReply().setEphemeral(true).queue();
    String channelId = event.getOption("channel_id").getAsString();
    // input channel_id checking
    var channelExist = ChannelExists(event, channelId);
    if(channelExist.numberException()){
      HandleFailure(event, FailureCondition.SnowflakeIdInvalid, channelId);
    }
    else if(!channelExist.exists()){
      HandleFailure(event, FailureCondition.ChannelExistError, channelId);
    }
    else{
      // Start of command logic
      try{
        if(!MemberIsAdmin(event.getMember())){
          HandleFailure(event, FailureCondition.AdminPrivilageError, channelId);
          return;
        }
        boolean ignoredValue = true;
        boolean channelAdded = false;
        IgnoredChannel channel = ignoredChannelRepo.GetIgnoredChannel(channelId);
        if(channel == null){
          ignoredValue = true;
          ignoredChannelRepo.AddIgnoredChannel(channelId);
          channelAdded = true;
        }
        else{
          ignoredValue = !channel.GetIgnored();
          ignoredChannelRepo.SetIgnoredChannel(channelId, ignoredValue);
        }
        DoReply(event, channelId, ignoredValue);
        logger.info("{author_id: '{}', input: [channelId: '{}'], output: [ignored: '{}'], comments: '{}'}",
          event.getMember().getId(),
          channelId,
          ignoredValue ? "ignored" : "unignored",
          channelAdded ? "channel added to database": "channel already existed in database"
        );
      }
      catch(SQLException e){
        HandleSqlException(e, event);
      }
    }
  }
}
