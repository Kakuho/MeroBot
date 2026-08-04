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

public class SendGifCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "sendgif";

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    String emojiName = event.getOption("emoji").getAsString();
    RichCustomEmoji emoji = EmojiUtil.GetEmoji(event.getJDA(), event.getGuild(), emojiName);
    if(emoji == null){
      event.reply("Sorry... I could not find an emoji with that name").setEphemeral(true).queue();
      return;
    }
    if(emoji.isAnimated() != true){
      event.reply("Sorry... the emoji is not animated").setEphemeral(true).queue();
      return;
    }
    String gifUrl = emoji.getImageUrl(ImageFormat.GIF);
    var member = event.getMember();
    var channel = event.getChannel();
    IWebhookContainer container = WebhookUtil.ConvertChannelToWebhookContainer(channel);
    if(container == null){
      event.reply("Sorry... cant send the gif in this channel!").setEphemeral(true).queue();
    }
    CompletableFuture<Message> future = WebhookActions.SendMessageAsMemberAsync(container, member, gifUrl);
    future.whenComplete( (message, error) -> {
      if(error != null){
        event.reply("Sorry... something went wrong sending the gif").setEphemeral(true).queue();
        return;
      }
      event.reply("gif sent").setEphemeral(true).queue();
      return;
    });
  }
}
