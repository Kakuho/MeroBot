package MeroBot.SlashCommands;

import MeroBot.SlashCommands.RewriteMessageCommand;
import MeroBot.SlashCommands.SendGifCommand;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class CommandInstaller{
  static public JDABuilder Install(JDABuilder builder){
    return builder.addEventListeners(new RewriteMessageCommand())
                  .addEventListeners(new SendGifCommand());

  }

  static public void InstallCommandsAsync(JDA bot){
    bot.updateCommands().addCommands(
      Commands.slash(RewriteMessageCommand.COMMAND_NAME, "Rewrites the message")
              .addOption(OptionType.STRING, "message", "the message to rewrite", true),
      Commands.slash(SendGifCommand.COMMAND_NAME, "Makes a gif from an emoji")
              .addOption(OptionType.STRING, "emoji", "the name of the emoji to make a gif", true)
    ).queue();
  }
}
