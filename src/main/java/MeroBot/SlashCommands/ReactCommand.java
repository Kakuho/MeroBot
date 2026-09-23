package MeroBot.SlashCommands;

import MeroBot.EmojiUtil;
import MeroBot.EmojiActions;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;

import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletionException;

//  Todo: make this command a toggle
//        make this command able to be used in any channel allowing webhooks
//
//  regarding logging:  check messageid and emojiName to see if they're valid

public class ReactCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "react";
  static private final Logger logger = LoggerFactory.getLogger(ReactCommand.class);

  enum ReactCommandError{
    EmojiNotFound,
  }

  static void HandleFailure(SlashCommandInteractionEvent event, ReactCommandError condition, String messageId, String emojiName){
    switch(condition){
      case EmojiNotFound:
        event.getHook().sendMessage("Sorry... I could not find an emoji with that name mero").setEphemeral(true).queue();
        break;
    }
    logger.error("{author_id: '{}', input: [messageId: '{}', emojiName: '{}'], output: [sent: 'false'], reason: '{}'}",
      event.getMember().getId(),
      messageId,
      emojiName,
      condition.toString()
    );
  }

  static private void HandleViaCache(SlashCommandInteractionEvent event){
    String messageId = event.getOption("message_id").getAsString();
    String emojiName = event.getOption("emoji").getAsString();
    RichCustomEmoji emoji = EmojiUtil.GetEmoji(event.getJDA(), event.getGuild(), emojiName);
    if(emoji == null){
      event.reply("Sorry... I could not find an emoji with that name").setEphemeral(true).queue();
      HandleFailure(event, ReactCommandError.EmojiNotFound,  messageId, emojiName);
      return;
    }
    else{
      MessageChannel channel = event.getMessageChannel();
      channel.getHistoryAround(messageId, 5).queue(
        (MessageHistory history) -> {
          history.getMessageById(messageId).addReaction(emoji).queue(
            (v) -> event.reply("reacted to the message!").setEphemeral(true).queue()
          );
          logger.info("{author_id: '{}', input: [messageId: '{}', emojiName: '{}', channelId: '{}'], output: [reacted: 'true']}",
            event.getMember().getId(),
            messageId,
            emojiName,
            channel.getId()
          );
        },
        (error) -> {
          event.reply("something went wrong reacting to the message mero").setEphemeral(true).queue();
          logger.error("{author_id: '{}', input: [messageId: '{}', emojiName: '{}', channelId: '{}'], output: [reacted: 'false'], reason: '{}'}",
            event.getMember().getId(),
            messageId,
            emojiName,
            channel.getId(),
            error
          );
        }
      );
    }
  }

  static private void HandleViaFuture(SlashCommandInteractionEvent event){
    String messageId = event.getOption("message_id").getAsString();
    String emojiName = event.getOption("emoji").getAsString();
    EmojiActions.GetEmojiAsyncV2(event.getJDA(), event.getGuild(), emojiName)
    .whenComplete((emoji, e) -> {
      if(e != null){
        throw new CompletionException(e);
      }
      if(emoji == null){
        event.reply("Sorry... I could not find an emoji with that name").setEphemeral(true).queue();
        HandleFailure(event, ReactCommandError.EmojiNotFound,  messageId, emojiName);
      }
      else{
        MessageChannel channel = event.getMessageChannel();
        channel.getHistoryAround(messageId, 5).queue(
          (MessageHistory history) -> {
            history.getMessageById(messageId).addReaction(emoji).queue(
              (v) -> event.reply("reacted to the message!").setEphemeral(true).queue()
            );
            logger.info("{author_id: '{}', input: [messageId: '{}', emojiName: '{}', channelId: '{}'], output: [reacted: 'true']}",
              event.getMember().getId(),
              messageId,
              emojiName,
              channel.getId()
            );
          },
          (error) -> {
            event.reply("something went wrong reacting to the message mero").setEphemeral(true).queue();
            logger.error("{author_id: '{}', input: [messageId: '{}', emojiName: '{}', channelId: '{}'], output: [reacted: 'false'], reason: '{}'}",
              event.getMember().getId(),
              messageId,
              emojiName,
              channel.getId(),
              error
            );
          }
        );
      }
    });
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    HandleViaFuture(event);
  }
}
