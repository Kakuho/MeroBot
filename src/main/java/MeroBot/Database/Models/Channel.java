package MeroBot.Database.Models;

import java.util.Date;

/*
 channel_id | character varying           |           | not null | 
 ignored    | boolean                     |           |          | false
 date_added | timestamp without time zone |           |          | LOCALTIMESTAMP(0)
*/

public class Channel{
  private String channelId;
  private boolean ignored;
  private Date dateAdded;

  public Channel(String channelId, boolean ignored, Date dateAdded){
    this.channelId = channelId;
    this.ignored = ignored;
    this.dateAdded = dateAdded;
  }

  public String GetChannelId(){return this.channelId;}
  public void SetChannelid(String channelId){this.channelId = channelId;}

  public boolean GetIgnored(){return this.ignored;}
  public void SetIgnored(boolean ignored){this.ignored = ignored;}

  public Date GetDateAdded(){return this.dateAdded;}
  public void SetDateAdded(Date dateAdded){this.dateAdded = dateAdded;}
}
