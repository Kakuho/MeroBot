package MeroBot.Listeners;

import MeroBot.Config;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.JDA.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusChangeListener extends ListenerAdapter{
  static private final Logger logger = LoggerFactory.getLogger(StatusChangeListener.class);

  static private boolean IsNumber(String numString){
    try{
      Long.decode(numString);
      return true;
    }
    catch(NumberFormatException e){
      return false;
    }
  }
  
  @Override
  public void onStatusChange(StatusChangeEvent event){
    String statusChannel = Config.GetStatusChannel();
    if(statusChannel == null || statusChannel.length() == 0 || !IsNumber(statusChannel)){
      logger.info("No status change is logged because environment variable status channel is not valid");
      return;
    }
    var jda = event.getJDA();
    var channel = jda.getChannelById(MessageChannel.class, Config.GetStatusChannel());
    if(event.getNewStatus() == Status.CONNECTED){
      channel.sendMessage("merobot connected").queue();
    }
  }
}
