package MeroBot;

import java.lang.RuntimeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NullConfigOptionException extends RuntimeException{
  public NullConfigOptionException(String errorMessage){
    super(errorMessage);
  }
}

public class Config{
  static private String StatusChannel = "";
  static private String BotToken = "";
  static private String DatabaseUsername = "";
  static private String DatabasePassword = "";
  static private String DatabaseConnectionString = "";
  static private boolean Initialised = false;
  static final Logger logger = LoggerFactory.getLogger(Config.class);

  static public String GetStatusChannel(){return Config.StatusChannel;}
  static public String GetBotToken(){return Config.BotToken;}
  static public String GetDbUsername(){return Config.DatabaseUsername;}
  static public String GetDbPassword(){return Config.DatabasePassword;}
  static public String GetDbConnectionString(){return Config.DatabaseConnectionString;}
  static public boolean GetInitialised(){return Config.Initialised;}

  static public void InitConfig(){
    InitFromSysEnv();
    if(StatusChannel == null){
      throw new NullConfigOptionException("Status channel not found from environment variables");
    }
    if(BotToken == null){
      throw new NullConfigOptionException("Bot token not found from environment variables");
    }
    if(DatabaseUsername == null){
      throw new NullConfigOptionException("Database username not found from environment variables");
    }
    if(DatabasePassword == null){
      throw new NullConfigOptionException("Database password not found from environment variables");
    }
    if(DatabaseConnectionString == null){
      throw new NullConfigOptionException("Database connection string not found from environment variables");
    }
  }

  static private void InitFromSysEnv(){
    Config.StatusChannel = System.getenv("status_channel");
    Config.BotToken = System.getenv("mero_token");
    Config.StatusChannel = System.getenv("status_channel").strip();
    Config.BotToken = System.getenv("mero_token").strip();
    Config.DatabaseUsername = System.getenv("DbUser").strip();
    Config.DatabasePassword = System.getenv("DbPassword").strip();
    Config.DatabaseConnectionString = System.getenv("DbConnectionString").strip();
    Config.Initialised = true;
    logger.info("Connection String: " + Config.DatabaseConnectionString);
  }
}
