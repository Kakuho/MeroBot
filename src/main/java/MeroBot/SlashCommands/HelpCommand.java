package MeroBot.SlashCommands;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

// rewrites the user's message, repost the message with the user's profile picture, display name and bio

public class HelpCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "help";

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    var channel = event.getChannel();
    event.reply("To use merobot's reposting capabilities, simply write an emoji like so `:emoji:`. You can post emojis from servers which im in and animated emojis Mero :pink_heart:!").queue();
  }
}

