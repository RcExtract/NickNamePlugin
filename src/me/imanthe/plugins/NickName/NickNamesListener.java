package me.imanthe.plugins.NickName;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class NickNamesListener
  implements Listener
{
  private final NickNameManager nickManager;
  private final String jqMsgColour;
  
  public NickNamesListener(NickNames plugin)
  {
    this.nickManager = plugin.getNickManager();
    this.jqMsgColour = plugin.getJoinQuitMessageColour();
  }
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerJoinEvent event)
  {
    Player player = event.getPlayer();
    UUID playerId = player.getUniqueId();
    String nick = this.nickManager.getNickName(playerId);
    if (nick != null)
    {
      player.setDisplayName(nick);
      player.setPlayerListName(nick);
      
      String joinMsg = event.getJoinMessage();
      if ((joinMsg != null) && (!joinMsg.equals(""))) {
        event.setJoinMessage(joinMsg.replaceAll(player.getName(), nick));
      }
    }
  }
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    Player player = event.getPlayer();
    UUID playerId = player.getUniqueId();
    String nick = this.nickManager.getNickName(playerId);
    if (nick != null)
    {
      String quitMsg = event.getQuitMessage();
      if ((quitMsg != null) && (!quitMsg.equals(""))) {
        event.setQuitMessage(quitMsg.replaceAll(player.getName(), nick + this.jqMsgColour));
      }
    }
  }
}
