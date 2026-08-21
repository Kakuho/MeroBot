package MeroBot.SlashCommands;

import MeroBot.EmojiUtil;
import MeroBot.WebhookUtil;
import MeroBot.WebhookActions;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.attribute.IWebhookContainer;
import net.dv8tion.jda.api.utils.ImageFormat;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendGifCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "sendgif";
  static private final Logger logger = LoggerFactory.getLogger(SendGifCommand.class);

  enum SendGifError{
    EmojiNotFound,
    EmojiNotAnimated,
    ChannelNotWebhookContainerError
  }

  static void HandleFailure(SlashCommandInteractionEvent event, SendGifError condition, String emojiName, String channelId){
    switch(condition){
      case EmojiNotFound:
        event.getHook().sendMessage("Sorry... I could not find an emoji with that name mero").setEphemeral(true).queue();
        break;
      case EmojiNotAnimated:
        event.getHook().sendMessage("Sorry... only animated emojis can be made into gifs mero").setEphemeral(true).queue();
        break;
      case ChannelNotWebhookContainerError:
        event.getHook().sendMessage("Sorry... cant send the gif in this channel!").setEphemeral(true).queue();
        break;
    }
    logger.error("{author_id: '{}', input: [emojiName: '{}', channelId: '{}'], output: [sent: 'false'], reason: '{}'}",
      event.getMember().getId(),
      emojiName,
      channelId,
      condition.toString()
    );
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    event.deferReply().setEphemeral(true).queue();
    String emojiName = event.getOption("emoji").getAsString();
    RichCustomEmoji emoji = EmojiUtil.GetEmoji(event.getJDA(), event.getGuild(), emojiName);
    var channel = event.getChannel();
    // command guard
    if(emoji == null){
      HandleFailure(event, SendGifError.EmojiNotFound, emojiName, channel.getId());
      return;
    }
    if(emoji.isAnimated() != true){
      HandleFailure(event, SendGifError.EmojiNotAnimated, emojiName, channel.getId());
      return;
    }
    IWebhookContainer container = WebhookUtil.ConvertChannelToWebhookContainer(channel);
    if(container == null){
      HandleFailure(event, SendGifError.ChannelNotWebhookContainerError, emojiName, channel.getId());
      return;
    }
    // actual logic starts here
    String gifUrl = emoji.getImageUrl(ImageFormat.GIF);
    var member = event.getMember();
    CompletableFuture<Message> future = WebhookActions.SendMessageAsMemberAsync(container, member, gifUrl);
    future.whenComplete( (message, error) -> {
      if(error != null){
        event.reply("Sorry... something went wrong sending the gif").setEphemeral(true).queue();
        // catch all error
        logger.error("{author_id: '{}', input: [emojiName: '{}', channelId: '{}'], output: [sent: 'false'], reason: '{}'}",
          event.getMember().getId(),
          emojiName,
          channel.getId(),
          error
        );
        return;
      }
      else{
        event.reply("gif sent").setEphemeral(true).queue();
        logger.info("{author_id: '{}', input: [emojiName: '{}', channelId: '{}'], output: [sent: 'true'], reason: '{}'}",
          event.getMember().getId(),
          emojiName,
          channel.getId(),
          error
        );
        return;
      }
    });
  }
}
