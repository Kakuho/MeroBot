package MeroBot.Listeners;

import MeroBot.EmojiDetector;
import MeroBot.WebhookActions;
import MeroBot.WebhookUtil;
import MeroBot.Database.Repository.TrackedUserRepository;
import MeroBot.Database.Models.TrackedUser;
import MeroBot.Database.Repository.IgnoredChannelRepository;
import MeroBot.Database.Models.IgnoredChannel;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.attribute.IWebhookContainer;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.CompletableFuture;
import java.sql.SQLException;

// Core message listener, will create a webhook if its not created yet, and post 
// the user's message via that webhook if an emoji is detected

public class MessageListener extends ListenerAdapter{
  static private TrackedUserRepository trackedUserRepo = new TrackedUserRepository();
  static private IgnoredChannelRepository ignoredChannelRepo = new IgnoredChannelRepository();

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

  static private boolean PassesInitialChecks(MessageReceivedEvent event, Member member){
    if(event.getAuthor().isBot()){
      return false;
    }
    if(event.isWebhookMessage()){
      return false;
    }
    try{
      TrackedUser user = trackedUserRepo.GetTrackedUser(member.getId());
      if(user != null && user.GetIgnored() == true){
        return false;
      }
      IgnoredChannel channel = ignoredChannelRepo.GetIgnoredChannel(event.getGuildChannel().getId());
      if(channel != null && channel.GetIgnored() == true){
        return false;
      }
    }
    catch(SQLException e){
      // should log the error
    }
    return true;
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event){
    Member member = event.getMember();
    if(!PassesInitialChecks(event, member)){
      return;
    }
    Message message = event.getMessage();
    String content = message.getContentRaw();
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
