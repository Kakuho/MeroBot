package MeroBot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;

import MeroBot.StatusChangeListener;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println(System.getenv("merotoken"));
        String token = System.getenv("merotoken");
        JDA bot = JDABuilder.createDefault(token)
                            .addEventListeners(new StatusChangeListener())
                            .build();
    }
}
