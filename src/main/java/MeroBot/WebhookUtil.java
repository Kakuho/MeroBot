package MeroBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.attribute.IWebhookContainer;

public class WebhookUtil{
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

  static public String GetWebhookName(IWebhookContainer container){
    return "Merobot_" + container.getId();
  }
}
