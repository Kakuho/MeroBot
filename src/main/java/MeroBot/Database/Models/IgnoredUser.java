package MeroBot.Database.Models;

import java.util.Date;

/*
 user_id    | character varying           |           | not null | 
 ignored    | boolean                     |           |          | false
 date_added | timestamp without time zone |           |          | LOCALTIMESTAMP(0)
*/

public class IgnoredUser{
  private int userId;
  private boolean ignored;
  private Date dateAdded;

  public IgnoredUser(int userId, boolean ignored, Date dateAdded){
    this.userId = userId;
    this.ignored = ignored;
    this.dateAdded = dateAdded;
  }

  public int GetUserId(){return this.userId;}
  public void SetUserid(int userId){this.userId = userId;}

  public boolean GetIgnored(){return this.ignored;}
  public void SetIgnored(boolean ignored){this.ignored = ignored;}

  public Date GetDateAdded(){return this.dateAdded;}
  public void SetDateAdded(Date dateAdded){this.dateAdded = dateAdded;}
}
