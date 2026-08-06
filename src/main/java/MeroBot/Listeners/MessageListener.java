package MeroBot.Listeners;

import MeroBot.EmojiDetector;
import MeroBot.WebhookActions;
import MeroBot.WebhookUtil;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.attribute.IWebhookContainer;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.CompletableFuture;

// Core message listener, will create a webhook if its not created yet, and post 
// the user's message via that webhook if an emoji is detected

public class MessageListener extends ListenerAdapter{

  static private CompletableFuture<Message> SendToChannelAsync(IWebhookContainer container, String content, Member member){
    return WebhookActions.CreateWebhookIfNotOwnAsync(container)
      .thenCompose((Webhook webhook) -> WebhookActions.SendMessageAsMemberAsync(container, member, content));
  }

  static private CompletableFuture<Message> SendToThreadAsync(ThreadChannel thread, String content, Member member){
    IWebhookContainer container = WebhookUtil.GetWebhookContainer(thread);
    if(container != null){
      return WebhookActions.CreateWebhookIfNotOwnAsync(container)
        .thenCompose((Webhook webhook) -> WebhookActions.SendMessageAsMemberAsync(thread, member, content));
    }
    else{
      return null;
    }
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event){
    if(event.getAuthor().isBot()){
      return;
    }
    if(event.isWebhookMessage()){
      return;
    }
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
    IWebhookContainer container = WebhookUtil.ConvertChannelToWebhookContainer(event.getChannel());
    CompletableFuture<Message> future = null;
    if(container != null){
      future = SendToChannelAsync(container, outmessage, member);
    }
    else{
      future = SendToThreadAsync(event.getChannel().asThreadChannel(), outmessage, member);
    }
    future.whenComplete((m, error) -> {
      if(error != null){
      }
      message.delete().queue();
    });
  }
}
