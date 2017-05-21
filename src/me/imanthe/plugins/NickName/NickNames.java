package me.imanthe.plugins.NickName;

import java.io.File;
import java.io.IOException;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NickNames
  extends JavaPlugin
{
  private NickNameManager manager;
  private File configFile;
  private YamlConfiguration config;
  private File nicksFile;
  private YamlConfiguration nicks;
  private boolean duplicates;
  private String jqMsgColour;
  
  public void onEnable()
  {
    this.configFile = new File(getDataFolder(), "config.yml");
    this.nicksFile = new File(getDataFolder(), "nicks.yml");
    if (!getDataFolder().exists()) {
      getDataFolder().mkdirs();
    }
    if (!this.configFile.exists()) {
      try
      {
        this.configFile.createNewFile();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    if (!this.nicksFile.exists()) {
      try
      {
        this.nicksFile.createNewFile();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    this.config = YamlConfiguration.loadConfiguration(this.configFile);
    this.nicks = YamlConfiguration.loadConfiguration(this.nicksFile);
    if (!this.config.contains("allow-duplicate-nicknames")) {
      this.config.set("allow-duplicate-nicknames", false);
    }
    if (!this.config.contains("join-and-quit-message-colour")) {
      this.config.set("join-and-quit-message-colour", "&e");
    }
    try
    {
      this.config.save(this.configFile);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    this.duplicates = this.config.getBoolean("allow-duplicate-nicknames", false);
    this.jqMsgColour = this.config.getString("join-and-quit-message-colour", "&e");
    
    this.manager = new NickNameManager(this);
    this.manager.loadNicks();
    
    NickCommand nickCommand = new NickCommand(this.manager);
    getCommand("nick").setExecutor(nickCommand);
    getCommand("nicknames").setExecutor(nickCommand);
    
    getServer().getPluginManager().registerEvents(new NickNamesListener(this), this);
  }
  
  public void onDisable()
  {
    this.manager.saveNicks(this.nicksFile);
  }
  
  public static NickNameManager getNickManager()
  {
    return this.manager;
  }
  
  public static getJoinQuitMessageColour()
  {
    return this.jqMsgColour;
  }
  
  public static boolean allowDuplicates()
  {
    return this.duplicates;
  }
  
  public static YamlConfiguration getNicksConfig()
  {
    return this.nicks;
  }
}
