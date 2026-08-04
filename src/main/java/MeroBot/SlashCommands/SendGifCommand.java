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
    Guild guild = event.getGuild();
    String emojiName = event.getOption("emoji").getAsString();
    RichCustomEmoji emoji = EmojiUtil.GetEmojiFromGuild(guild, emojiName);
    if(emoji == null){
      event.reply("Sorry... I could not find an emoji with that name").setEphemeral(true).queue();
      return;
    }
    String gifUrl = emoji.getImageUrl(ImageFormat.GIF);
    event.reply(gifUrl).queue();
  }
}
