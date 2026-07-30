package MeroBot;

import MeroBot.EmojiUtil;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.JDA;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

//  When a user sends an emoji that they do not have access to, discord sends the message containing the original :emoji_name:.
//  We need to read the message and then:
//    - if the pattern is not of the form <a:emoji_name:emoji_id>, and only of the from :emoji_name:
//      then we replace :emoji_name: with the <a:emoji_name:emoji_id>
//
//    - otherwise we leave the message alone
//
//  e.g,
//    given message = ":moomgun:"
//    the bot replaces that with "<a:moomgun:413241234>"
//
//    given the message = "<a:moomgun:143423>"
//    the bot does not replace that message and reposts it
//
//    given the message = ":moomgun: but not <a:moomgun:143242>"
//    the bot replaces it with "<a:moomgun:1433243>, <a:moomgun:143242>"

public class EmojiDetector{
  static private final Pattern EMOJI_PATTERN = Pattern.compile("(?<!<a):[a-zA-Z]+:(?![0-9]+>)");

  static public boolean HasEmoji(String message){
    Matcher matcher = EMOJI_PATTERN.matcher(message);
    if(matcher.find()){
      return true;
    }
    else{
      return false;
    }
  }

  static public class EmojiIndexer{
    private int start;
    private int end;

    public EmojiIndexer(int start, int end){
      this.start = start;
      this.end = end;
    }

    public int GetStart(){ return this.start;}
    public int GetEnd(){ return this.end;}
  }

  static public List<EmojiIndexer> GetEmojis(String message){
    Matcher matcher = EMOJI_PATTERN.matcher(message);
    List<EmojiIndexer> emojis = new ArrayList<EmojiIndexer>();
    while(matcher.find()){
      emojis.add(new EmojiIndexer(matcher.start(), matcher.end()));
    }
    return emojis;
  }

  static public String ReplaceEmoji(JDA jda, Guild fromGuild, String message){
    Matcher matcher = EMOJI_PATTERN.matcher(message);
    while(matcher.find()){
      int startIndex = matcher.start();
      int  endIndex = matcher.end();
      String emojiRegion = message.substring(startIndex, endIndex);
      String emojiRaw = message.substring(startIndex + 1, endIndex - 1);
      String emojiId = null;
      if(EmojiUtil.GuildHasEmoji(fromGuild, emojiRaw)){
        // really want null operators here ngl
        var emoji = EmojiUtil.GetEmojiFromGuild(fromGuild, emojiRaw); 
        emojiId = emoji != null ? emoji.getFormatted() : null; 
      }
      else{
        // really want null operators here ngl
        var emoji = EmojiUtil.GetEmojiFromOtherServers(jda, emojiRaw); 
        emojiId = emoji != null ? emoji.getFormatted() : null; 
      }
      if(emojiId != null){
        message = message.replace(emojiRegion, "<a:" + emojiRaw + ":" + emojiId + ">");
      }
    }
    return message;
  }
}
