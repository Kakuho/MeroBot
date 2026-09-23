package MeroBot;

import MeroBot.EmojiUtil;

import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;

import java.util.concurrent.CompletableFuture;

public class EmojiActions{
  static public CompletableFuture<Message> PostEmojiAsync(GuildMessageChannel channel, String emojiName){
    Guild guild = channel.getGuild();
    if(EmojiUtil.GuildHasEmoji(guild, emojiName)){
      RichCustomEmoji emoji = guild.getEmojisByName(emojiName, false).get(0);
      return channel.sendMessage(emoji.getFormatted()).submit();
    }
    else{
      RichCustomEmoji emoji = EmojiUtil.GetEmojiFromOtherServers(channel.getJDA(), emojiName);
      return channel.sendMessage(emoji.getFormatted()).submit();
    }
  }

  static public CompletableFuture<RichCustomEmoji> GetEmojiFromGuildAsync(Guild guild, String emojiName){
    return guild.retrieveEmojis().submit()
    .thenApply(
      (emojiList) -> {
        for(var emoji: emojiList){
          if(emoji.getName().equals(emojiName)){
            System.out.println(emoji.getName());
            return emoji;
          }
        }
        return null;
      }
    );
  }
}
