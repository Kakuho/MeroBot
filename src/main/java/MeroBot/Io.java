package MeroBot;

import MeroBot.EmojiUtil;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.ImageFormat;
import net.dv8tion.jda.api.utils.FileUpload;

import java.net.URL;
import java.io.IOException;
import java.io.BufferedInputStream;

public class Io{
  static private final int KILOBYTES = 1024;

  static public byte[] DownloadEmojiGif(JDA jda, Guild sourceGuild, String emojiName){
    RichCustomEmoji emoji = EmojiUtil.GetEmoji(jda, sourceGuild, emojiName);
    if(emoji == null){
      return null;
    }
    String gifUrl = emoji.getImageUrl(ImageFormat.GIF);
    try(BufferedInputStream in = new BufferedInputStream(new URL(gifUrl).openStream())){
      byte dataBuffer[] = new byte[1000*KILOBYTES]; // we assume there is an maximum of 1000*KiloBytes for each gif
                                                    // file, might be useful to create a buffer cache
      int bytesRead = 0;
      int totalBytesRead = 0;
      while((bytesRead = in.read(dataBuffer, totalBytesRead, 200*KILOBYTES)) != -1){
        totalBytesRead += bytesRead;
      }
      return dataBuffer;
    }
    catch (IOException e) {
      return null;
    }
  }
}
