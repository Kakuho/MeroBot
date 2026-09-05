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

  public String GetStatusChannel(){ return this.statusChannel;}
  public String GetBotToken(){ return this.botToken;}
}

class NullXmlConfigOptionException extends RuntimeException{
  public NullXmlConfigOptionException(String errorMessage) {
      super(errorMessage);
  }
}

public class Config{
  static private String StatusChannel = "";
  static private String BotToken = "";
  static final Logger logger = LoggerFactory.getLogger(Config.class);

  static public void InitConfig(){
    // first try to read from the environment, only do system environment as fallback
    try{
      InitFromXml();
    }
    catch(IOException exception){
      logger.info("Failed to initialise configuration values from xml file, reason: IOException");
      logger.info(exception.getMessage());
      InitFromSysEnv();
    }
    catch(NullXmlConfigOptionException exception){
      logger.info("Failed to initialise configuration values from xml file, reason: cannot find either bot token or status channel in the config file");
      logger.info(exception.getMessage());
      InitFromSysEnv();
    }
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
    Config.StatusChannel = configDeserialised.GetStatusChannel().strip();
    Config.BotToken = configDeserialised.GetBotToken().strip();
  }

  static private void InitFromSysEnv(){
    ReadBotTokenFromEnv();
    ReadStatusChannelFromEnv();
  }

  static public void SetStatusChannel(String value){Config.StatusChannel = value;}
  static public String GetStatusChannel(){return Config.StatusChannel;}

  static public void SetBotToken(String value){Config.BotToken = value;}
  static public String GetBotToken(){return Config.BotToken;}

  static private void ReadStatusChannelFromEnv(){
    // Read Status Channel from somewhere
    Config.StatusChannel = System.getenv("status_channel");
  }

  static private void ReadBotTokenFromEnv(){
    // Read Status Channel from somewhere
    Config.BotToken = System.getenv("mero_token");
  }
}


