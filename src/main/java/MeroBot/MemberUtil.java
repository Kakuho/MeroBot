package MeroBot;

import net.dv8tion.jda.api.entities.Member;

class MemberUtil{
  static public String GetAvatarUrl(Member member){
    return member.getEffectiveAvatarUrl();
  }

  static public String GetDisplayName(Member member){
    return member.getEffectiveName();
  }
}
