package MeroBot.SlashCommands.Ignore;

import MeroBot.Database.Repository.RoleRepository;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.sql.SQLException;

class IgnoreUtils extends ListenerAdapter{
  private static RoleRepository roleRepo = new RoleRepository();

  static boolean MemberIsAdmin(Member member) throws SQLException{
    // there really should be a more efficient way for this other than the O(n) loop...
    try{
      List<Role> roles = member.getRoles();
      for(Role role: roles){
        if(roleRepo.IsRoleAdmin(role.getName())){
          return true;
        }
      }
      return false;
    }
    catch(SQLException e){
      throw e;
    }
  }
}
