package MeroBot;

import MeroBot.EmojiUtil;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletionException;

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

  static public CompletableFuture<RichCustomEmoji> GetEmojiAsync(JDA jda, Guild sourceGuild, String emojiName){
    // * spawn a new thread
    // * for each guild, retrieve the list of emojis, blocking on each one
    // * for the emojis, check name
    CompletableFuture<RichCustomEmoji> emojiFut = new CompletableFuture<>();
    new Thread(() -> {
      List<Guild> guilds = jda.getGuilds();
      for(Guild guild: guilds){
        try{
          List<RichCustomEmoji> emojis = guild.retrieveEmojis().submit().get();
          for(var emoji: emojis){
            if(emoji.getName().equals(emojiName)){
              emojiFut.complete(emoji);
            }
          }
        }
        catch(InterruptedException e){
          emojiFut.completeExceptionally(e);
        }
        catch(ExecutionException e){
          emojiFut.completeExceptionally(e);
        }
      }
    });
    return emojiFut;
  }

  static public CompletableFuture<RichCustomEmoji> GetEmojiAsyncV2(JDA jda, Guild sourceGuild, String emojiName){
    List<CompletableFuture<List<RichCustomEmoji>>> emojiFut = new ArrayList<>();
    // add the source guild first
    emojiFut.add(sourceGuild.retrieveEmojis().submit());
    List<Guild> guilds = jda.getGuilds();                             // GUILD CACHE - IDK IF JDA DOCUMENTS THE CONSISTENCY
    for(Guild guild: guilds){
      emojiFut.add(guild.retrieveEmojis().submit());
    }
    var allof = CompletableFuture.allOf(emojiFut.toArray(new CompletableFuture[0]));

    return allof.thenApply((ignored) ->{
        for(var ef: emojiFut){
          try{
            List<RichCustomEmoji> emojiList = ef.get();
            for(var emoji: emojiList){
              if(emoji.getName().equals(emojiName)){
                return emoji;
              }
            }
          }
          catch(InterruptedException e){
            throw new CompletionException(e);
          }
          catch(ExecutionException e){
            throw new CompletionException(e);
          }
        }
        return null;
      }
    );
  }
}
