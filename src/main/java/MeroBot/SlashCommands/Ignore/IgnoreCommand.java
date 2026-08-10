package MeroBot.SlashCommands.Ignore;

import MeroBot.Database.Repository.IgnoredUserRepository;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import com.zaxxer.hikari.pool.HikariPool;

import java.lang.ExceptionInInitializerError;

// probably can put more info here 

public class IgnoreCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "ignore";
  private IgnoredUserRepository repo = new IgnoredUserRepository();

  void HandleInitialisationException(ExceptionInInitializerError e, SlashCommandInteractionEvent event){
    if(e.getCause() instanceof HikariPool.PoolInitializationException){
      event.getHook().sendMessage("Meroron failed to connect to the database mero")
                     .queue();
    }
    else{
      event.getHook().sendMessage("Meroron ran into an error mero")
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
      if(ignoredUser == null){
        repo.AddIgnoredUser(userId);
        DoReply(ignoredValue, event);
      }
      else{
        ignoredValue = !ignoredUser.GetIgnored();
        repo.SetIgnoredUser(userId, ignoredValue);
        DoReply(ignoredValue, event);
      }
    }
    catch(ExceptionInInitializerError e){
      HandleInitialisationException(e, event);
    }
  }
}
