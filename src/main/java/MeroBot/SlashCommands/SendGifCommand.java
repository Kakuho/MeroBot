package MeroBot.SlashCommands;

import MeroBot.EmojiUtil;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.utils.ImageFormat;

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
    event.reply(gifUrl).queue();
  }
}
