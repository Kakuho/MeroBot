package MeroBot.SlashCommands.Ignore;

import MeroBot.Database.Repository.TrackedUserRepository;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

// probably can put more info here 

public class TrackEmojisCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "track_emojis";
  private TrackedUserRepository repo = new TrackedUserRepository();
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
      var user = repo.GetTrackedUser(userId);
      boolean ignoredValue = false;
      boolean userExists = false;
      if(user == null){
        userExists = false;
        repo.AddTrackedUser(userId, false);
        DoReply(ignoredValue, event);
      }
      else{
        userExists = true;
        ignoredValue = !user.GetIgnored();
        repo.SetTrackedUser(userId, ignoredValue);
        DoReply(ignoredValue, event);
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
