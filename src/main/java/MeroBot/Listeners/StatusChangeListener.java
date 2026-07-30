package MeroBot.Listeners;

import MeroBot.Config;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.JDA.Status;

public class StatusChangeListener extends ListenerAdapter{
  @Override
  public void onStatusChange(StatusChangeEvent event){
    var jda = event.getJDA();
    var channel = jda.getChannelById(MessageChannel.class, Config.GetStatusChannel());
    if(event.getNewStatus() == Status.CONNECTED){
      channel.sendMessage("merobot connected").queue();
    }
  }
}
