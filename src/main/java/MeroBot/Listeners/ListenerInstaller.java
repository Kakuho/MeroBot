package MeroBot.Listeners;

import MeroBot.Listeners.StatusChangeListener;
import MeroBot.Listeners.MessageListener;

import net.dv8tion.jda.api.JDABuilder;

public class ListenerInstaller{
  static public JDABuilder Install(JDABuilder builder){
    return builder.addEventListeners(new StatusChangeListener())
                  .addEventListeners(new MessageListener());
  }
}
