package MeroBot.SlashCommands;

import MeroBot.WebhookActions;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Member;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// rewrites the user's message, repost the message with the user's profile picture, display name and bio

public class RewriteMessageCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "rewrite_message";
  static private final Logger logger = LoggerFactory.getLogger(RewriteMessageCommand.class);

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    Member member = event.getMember();
    String message = event.getOption("message").getAsString();
    WebhookActions.CreateWebhookIfNotOwnAsync(event.getChannel().asTextChannel())
    .thenCompose( (webhook) -> 
      WebhookActions.SendMessageAsMemberAsync(
          event.getChannel().asTextChannel(), 
          member, 
          message
      )
    )
    .whenComplete( (createdMessage, error) ->{
      if(error != null){
          event.reply("Sorry... something went wrong rewriting the message").setEphemeral(true).queue();
          logger.error("{author_id: '{}', input: [message: '{}', channelId: '{}'], output: [sent: 'false'], reason: '{}'}",
            event.getMember().getId(),
            message,
            event.getChannel().getId(),
            error.getMessage(),
            error
          );
      }
      else{
        event.reply("Rewrite Message Finished").setEphemeral(true).queue();
        logger.info("{author_id: '{}', input: [message: '{}', channelId: '{}'], output: [sent: 'true']}",
          event.getMember().getId(),
          message,
          event.getChannel().getId()
        );
      }
    });
  }
}

