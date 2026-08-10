package MeroBot.SlashCommands.Ignore;

import MeroBot.Database.Repository.IgnoredUserRepository;
import MeroBot.Database.Repository.RoleRepository;
import MeroBot.Database.Models.IgnoredUser;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.lang.NumberFormatException;
import java.sql.SQLException;

// Permissions: Admin only

public class IgnoreUserCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "ignore_user";
  private static IgnoredUserRepository ignoredUserRepo = new IgnoredUserRepository();
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

  static private void DoReply(SlashCommandInteractionEvent event, String userId, boolean ignoreValue){
    if(ignoreValue == true){
      event.getHook().sendMessage("Mero will start ignoring that user mero!!").queue();
    }
    else{
      event.getHook().sendMessage("Mero will stop ignoring that user mero!!").queue();
    }
  }

  void HandleSqlException(SQLException e, SlashCommandInteractionEvent event){
    if(e instanceof java.sql.SQLTransientConnectionException){
      event.getHook().sendMessage("Meroron failed to connect to the database mero, could the database be offline mero?")
                     .queue();
    }
    else if(e instanceof java.sql.SQLNonTransientConnectionException){
      event.getHook().sendMessage("Meroron failed to connect to the database mero, could the database be offline mero?")
                     .queue();
    }
  }

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    event.deferReply().setEphemeral(true).queue();
    String userId = event.getOption("user_id").getAsString();
    // user id checking
    try{
      if(event.getGuild().getMemberById(userId) == null){
        event.getHook().sendMessage("Sorry the user with the id " + userId + " does not exist mero!").queue();
        return;
      }
    }
    catch(NumberFormatException e){
      event.getHook().sendMessage("Sorry that id is invalid mero!").queue();
      return;
    }
    // Start of actual logic
    try{
      if(!MemberIsAdmin(event.getMember())){
        // should this be logged so admins can see if there's anyone suspiciously using this command?
        event.getHook().sendMessage("Sorry you cannot use that command, you're not an admin mero!").queue();
        return;
      }
      boolean ignoredValue = true;
      IgnoredUser user = ignoredUserRepo.GetIgnoredUser(userId);
      if(user == null){
        ignoredValue = true;
        ignoredUserRepo.AddIgnoredUser(userId);
      }
      else{
        ignoredValue = !user.GetIgnored();
        ignoredUserRepo.SetIgnoredUser(userId, ignoredValue);
      }
      DoReply(event, userId, ignoredValue);
    }
    catch(SQLException e){
      HandleSqlException(e, event);
    }
  }
}
