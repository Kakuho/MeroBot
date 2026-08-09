package MeroBot.Database.Models;

import java.util.Date;

public class Role{
  private int id;
  private String value;
  private boolean isAdmin;
  private Date dateAdded;

  public Role(int id, String value, boolean isAdmin, Date dateAdded){
    this.id = id;
    this.value = value;
    this.isAdmin = isAdmin;
    this.dateAdded = dateAdded;
  }

  public int GetId(){return this.id;}
  public void SetId(int id){this.id = id;}

  public String GetValue(){return this.value;}
  public void SetValue(String value){this.value = value;}

  public boolean GetIsAdmin(){return this.isAdmin;}
  public void SetIsAdmin(boolean isAdmin){this.isAdmin = isAdmin;}

  public Date GetDateAdded(){return this.dateAdded;}
  public void SetDateAdded(Date dateAdded){this.dateAdded = dateAdded;}
}
