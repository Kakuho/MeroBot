package MeroBot.SlashCommands.Ignore;

import MeroBot.Database.Repository.IgnoredChannelRepository;
import MeroBot.Database.Repository.RoleRepository;
import MeroBot.Database.Models.IgnoredChannel;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.lang.NumberFormatException;

// Permissions: Admin only

public class IgnoreChannelCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "ignore_channel";
  private static IgnoredChannelRepository ignoredChannelRepo = new IgnoredChannelRepository();
  private static RoleRepository roleRepo = new RoleRepository();

  static private boolean MemberIsAdmin(Member member){
    // there really should be a more efficient way for this other than the O(n) loop...
    List<Role> roles = member.getRoles();
    for(Role role: roles){
      if(roleRepo.IsRoleAdmin(role.getName())){
        return true;
      }
    }
    return false;
  }

  static private void DoReply(SlashCommandInteractionEvent event, String channelId, boolean ignoreValue){
    if(ignoreValue == true){
      event.getHook().sendMessage("Mero will start ignoring that channel mero!!").queue();
    }
    else{
      event.getHook().sendMessage("Mero will stop ignoring that channel mero!!").queue();
    }
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    event.deferReply().setEphemeral(true).queue();
    String channelId = event.getOption("channel_id").getAsString();
    try{
      if(event.getGuild().getGuildChannelById(channelId) == null){
        event.getHook().sendMessage("Sorry the channel with the id " + channelId + " does not exist mero!").queue();
        return;
      }
    }
    catch(NumberFormatException e){
        event.getHook().sendMessage("Sorry that id is invalid mero!").queue();
        return;
    }
    if(!MemberIsAdmin(event.getMember())){
      // should this be logged so admins can see if there's anyone suspiciously using this command?
      event.getHook().sendMessage("Sorry you cannot use that command, you're not an admin mero!").queue();
      return;
    }
    boolean ignoredValue = true;
    IgnoredChannel channel = ignoredChannelRepo.GetIgnoredChannel(channelId);
    if(channel == null){
      ignoredValue = true;
      ignoredChannelRepo.AddIgnoredChannel(channelId);
    }
    else{
      ignoredValue = !channel.GetIgnored();
      ignoredChannelRepo.SetIgnoredChannel(channelId, ignoredValue);
    }
    DoReply(event, channelId, ignoredValue);
  }
}
