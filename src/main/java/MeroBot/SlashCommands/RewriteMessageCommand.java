package MeroBot.SlashCommands;

import MeroBot.WebhookActions;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Member;

// rewrites the user's message, repost the message with the user's profile picture, display name and bio

public class RewriteMessageCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "rewrite_message";

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    User user = event.getUser();
    Member member = event.getGuild().getMemberById(user.getId());
    String message = event.getOption("message").getAsString();
    WebhookActions.SendMessageAsMemberAsync(
        event.getChannel().asTextChannel(), 
        member, 
        message
    )
    .whenComplete( (createdMessage, error) ->{
      if(error != null){
      }
      else{
        System.out.println("Rewrote message");
        event.reply("Rewrite Message Finished").queue();
      }
    });
  }
}

