package MeroBot.SlashCommands;

import MeroBot.SlashCommands.RewriteMessageCommand;
import MeroBot.SlashCommands.SendGifCommand;
import MeroBot.SlashCommands.HelpCommand;
import MeroBot.SlashCommands.ReactCommand;
import MeroBot.SlashCommands.UseableEmojisCommand;
import MeroBot.SlashCommands.QueryEmojiCommand;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class CommandInstaller{
  static public JDABuilder Install(JDABuilder builder){
    return builder.addEventListeners(new RewriteMessageCommand())
                  .addEventListeners(new SendGifCommand())
                  .addEventListeners(new ReactCommand())
                  .addEventListeners(new UseableEmojisCommand())
                  .addEventListeners(new QueryEmojiCommand())
                  .addEventListeners(new HelpCommand());
  }

  static public void InstallCommandsAsync(JDA bot){
    bot.updateCommands().addCommands(
      Commands.slash(RewriteMessageCommand.COMMAND_NAME, "Rewrites the message")
              .addOption(OptionType.STRING, "message", "the message to rewrite", true),
      Commands.slash(SendGifCommand.COMMAND_NAME, "Makes a gif from an emoji")
              .addOption(OptionType.STRING, "emoji", "the name of the emoji to make a gif", true),
      Commands.slash(HelpCommand.COMMAND_NAME, "Sends a text regarding how to use the bot"),
      Commands.slash(ReactCommand.COMMAND_NAME, "Reacts to the message with the given emoji")
              .addOption(OptionType.STRING, "message_id", "the id of the message to react to", true)
              .addOption(OptionType.STRING, "emoji", "the name of the emoji to be used", true),
      Commands.slash(UseableEmojisCommand.COMMAND_NAME, "Send information regarding all useable emojis"),
      Commands.slash(QueryEmojiCommand.COMMAND_NAME, "Sends informationr regarding a emoji")
              .addOption(OptionType.STRING, "emoji", "the emoji to be queries", true)
    ).queue();
  }
}
