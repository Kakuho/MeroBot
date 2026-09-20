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

  static private void LogAndThrowNullOption(String message){
      logger.error(message);
      throw new NullConfigOptionException(message);
  }

  static private void SysEnvGuard(){
    if(System.getenv("status_channel") == null){
      logger.info("environment variable status_channel was found to be null, ignoring it for now...");
    }
    if(System.getenv("mero_token") == null){
      LogAndThrowNullOption("environment variable mero_token was found to be null. Please set this to your discord bot token");
    }
    if(System.getenv("DbUser") == null){
      LogAndThrowNullOption("environment variable DbUser was found to be null. It is recommended to set this value to `meroron`");
    }
    if(System.getenv("DbPassword") == null){
      LogAndThrowNullOption("environment variable DbPassword was found to be null. It is recommended to set this value to `meromeromero`");
    }
    if(System.getenv("DbConnectionString") == null){
      LogAndThrowNullOption("environment variable DbConnectionString was found to be null. It is recommended to set this value to `jdbc:postgresql://localhost:8500/`");
    }
  }

  static private void InitFromSysEnv(){
    SysEnvGuard();
    Config.StatusChannel = System.getenv("status_channel");
    Config.BotToken = System.getenv("mero_token").strip();
    Config.DatabaseUsername = System.getenv("DbUser").strip();
    Config.DatabasePassword = System.getenv("DbPassword").strip();
    Config.DatabaseConnectionString = System.getenv("DbConnectionString").strip();
    Config.Initialised = true;
    logger.info("Connection String: " + Config.DatabaseConnectionString);
  }
}
