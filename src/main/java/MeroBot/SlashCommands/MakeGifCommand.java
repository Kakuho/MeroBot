package MeroBot.SlashCommands;

import MeroBot.WebhookActions;
import MeroBot.EmojiUtil;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.utils.ImageFormat;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;

public class MakeGifCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "makegif";

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
    // would it be better to avoid writing the bytes to disk keep it in ram?
    try{
      InputStream in = new URL(gifUrl).openStream();
      Files.copy(in, Paths.get("badonka.gif"), StandardCopyOption.REPLACE_EXISTING);
      event.replyFiles(FileUpload.fromData(Paths.get("badonka.gif"))).queue();
    }
    catch(IOException e){
      event.reply("Sorry... Something went wrong when trying to download the image").setEphemeral(true).queue();
    }
  }
}

