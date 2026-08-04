package MeroBot.Listeners;

import MeroBot.EmojiDetector;
import MeroBot.WebhookActions;
import MeroBot.WebhookUtil;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.attribute.IWebhookContainer;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.JDA.Status;

import java.util.concurrent.CompletableFuture;

// Core message listener, will create a webhook if its not created yet, and post 
// the user's message via that webhook if an emoji is detected

public class MessageListener extends ListenerAdapter{
  @Override
  public void onMessageReceived(MessageReceivedEvent event){
    if(event.getAuthor().isBot()){
      return;
    }
    if(event.isWebhookMessage()){
      return;
    }
    IWebhookContainer container = WebhookUtil.ConvertChannelToWebhookContainer(event.getChannel());
    Message message = event.getMessage();
    String content = message.getContentRaw();
    Member member = event.getMember();
    if(!EmojiDetector.HasEmoji(content)){
      return;
    }
    // at this point there is an unhandled emoji in the message
    String outmessage = EmojiDetector.ReplaceEmoji(event.getJDA(), event.getGuild(), content);
    if(outmessage.equals(content)){
      return;
    }
    // only if all checks fail
    WebhookActions.CreateWebhookIfNotOwnAsync(container)
      .thenCompose((Webhook webhook) -> {
        return WebhookActions.SendMessageAsMemberAsync(container, member, outmessage);
      })
      .whenComplete( (s, error) ->{
        if(error != null){
          return;
        }
      });
  }
}
