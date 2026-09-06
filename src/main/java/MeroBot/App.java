package MeroBot;

import MeroBot.Database.MeroDatabase;

import MeroBot.Config;
import MeroBot.Listeners.ListenerInstaller;
import MeroBot.SlashCommands.CommandInstaller;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App{
  static private Logger logger = LoggerFactory.getLogger(App.class);

  static private JDABuilder MeroBotBuilder(){
      JDABuilder builder = JDABuilder.createDefault(Config.GetBotToken())
                          .enableIntents(GatewayIntent.MESSAGE_CONTENT);
      ListenerInstaller.Install(builder);
      CommandInstaller.Install(builder);
      return builder;
  }

  public static void main(String[] args) {
    Config.InitConfig();
    try{
      MeroDatabase.StartDatabase();
    }
    catch(MeroDatabase.DatabaseInitialisationException e){
      logger.info("Starting discord bot without database configuration set");
    }
    JDA bot = MeroBotBuilder().build();
    CommandInstaller.InstallCommandsAsync(bot);
  }
}
