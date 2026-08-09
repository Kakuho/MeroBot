package MeroBot.Database.Models;

import java.util.Date;

class MessageLog{
  private String authorId;
  private String messageId;
  private Date dateAdded;

  MessageLog(String authorId, String messageId, Date dateAdded){
    this.authorId = authorId;
    this.messageId = messageId;
    this.dateAdded = dateAdded;
  }

  MessageLog(String authorId, String messageId){
    this.authorId = authorId;
    this.messageId = messageId;
  }

  public String GetAuthorId(){return this.authorId;}
  public void SetAuthorId(String authorId){this.authorId = authorId;}

  public String GetMessageId(){return this.messageId;}
  public void SetMessageId(String messageId){this.messageId = messageId;}

  public Date GetDateAdded(){return this.dateAdded;}
  public void SetDateAdded(Date dateAdded){this.dateAdded = dateAdded;}
}
