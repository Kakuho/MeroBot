package MeroBot;

public class Config{
  static private String StatusChannel = "";
  static private String BotToken = "";

  static public void InitConfig(){
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
