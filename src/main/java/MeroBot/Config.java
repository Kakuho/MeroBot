package MeroBot;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.io.IOException; 
import java.lang.RuntimeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MerobotXmlConfig{
  @JsonProperty("StatusChannel")
  private String statusChannel;

  @JsonProperty("BotToken")
  private String botToken;

  @JsonProperty("DbUsername")
  private String dbUsername = "";

  @JsonProperty("DbPassword")
  private String dbPassword = "";

  @JsonProperty("DbConnectionString")
  private String dbConnectionString = "";

  public String GetStatusChannel(){ return this.statusChannel;}
  public String GetBotToken(){ return this.botToken;}
  public String GetDbUsername(){ return this.dbUsername;}
  public String GetDbPassword(){ return this.dbPassword;}
  public String GetDbConnectionString(){ return this.dbConnectionString;}
}

class NullXmlConfigOptionException extends RuntimeException{
  public NullXmlConfigOptionException(String errorMessage){
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
    /*
    try{
      InitFromXml();
      Initialised = true;
    }
    catch(IOException exception){
      logger.info("Failed to initialise configuration values from xml file, IOException reason: " + exception.getMessage());
      logger.info("Trying to initialise from system env...");
      InitFromSysEnv();
    }
    catch(NullXmlConfigOptionException exception){
      logger.info("Failed to initialise configuration values from xml file, NullXmlConfigOptionException reason: " + exception.getMessage());
      logger.info("Trying to initialise from system env...");
      InitFromSysEnv();
    }
    */
  }

  static private void InitFromXml() throws IOException{
    File file = new File("merobot_config.xml");
    XmlMapper xmlMapper = new XmlMapper();
    MerobotXmlConfig configDeserialised = xmlMapper.readValue(file, MerobotXmlConfig.class); // it will be here if it throws
    if(configDeserialised.GetStatusChannel() == null){
      throw new NullXmlConfigOptionException("Status channel not found in xml");
    }
    if(configDeserialised.GetBotToken() == null){
      throw new NullXmlConfigOptionException("Bot token not found in xml");
    }
    if(configDeserialised.GetDbUsername() == null){
      throw new NullXmlConfigOptionException("Database username not found in xml");
    }
    if(configDeserialised.GetDbPassword() == null){
      throw new NullXmlConfigOptionException("Database password not found in xml");
    }
    if(configDeserialised.GetDbConnectionString() == null){
      throw new NullXmlConfigOptionException("Database connection string not found in xml");
    }
    Config.StatusChannel = configDeserialised.GetStatusChannel().strip();
    Config.BotToken = configDeserialised.GetBotToken().strip();
    Config.DatabaseUsername = configDeserialised.GetDbUsername().strip();
    Config.DatabasePassword = configDeserialised.GetDbPassword().strip();
    Config.DatabaseConnectionString = configDeserialised.GetDbConnectionString().strip();
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
