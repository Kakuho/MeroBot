package MeroBot.SlashCommands;

import MeroBot.EmojiUtil;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;

// probably can put more info here 

public class QueryEmojiCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "query_emoji";

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    String emojiName = event.getOption("emoji").getAsString();
    RichCustomEmoji emoji = EmojiUtil.GetEmoji(event.getJDA(), event.getGuild(), emojiName);
    if(emoji == null){
      event.reply("Sorry... The emoji with name " + emojiName + " cannot be found...").queue();
    }
    else{
      event.reply(emojiName + " " + emoji.getFormatted() + " info: from server " + emoji.getGuild().getName()).queue();
    }
  }
}
