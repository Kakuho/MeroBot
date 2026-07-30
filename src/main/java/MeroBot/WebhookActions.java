package MeroBot;

import MeroBot.MemberUtil;

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

class WebhookActions{
  static public boolean OwnWebhook(JDA jda, Webhook webhook){
    User selfUser = jda.getSelfUser();
    User webhookOwner = webhook.getOwnerAsUser();
    if(webhookOwner.getId().equals(selfUser.getId())){
      return true;
    }
    else{
      return false;
    }
  }

  static public CompletableFuture<Boolean> ChannelHasOwnWebhookAsync(IWebhookContainer container){
    return container.retrieveWebhooks().submit()
    .thenApply( (webhooks) ->{
      JDA jda = container.getJDA();
      for(Webhook webhook: webhooks){
        if(OwnWebhook(jda, webhook)){
          return true;
        }
      }
      return false;
    });
  }

  static public String GetWebhookName(IWebhookContainer container){
    return "Merobot_" + container.getId();
  }

  static public CompletableFuture<Webhook> GetWebhookAsync(IWebhookContainer container){
    return container.retrieveWebhooks().submit()
    .thenApply( (webhooks) ->{
      JDA jda = container.getJDA();
      for(Webhook webhook: webhooks){
        if(OwnWebhook(jda, webhook)){
          return webhook;
        }
      }
      return null;
    });
  }

  static public WebhookAction CreateWebhookAsync(IWebhookContainer container){
    return container.createWebhook(GetWebhookName(container));
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
