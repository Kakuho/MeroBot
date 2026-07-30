package MeroBot;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

class EmojiUtil{
  RichCustomEmoji GetEmojiFromOtherServers(JDA jda, String emojiName){
    List<Guild> guilds = jda.getGuilds();
    for(var guild: guilds){
      List<RichCustomEmoji> foundEmojis = guild.getEmojisByName(emojiName, false);
      if(foundEmojis.size() >= 1){
        return foundEmojis.get(0);
      }
    }
    return null;
  }
}
