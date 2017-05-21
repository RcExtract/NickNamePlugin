package me.imanthe.plugins.NickName;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public final class NickNameManager
{
  private final NickNames plugin;
  private final boolean duplicate;
  private final Map<UUID, String> nicknames = new HashMap();
  
  public NickNameManager(NickNames plugin)
  {
    this.plugin = plugin;
    
    this.duplicate = plugin.allowDuplicates();
  }
  
  public String getNickName(UUID player)
  {
    return (String)this.nicknames.get(player);
  }
  
  public UUID getPlayerFromNickName(String nick)
  {
    if (this.duplicate) {
      return null;
    }
    nick = ChatColor.stripColor(nick);
    for (Map.Entry<UUID, String> entry : this.nicknames.entrySet()) {
      if (ChatColor.stripColor((String)entry.getValue()).equals(nick)) {
        return (UUID)entry.getKey();
      }
    }
    return null;
  }
  
  public boolean setNickName(UUID id, String nickname)
  {
    if (nickname == null)
    {
      this.nicknames.remove(id);
      return true;
    }
    if ((!this.duplicate) && (getPlayerFromNickName(nickname) != null)) {
      return false;
    }
    if (this.nicknames.containsKey(id)) {
      this.nicknames.remove(id);
    }
    this.nicknames.put(id, nickname);
    return true;
  }
  
  public void loadNicks()
  {
    YamlConfiguration conf = this.plugin.getNicksConfig();
    Set<String> keys = conf.getKeys(false);
    for (String key : keys) {
      this.nicknames.put(UUID.fromString(key), conf.getString(key));
    }
  }
  
  public void saveNicks(File file)
  {
    YamlConfiguration conf = this.plugin.getNicksConfig();
    List<String> done = new ArrayList();
    for (Map.Entry<UUID, String> entry : this.nicknames.entrySet())
    {
      String key = ((UUID)entry.getKey()).toString();
      
      conf.set(key, entry.getValue());
      done.add(key);
    }
    for (String key : conf.getKeys(false)) {
      if (!done.contains(key)) {
        conf.set(key, null);
      }
    }
    try
    {
      conf.save(file);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public boolean allowingDuplicates()
  {
    return this.duplicate;
  }
}
