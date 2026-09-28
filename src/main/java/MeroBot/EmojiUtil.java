package MeroBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

public class EmojiUtil{
  static public RichCustomEmoji GetEmojiFromGuild(Guild guild, String emojiName){
    List<RichCustomEmoji> emojiList = guild.getEmojisByName(emojiName, false);
    if(emojiList.size() >= 1){
      return emojiList.get(0);
    }
    else{
      return null;
    }
  }

  static public boolean GuildHasEmoji(Guild guild, String emojiName){
    if(GetEmojiFromGuild(guild, emojiName) != null){
      return true;
    }
    else{
      return false;
    }
  }

  static public record GetEmojiPack(RichCustomEmoji emoji, Guild guild){}

  static public RichCustomEmoji GetEmojiFromOtherServers(JDA jda, String emojiName){
    List<Guild> guilds = jda.getGuilds();
    for(var guild: guilds){
      List<RichCustomEmoji> foundEmojis = guild.getEmojisByName(emojiName, false);
      if(foundEmojis.size() >= 1){
        return foundEmojis.get(0);
      }
    }
    return null;
  }

  static public GetEmojiPack GetEmojiFromOtherServersPack(JDA jda, String emojiName){
    // this one returns a pack, so you can query which server the emoji comes from if valid
    List<Guild> guilds = jda.getGuilds();
    for(var guild: guilds){
      List<RichCustomEmoji> foundEmojis = guild.getEmojisByName(emojiName, false);
      if(foundEmojis.size() >= 1){
        return new GetEmojiPack(foundEmojis.get(0), guild);
      }
    }
    return null;
  }


  static public RichCustomEmoji GetEmoji(JDA jda, Guild sourceGuild, String emojiName){
    // First tries to get the emoji from the sourceGuild, if it fails, it tries 
    // to get the emoji from other servers. 
    RichCustomEmoji emoji = null;
    emoji = GetEmojiFromGuild(sourceGuild, emojiName);
    if(emoji != null){
      return emoji;
    }
    emoji = GetEmojiFromOtherServers(jda, emojiName);
    if(emoji != null){
      return emoji;
    }
    return null;
  }
}
