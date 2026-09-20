package MeroBot.Database.Models;

import java.util.Date;

/*
 user_id       | character varying           |           | not null | 
 ignored       | boolean                     |           |          | false
 admin_ignored | boolean                     |           |          | false
 admin_id      | boolean                     |           |          | false
 date_added    | timestamp without time zone |           |          | LOCALTIMESTAMP(0)
*/

public class User{
  private String userId;
  private boolean ignored;
  private boolean adminIgnored;
  private String adminId;
  private Date dateAdded;

  public User(String userId, boolean ignored, boolean adminIgnored, String adminId, Date dateAdded){
    this.userId = userId;
    this.ignored = ignored;
    this.adminIgnored = adminIgnored;
    this.adminId = adminId;
    this.dateAdded = dateAdded;
  }

  public String GetUserId(){return this.userId;}
  public void SetUserid(String userId){this.userId = userId;}

  public boolean GetIgnored(){return this.ignored;}
  public void SetIgnored(boolean ignored){this.ignored = ignored;}
  
  public boolean GetAdminIgnored(){return this.adminIgnored;}
  public void SetAdminIgnored(boolean adminIgnored){this.adminIgnored = adminIgnored;}

  public String GetAdminId(){return this.adminId;}
  public void SetAdminid(String adminId){this.adminId = adminId;}

  public Date GetDateAdded(){return this.dateAdded;}
  public void SetDateAdded(Date dateAdded){this.dateAdded = dateAdded;}
}
