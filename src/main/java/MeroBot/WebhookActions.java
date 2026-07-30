package MeroBot;

import MeroBot.MemberUtil;
import MeroBot.WebhookUtil;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.attribute.IWebhookContainer;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;

import java.util.List;
import java.util.concurrent.CompletableFuture;

//  Assumptions:
//    - For each channel, MeroBot creates one and only one webhook

class WebhookActions{
  static public CompletableFuture<Boolean> ChannelHasOwnWebhookAsync(IWebhookContainer container){
    return container.retrieveWebhooks().submit()
    .thenApply( (webhooks) ->{
      JDA jda = container.getJDA();
      for(Webhook webhook: webhooks){
        if(WebhookUtil.OwnWebhook(jda, webhook)){
          return true;
        }
      }
      return false;
    });
  }

  static public CompletableFuture<Webhook> GetWebhookAsync(IWebhookContainer container){
    return container.retrieveWebhooks().submit()
    .thenApply( (webhooks) ->{
      JDA jda = container.getJDA();
      for(Webhook webhook: webhooks){
        if(WebhookUtil.OwnWebhook(jda, webhook)){
          return webhook;
        }
      }
      return null;
    });
  }

  static public WebhookAction CreateWebhookAsync(IWebhookContainer container){
    return container.createWebhook(WebhookUtil.GetWebhookName(container));
  }

  static public CompletableFuture<Webhook> CreateWebhookIfNotOwnAsync(IWebhookContainer container){
    return ChannelHasOwnWebhookAsync(container)
      .thenCompose((Boolean hasWebhook) ->{
        if(hasWebhook){
          return CompletableFuture.failedFuture(
            new IllegalStateException("Webhook already exists")
          );
        }
        else{
          return CreateWebhookAsync(container).submit();
        }
      });
  }

  static public CompletableFuture<Void> DeleteWebhookAsync(IWebhookContainer container){
    return GetWebhookAsync(container)
      .thenCompose((Webhook webhook) -> {
        if(webhook != null){
          container.deleteWebhookById(webhook.getId()).submit();
        }
        return CompletableFuture.completedFuture(null);
      });
  }

  static public CompletableFuture<Message> SendMessageAsync(IWebhookContainer container, String message){
    return ChannelHasOwnWebhookAsync(container)
      .thenCompose((Boolean hasWebhook) -> {
        if(!hasWebhook){
          return CompletableFuture.failedFuture(
            new IllegalStateException("Webhook doesn't exist for this channel")
          );
        }
        else{
          return GetWebhookAsync(container);
        }
      })
      .thenCompose((Webhook webhook) ->{
        return webhook.sendMessage(message).submit();
      });
  }

  static public CompletableFuture<Message> SendMessageAsMemberAsync(IWebhookContainer container, Member member, String message){
    return ChannelHasOwnWebhookAsync(container)
      .thenCompose((Boolean hasWebhook) -> {
        if(!hasWebhook){
          return CompletableFuture.failedFuture(
            new IllegalStateException("Webhook doesn't exist for this channel")
          );
        }
        else{
          return GetWebhookAsync(container);
        }
      })
      .thenCompose((Webhook webhook) ->{
        return webhook.sendMessage(message)
                      .setUsername(MemberUtil.GetDisplayName(member))
                      .setAvatarUrl(MemberUtil.GetAvatarUrl(member))
                      .submit();
      });
  }
}
