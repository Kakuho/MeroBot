package MeroBot.SlashCommands.Ignore;

import MeroBot.SlashCommands.Ignore.IgnoreUtils;
import MeroBot.Database.Repository.UserRepository;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

// Track Emoji is a toggle slash command, so if you want to be tracked you use the command, 
// and if you dont want to be tracked you use the command again
//
// by default a user can track and untrack themselves.
// however, if an admin user has chosen to ignore a user,,, users cannot bypass the admin.
//
// There are 2 cases:
//  either the admin has ignored the user or they have not
//
//  if an admin has ignored a user (and they are thus in a untracked state), any usage of this command fails for the
//  user
//
//  if an admin has not ignored a user, user.adminIgnored is false, and the user is freely able to track and untrack
//  themselves
//
// admin users can freely track and untrack themselves

public class TrackEmojisCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "track_emojis";
  private UserRepository repo = new UserRepository();
  final Logger logger = LoggerFactory.getLogger(TrackEmojisCommand.class);

  void HandleSqlException(SQLException e, SlashCommandInteractionEvent event){
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

  void DoReply(boolean ignoredValue, SlashCommandInteractionEvent event){
    if(ignoredValue == true){
      event.getHook().sendMessage("Mero will stop tracking your messages mero!")
                     .queue();
    }
    else{
      event.getHook().sendMessage("Mero will start tracking your emojis mero")
                     .queue();
    }
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    try{
      if(!event.getName().equals(COMMAND_NAME)){
        return;
      }
      event.deferReply().setEphemeral(true).queue();
      String userId = event.getMember().getId();
      var user = repo.GetUser(userId);
      boolean ignoredValue = false;
      boolean userExists = false;
      if(user == null){
        userExists = false;
        repo.AddUser(userId, false);
        DoReply(ignoredValue, event);
      }
      else{
        userExists = true;
        ignoredValue = !user.GetIgnored();
        boolean adminIgnored = user.GetAdminIgnored();
        if(adminIgnored == true && !IgnoreUtils.MemberIsAdmin(event.getMember())){
          // then you cant start tracking yourself, you need an admin to do it first
          logger.error("{input: [userId: '{}'], output: '{}', comments: '{}'}",
              userId, 
              "not tracking",
              "user lacks privilage for this"
          );
          return;
        }
        else{
          // either admin ignored is false, or we're an admin
          repo.SetUser(userId, ignoredValue);
          DoReply(ignoredValue, event);
        }
      }
      logger.info("{input: [userId: '{}'], output: '{}', comments: '{}'}",
          userId, 
          ignoredValue ? "not tracking" : "tracking",
          userExists ? "user already in database": "user added to database"
      );
    }
    catch(SQLException e){
      HandleSqlException(e, event);
    }
  }
}
