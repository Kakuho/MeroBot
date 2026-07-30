package MeroBot;

import MeroBot.Config;
import MeroBot.Listeners.ListenerInstaller;
import MeroBot.SlashCommands.CommandInstaller;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class App {
    static private JDABuilder MeroBotBuilder(){
        JDABuilder builder = JDABuilder.createDefault(Config.GetBotToken())
                            .enableIntents(GatewayIntent.MESSAGE_CONTENT);
        ListenerInstaller.Install(builder);
        CommandInstaller.Install(builder);
        return builder;
    }

    public static void main(String[] args) {
        Config.InitConfig();
        JDA bot = MeroBotBuilder().build();
        CommandInstaller.InstallCommandsAsync(bot);
    }
}
