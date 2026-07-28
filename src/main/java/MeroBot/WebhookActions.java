package MeroBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.attribute.IWebhookContainer;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.User;
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

  static public CompletableFuture<Boolean> ChannelHasOwnWebhook(IWebhookContainer container){
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

  static public WebhookAction CreateWebhook(IWebhookContainer container){
    return container.createWebhook(GetWebhookName(container));
  }

  static public void CreateWebhookIfNotOwn(IWebhookContainer container){
    ChannelHasOwnWebhook(container)
      .thenCompose((Boolean hasWebhook) ->{
        if(hasWebhook){
          return CompletableFuture.failedFuture(
            new IllegalStateException("Webhook already exists")
          );
        }
        else{
          return CreateWebhook(container).submit();
        }
      })
      .whenComplete( (s, error) -> {});
  }
}
