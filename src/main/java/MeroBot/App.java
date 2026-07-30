package MeroBot;

import MeroBot.Config;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class App {
    public static void main(String[] args) {
        Config.InitConfig();
        JDA bot = JDABuilder.createDefault(Config.GetBotToken())
                            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                            .build();
    }
}
