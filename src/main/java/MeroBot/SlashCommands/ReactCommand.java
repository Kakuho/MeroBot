package MeroBot.SlashCommands;

import MeroBot.EmojiUtil;
import MeroBot.WebhookUtil;
import MeroBot.WebhookActions;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

// Todo: make this command a toggle

public class ReactCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "react";

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    String messageId = event.getOption("message_id").getAsString();
    String emojiName = event.getOption("emoji").getAsString();
    RichCustomEmoji emoji = EmojiUtil.GetEmoji(event.getJDA(), event.getGuild(), emojiName);
    if(emoji == null){
      event.reply("Sorry... I could not find an emoji with that name").setEphemeral(true).queue();
      return;
    }
    MessageChannel channel = event.getMessageChannel();
    channel.getHistoryAround(messageId, 5).queue(
      (MessageHistory history) -> history.getMessageById(messageId).addReaction(emoji).queue(
        (v) -> event.reply("reacted to the message!").queue()
      )
    );
  }
}
