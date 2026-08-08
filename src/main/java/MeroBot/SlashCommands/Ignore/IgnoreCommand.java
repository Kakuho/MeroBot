package MeroBot.SlashCommands.Ignore;

import MeroBot.Database.MeroDatabase;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

// probably can put more info here 

public class IgnoreCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "ignore";

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    event.deferReply().setEphemeral(true).queue();
    String userId = event.getMember().getId();
    System.out.println("id: " + userId);
    var ignoredUser = MeroDatabase.GetIgnoredUser(userId);
    if(ignoredUser == null){
      System.out.println("user was null");
      MeroDatabase.AddIgnoredUser(userId);
      event.getHook().sendMessage("Mero will ignore your messages amero!")
                     .queue();
      return;
    }
    boolean newIgnore = !ignoredUser.ignored();
    MeroDatabase.SetIgnoredUser(userId, newIgnore);
    if(newIgnore == true){
      event.getHook().sendMessage("Mero will ignore your messages mero!")
                     .queue();
    }
    else{
      event.getHook().sendMessage("Mero will start reposting your messages mero")
                     .queue();
    }
  }
}
