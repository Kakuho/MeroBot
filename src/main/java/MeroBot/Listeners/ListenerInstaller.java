package MeroBot.Listeners;

import MeroBot.Listeners.StatusChangeListener;

import net.dv8tion.jda.api.JDABuilder;

class ListenerInstaller{
  static public JDABuilder Install(JDABuilder builder){
    return builder.addEventListeners(new StatusChangeListener());
  }
}
