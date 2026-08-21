package MeroBot.SlashCommands;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UseableEmojisCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "useable_emojis";
  static private final Logger logger = LoggerFactory.getLogger(UseableEmojisCommand.class);

  static private String FormatEmoji(RichCustomEmoji emoji){
    return  emoji.getFormatted() + "  from " + emoji.getGuild().getName();
  }

  static private CompletableFuture<String> GetEmojiListAsync(Guild guild){
    return guild.retrieveEmojis().submit()
      .thenApply( (List<RichCustomEmoji> emojis) ->{
        String content = "";
        for(RichCustomEmoji emoji: emojis){
          content = content.concat(FormatEmoji(emoji) + '\n');
        }
        return content;
      }
    );
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    List<CompletableFuture<String>> futures = new ArrayList<CompletableFuture<String>>();
    List<Guild> guilds = event.getJDA().getGuilds(); 
    for(Guild guild: guilds){
      futures.add(GetEmojiListAsync(guild));
    }

    CompletableFuture<String> combined = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
      .thenApply(v ->
        futures.stream()
          .map(CompletableFuture::join)
          .collect(Collectors.joining())
      );

    combined.thenAccept((list) -> {
      logger.info("{author_id: '{}', output: [sent: 'true']",
          event.getMember().getId()
      );
      event.reply(list).queue();
    });
  }
}
