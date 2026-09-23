package MeroBot.SlashCommands;

import MeroBot.EmojiUtil;
import MeroBot.EmojiActions;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// probably can put more info here 

public class QueryEmojiCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "query_emoji";
  static private final Logger logger = LoggerFactory.getLogger(QueryEmojiCommand.class);

  void LogResult(SlashCommandInteractionEvent event, String emojiName, RichCustomEmoji emoji){
    boolean emojiFound = emoji != null;
    if(!emojiFound){
      logger.error("{author_id: '{}', input: [emojiName: '{}'], output: [found: '{}']}",
        event.getMember().getId(),
        emojiName,
        "false"
      );
    }
    else{
      logger.info("{author_id: '{}', input: [emojiName: '{}'], output: [found: '{}', from_server: '{}']}",
        event.getMember().getId(),
        emojiName,
        "true",
        emoji.getGuild().getName()
      );
    }
  }

  private void DoViaCache(SlashCommandInteractionEvent event){
    String emojiName = event.getOption("emoji").getAsString();
    RichCustomEmoji emoji = EmojiUtil.GetEmoji(event.getJDA(), event.getGuild(), emojiName);
    if(emoji == null){
      event.reply("Sorry... The emoji with name " + emojiName + " cannot be found...").queue();
    }
    else{
      event.reply(emojiName + " " + emoji.getFormatted() + " info: from server " + emoji.getGuild().getName()).queue();
    }

    LogResult(event, emojiName, emoji);
  }

  private void DoViaFuture(SlashCommandInteractionEvent event){
    String emojiName = event.getOption("emoji").getAsString();
    CompletableFuture<RichCustomEmoji> future = EmojiActions.GetEmojiFromGuildAsync(event.getGuild(), emojiName);
    try{
      RichCustomEmoji emoji = future.get();
      if(emoji == null){
        event.reply("Sorry... The emoji with name " + emojiName + " cannot be found...").queue();
      }
      else{
        event.reply(emojiName + " " + emoji.getFormatted() + " info: from server " + emoji.getGuild().getName()).queue();
      }
    }
    catch(InterruptedException e){
    }
    catch(ExecutionException e){
    }
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    DoViaFuture(event);
    //DoViaCache(event);
  }
}
