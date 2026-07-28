package MeroBot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println(System.getenv("merotoken"));
        String token = System.getenv("merotoken");
        JDA bot = JDABuilder.createDefault(token)
                            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                            .build();

        bot.updateCommands()
          .addCommands(
          )
          .queue();
    }
}
