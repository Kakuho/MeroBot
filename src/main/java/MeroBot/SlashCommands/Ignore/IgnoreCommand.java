package MeroBot.SlashCommands.Ignore;

import MeroBot.Database.Repository.IgnoredUserRepository;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

// probably can put more info here 

public class IgnoreCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "ignore";
  private IgnoredUserRepository repo = new IgnoredUserRepository();


  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    event.deferReply().setEphemeral(true).queue();
    String userId = event.getMember().getId();
    var ignoredUser = repo.GetIgnoredUser(userId);
    if(ignoredUser == null){
      repo.AddIgnoredUser(userId);
      event.getHook().sendMessage("Mero will ignore your messages amero!")
                     .queue();
      return;
    }
    boolean newIgnore = !ignoredUser.GetIgnored();
    repo.SetIgnoredUser(userId, newIgnore);
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
