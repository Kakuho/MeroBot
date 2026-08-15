package MeroBot.SlashCommands.Ignore;

import MeroBot.Database.Repository.IgnoredUserRepository;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

// probably can put more info here 

public class IgnoreCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "ignore";
  private IgnoredUserRepository repo = new IgnoredUserRepository();
  final Logger logger = LoggerFactory.getLogger(IgnoreCommand.class);

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
      event.getHook().sendMessage("Mero will ignore your messages mero!")
                     .queue();
    }
    else{
      event.getHook().sendMessage("Mero will start reposting your messages mero")
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
      var ignoredUser = repo.GetIgnoredUser(userId);
      boolean ignoredValue = true;
      boolean userExists = false;
      if(ignoredUser == null){
        userExists = false;
        repo.AddIgnoredUser(userId);
        DoReply(ignoredValue, event);
      }
      else{
        userExists = true;
        ignoredValue = !ignoredUser.GetIgnored();
        repo.SetIgnoredUser(userId, ignoredValue);
        DoReply(ignoredValue, event);
      }
      logger.info("{input: [userId: '{}'], output: '{}', comments: '{}'}",
          userId, 
          ignoredValue ? "ignored" : "unignored",
          userExists ? "user already in database": "user added to database"
      );
    }
    catch(SQLException e){
      HandleSqlException(e, event);
    }
  }
}
