package MeroBot.SlashCommands;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// rewrites the user's message, repost the message with the user's profile picture, display name and bio

public class HelpCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "help";
  static private final Logger logger = LoggerFactory.getLogger(HelpCommand.class);

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    event.reply("To use merobot's reposting capabilities, simply write an emoji like so `:emoji:`. You can post emojis from servers which im in and animated emojis Mero :pink_heart:!").queue();
    logger.info("{author_id: '{}' outcome: 'success'}",
      event.getMember().getId()
    );
  }
}

