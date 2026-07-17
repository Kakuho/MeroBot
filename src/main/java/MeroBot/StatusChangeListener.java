package MeroBot;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.JDA.Status;

public class StatusChangeListener extends ListenerAdapter{
  @Override
  public void onStatusChange(StatusChangeEvent event){
    var jda = event.getJDA();
    var channel = jda.getChannelById(MessageChannel.class, "1527677258444312747");
    if(event.getNewStatus() == Status.CONNECTED){
      channel.sendMessage("merobot connected").queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)
    }
  }
}
