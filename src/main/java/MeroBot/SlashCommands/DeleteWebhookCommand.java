package MeroBot.SlashCommands;

import MeroBot.WebhookActions;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;

import java.util.concurrent.CompletableFuture;
import java.util.List;

// Given a channel's name, delete the webhook if it exists for that channel

public class DeleteWebhookCommand extends ListenerAdapter{
  static public final String COMMAND_NAME = "delete_webhook";
  static public final String OPTION_NAME = "channel name";
  static public final String OPTION_TYPE = "channel name";

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
    if(!event.getName().equals(COMMAND_NAME)){
      return;
    }
    String channelName = event.getOption(OPTION_NAME).getAsString();
    Guild guild = event.getGuild();
    List<GuildChannel> guildChannels = guild.getChannels(false);
    for(GuildChannel channel: guildChannels){
      if(channel.getName().equals(channelName) && channel.getType() == ChannelType.TEXT){
      }
    }
  }
}

