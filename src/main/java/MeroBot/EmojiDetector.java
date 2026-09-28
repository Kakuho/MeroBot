package MeroBot;

import MeroBot.EmojiUtil;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
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
//
//  kinda want to log it so that
//  EmojiDetector: Detected Emojis [{EMOJI_NAME1, SERVER}, {EMOJI_NAME2, SERVER}, {EMOJI_NAME3, SERVER}, ...]
//  EmojiDetector: Couldn't Find Emojis[EMOJI_NAME1, EMOJI_NAME2, ...]

//  should be easier to see which emojis we think are sent and which are not sent

public class EmojiDetector{
  static private final Pattern EMOJI_PATTERN = Pattern.compile("(?<!<a):[a-zA-Z]+:(?![0-9]+>)");
  static private final Logger logger = LoggerFactory.getLogger(EmojiDetector.class);

  static public boolean HasEmoji(String message){
    Matcher matcher = EMOJI_PATTERN.matcher(message);
    if(matcher.find()){
      return true;
    }
    else{
      return false;
    }
  }

  static public record EmojiLogInfo(String emojiName, String emojiServer){}

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

  static private void LogBothEmojiLists(String messageId, List<EmojiLogInfo> success, List<EmojiLogInfo> fails){
    // just so we can log the message atomically
    // form the success
    String sucMessage = "";
    if(!success.isEmpty()){
      sucMessage += "Found the following emojis from[";
      for(var emoji: success){
        sucMessage += " {" + emoji.emojiName + ", " + emoji.emojiServer +"}";
      }
      sucMessage += "]";
    }
    // form the failure message
    String failMessage = "";
    if(!fails.isEmpty()){
      failMessage += "The following emojis were not found [";
      for(var emoji: fails){
        failMessage += " {" + emoji.emojiName + "}";
      }
      failMessage += "]";
    }
    // now we can log them both
    String logMessage = messageId + " ";
    if(!sucMessage.equals("")){
      logMessage += sucMessage;
    }
    if(!failMessage.equals("")){
      logMessage += failMessage;
    }
    logger.info(logMessage);
  }

  private record ReplaceEmojiPack(String newMessage, List<EmojiLogInfo> success, List<EmojiLogInfo> fail){}

  static private ReplaceEmojiPack DoReplaceEmoji(JDA jda, Guild fromGuild, String message){
    // free discord users cannot send either current server animated emojis, or normal custom emojis from other servers
    List<EmojiLogInfo> successEmoji = new ArrayList<>();
    List<EmojiLogInfo> failedEmoji = new ArrayList<>();

    Set<String> seenEmojis = new HashSet<>();
    Set<String> invalidEmojis = new HashSet<>();

    String outputMessage = message;
    Matcher matcher = EMOJI_PATTERN.matcher(message);
    for(var result: matcher.results().toList()){
      String emojiRaw = message.substring(result.start(), result.end());
      if(seenEmojis.contains(emojiRaw)){
        continue;
      }
      // try to fetch the emoji object from servers
      String emojiName = emojiRaw.substring(1, emojiRaw.length() -1);
      logger.info(emojiName);
      Emoji emoji = null;
      String fromServer = null;
      if(EmojiUtil.GuildHasEmoji(fromGuild, emojiRaw)){
        emoji = EmojiUtil.GetEmojiFromGuild(fromGuild, emojiName); 
        fromServer = fromGuild.getName();
      }
      else{
        var foreignPack = EmojiUtil.GetEmojiFromOtherServersPack(jda, emojiName); 
        if(foreignPack != null){
          emoji = foreignPack.emoji();
          fromServer = foreignPack.guild().getName();
        }
      }
      if(emoji != null){
        outputMessage = outputMessage.replace(emojiRaw, emoji.getFormatted());
        seenEmojis.add(emojiRaw);
        successEmoji.add(new EmojiLogInfo(emojiRaw, fromServer));
      }
      else{
        // the emoji could not be found
        if(!invalidEmojis.contains(emojiRaw)){
          invalidEmojis.add(emojiRaw);
          failedEmoji.add(new EmojiLogInfo(emojiRaw, null));
        }
      }
    }
    logger.info("finished message: {}", outputMessage);
    return new ReplaceEmojiPack(outputMessage, successEmoji, failedEmoji);
  }

  static public String ReplaceEmoji(JDA jda, Guild fromGuild, Message message){
    logger.info("Replaced Message: {}", message.getContentRaw());
    var pack = DoReplaceEmoji(jda, fromGuild, message.getContentRaw());
    if(!pack.success.isEmpty() || !pack.fail.isEmpty()){
      LogBothEmojiLists(message.getId(), pack.success(), pack.fail());
    }
    return pack.newMessage;
  }
}
