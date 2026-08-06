package MeroBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.attribute.IWebhookContainer;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.unions.IThreadContainerUnion;

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

  static public IWebhookContainer ConvertChannelToWebhookContainer(MessageChannelUnion union){
    if(union.getType() == ChannelType.NEWS){
      IWebhookContainer container = union.asNewsChannel();
      return container;
    }
    else if(union.getType() == ChannelType.STAGE){
      IWebhookContainer container = union.asStageChannel();
      return container;
    }
    else if(union.getType() == ChannelType.TEXT){
      IWebhookContainer container = union.asTextChannel();
      return container;
    }
    else if(union.getType() == ChannelType.VOICE){
      IWebhookContainer container = union.asVoiceChannel();
      return container;
    }
    else if(union.getType() == ChannelType.GUILD_NEWS_THREAD){
      System.out.println("ConvertChannelToWebhookContainer: Cannot convert Guild Thread to IWebhookContainer");
      return null;
    }
    else if(union.getType() == ChannelType.GUILD_PUBLIC_THREAD){
      System.out.println("ConvertChannelToWebhookContainer: Cannot convert Guild Thread to IWebhookContainer");
      return null;
    }
    else if(union.getType() == ChannelType.GUILD_PRIVATE_THREAD){
      System.out.println("ConvertChannelToWebhookContainer: Cannot convert Guild Thread to IWebhookContainer");
      return null;
    }
    return null;
  }

  static public IWebhookContainer GetWebhookContainer(ThreadChannel thread){
    IThreadContainerUnion union = thread.getParentChannel();
    if(union.getType() == ChannelType.FORUM){
      IWebhookContainer container = union.asForumChannel();
      return container;
    }
    else if(union.getType() == ChannelType.NEWS){
      IWebhookContainer container = union.asNewsChannel();
      return container;
    }
    else if(union.getType() == ChannelType.TEXT){
      IWebhookContainer container = union.asTextChannel();
      return container;
    }
    return null;
  }
}
