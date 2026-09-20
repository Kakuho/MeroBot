package MeroBot.SlashCommands.Ignore;

import MeroBot.SlashCommands.Ignore.IgnoreUtils;
import MeroBot.Database.Models.User;
import MeroBot.Database.Repository.UserRepository;
import MeroBot.Database.Repository.RoleRepository;

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

public class IgnoreUserCommand extends ListenerAdapter{
  public static final String COMMAND_NAME = "ignore_user";
  private static UserRepository trackedUserRepo = new UserRepository();
  private static final Logger logger = LoggerFactory.getLogger(IgnoreUserCommand.class);

  static private void DoReply(SlashCommandInteractionEvent event, String userId, boolean ignoreValue){
    if(ignoreValue == true){
      event.getHook().sendMessage("Mero will start ignoring that user mero!!").queue();
    }
    else{
      event.getHook().sendMessage("Mero will stop ignoring that user mero!!").queue();
    }
  }

  static private void HandleSqlException(SQLException e, SlashCommandInteractionEvent event){
    boolean dbConnectionFailure = false;
    if(e instanceof java.sql.SQLTransientConnectionException){
      dbConnectionFailure = true;
    }
    else if(e instanceof java.sql.SQLNonTransientConnectionException){
      dbConnectionFailure = true;
    }
    if(dbConnectionFailure){
      logger.error("{exception: '{}', comments: '{}'}",
        e,
        dbConnectionFailure ? "perhaps failed to connect to the database" : ""
      );
      event.getHook().sendMessage("Meroron failed to connect to the database mero, could the database be offline mero?")
                     .queue();
    }
  }

  private record ExistReturnValue(boolean exist, boolean numberFormatException){};

  ExistReturnValue MemberExist(SlashCommandInteractionEvent event, String memberId){
    try{
      if(event.getGuild().getMemberById(memberId) == null){
        return new ExistReturnValue(false, false);
      }
    }
    catch(NumberFormatException e){
      return new ExistReturnValue(false, true);
    }
    return new ExistReturnValue(true, false);
  }
  
  private enum FailureCondition{
    MemberExistError,      
    SnowflakeIdInvalid,
    AdminPrivilageError
  }

  static void HandleFailure(SlashCommandInteractionEvent event, FailureCondition condition, String userId){
    switch(condition){
      case MemberExistError:
        event.getHook().sendMessage("Sorry the user with the id " + userId + " does not exist mero!").queue();
        break;
      case SnowflakeIdInvalid:
        event.getHook().sendMessage("Sorry that id is invalid mero!").queue();
        break;
      case AdminPrivilageError:
        event.getHook().sendMessage("Sorry you cannot use that command, you're not an admin mero!").queue();
        break;
    }
    logger.error("{author_id: '{}', input: [userId: '{}'], reason: '{}'}",
      event.getMember().getId(),
      userId,
      condition.toString()
    );
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    event.deferReply().setEphemeral(true).queue();
    String userId = event.getOption("user_id").getAsString();
    // user id checking
    var exists = MemberExist(event, userId);
    if(exists.numberFormatException() == true){
      HandleFailure(event, FailureCondition.SnowflakeIdInvalid, userId);
      return;
    }
    else if(exists.exist() == false){
      HandleFailure(event, FailureCondition.MemberExistError, userId);
      return;
    }
    else{
      // Start of actual logic
      try{
        if(!IgnoreUtils.MemberIsAdmin(event.getMember())){
          HandleFailure(event, FailureCondition.AdminPrivilageError, userId);
          return;
        }
        boolean ignoredValue = true;
        boolean userAdded = false;
        User user = trackedUserRepo.GetUser(userId);
        if(user == null){
          ignoredValue = true;
          trackedUserRepo.AddUserAdmin(userId, true, event.getMember().getId());
          userAdded = true;
        }
        else{
          ignoredValue = !user.GetIgnored();
          trackedUserRepo.SetUserAdmin(userId, ignoredValue, event.getMember().getId());
          userAdded = false;
        }
        DoReply(event, userId, ignoredValue);
        logger.info("{author_id: '{}', input: [userId: '{}'], output: [ignored: '{}'], comments: '{}'}",
          event.getMember().getId(),
          userId,
          ignoredValue ? "ignored" : "unignored",
          userAdded ? "user added to database": "user already existed in database"
        );
      }
      catch(SQLException e){
        HandleSqlException(e, event);
      }
    }
  }
}
