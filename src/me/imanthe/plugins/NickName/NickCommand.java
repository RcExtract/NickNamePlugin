package me.imanthe.plugins.NickName;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public final class NickCommand implements CommandExecutor
{
  private final NickNameManager nickManager;
  
  public NickCommand(NickNameManager nickManager)
  {
    this.nickManager = nickManager;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (cmd.getName().equalsIgnoreCase("nick"))
    {
      if (!(sender instanceof Player))
      {
        sender.sendMessage("[NickNames] Only players can have nicknames!");
        return true;
      }
      Player player = (Player) sender;
      if (args.length < 1 || args.length > 2)
      {
        player.sendMessage(ChatColor.RED + "Usage: /nick <nickname> or /nick <playername> <nickname> or /nick off");
        return true;
      }
      if (args[0].equalsIgnoreCase("off"))
      {
        if (args.length == 1)
        {
          if (!(player.hasPermission("imanthe.nick.self")))
          {
            player.sendMessage(ChatColor.RED + "You don't have permission to change your nickname!");
            return true;
          }
          if (!(this.nickManager.setNickName(player.getUniqueId(), null)))
          {
            player.sendMessage(ChatColor.RED + "Failed to change nickname");
            return true;
          }
          player.setDisplayName(player.getName());
          player.setPlayerListName(player.getName());
          player.sendMessage(ChatColor.GREEN + "Nickname successfully removed!");
          return true;
        }
        return true;
      }
      if (args.length == 1 && !(args[0].equalsIgnoreCase("off"))
      {
        if (!player.hasPermission("imanthe.nick.other"))
        {
          player.sendMessage(ChatColor.RED + "You don't have permission to change others' nickname!");
          return true;
        }
        String other = args[1];
        Player target = Bukkit.getPlayer(other);
        if (target == null)
        {
          //Target equaling to null doesn't mean the player is offline. Perhaps that player doesn't even exist.
          sender.sendMessage(ChatColor.RED + "Failed to find the player " + args[1] + "!");
          return true;
        }
        if (!(this.nickManager.setNickName(target.getUniqueId(), null)))
        {
          sender.sendMessage(ChatColor.GRAY + "Couldn't disable nickname for " + ChatColor.RED + other);
          return true;
        }
        target.setDisplayName(target.getName());
        target.setPlayerListName(target.getName());
        sender.sendMessage(ChatColor.GRAY + "Disabled nickname for " + ChatColor.RED + other);
        player.sendMessage(ChatColor.GRAY + "Your nickname was disabled");
        return true;
      }
      if (args.length == 1)
      {
        if (!(player.hasPermission("imanthe.nick.self")))
        {
          player.sendMessage(ChatColor.RED + "You don't have permission to change your nickname!");
          return true;
        }
        String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
        if (!this.nickManager.setNickName(player.getUniqueId(), nick)))
        {
          player.sendMessage(ChatColor.RED + "Couldn't set nick! Is it already taken?");
          return true;
        }
        player.setDisplayName(player.getName());
        player.setDisplayName(nick);
        player.setPlayerListName(nick);
        player.sendMessage(ChatColor.GREEN + "Nickname successfully changed!");
        return true;
      }
      if (args.length == 2) {
        if (!(player.hasPermission("imanthe.nick.other")))
        {
          player.sendMessage(ChatColor.RED + "Couldn't set nick! Is it already taken?");
          return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null)
        {
          player.sendMessage(ChatColor.GRAY + "Player " + ChatColor.RED + args[0] + ChatColor.GRAY + " cannot be found. :(");
          return true;
        }
        String nick = ChatColor.translateAlternateColorCodes('&', args[1]);
        if (!this.nickManager.setNickName(target.getUniqueId(), nick)))
        {
          sender.sendMessage(ChatColor.RED + "Couldn't set nick! Is it already taken?");
          return true;
        }
        target.setDisplayName(nick);
        target.setPlayerListName(nick);
        sender.sendMessage(ChatColor.GRAY + "Set " + ChatColor.RED + target.getName() + ChatColor.GRAY + "'s nick to " + nick);
        return true;
      }
      return true;
    }
    if (cmd.getName().equalsIgnoreCase("nicknames"))
    {
      if (args.length == 0)
      {
        sender.sendMessage(ChatColor.GOLD + "[NickNames]" + ChatColor.GREEN + " /nick <nickname> - Allows changing your nick.");
        sender.sendMessage(ChatColor.GREEN + " /nick <playername> <nickname> - Allows changing <playername>'s nick.");
        sender.sendMessage(ChatColor.GOLD + "[NickNames]" + ChatColor.RED + " /nick off - Allows removing your nick.");
        sender.sendMessage(ChatColor.GOLD + "[NickNames]" + ChatColor.RED + " /nicknames lookup <nickname> - Look up <nickname>");
        return true;
      }
      if (args[0].equalsIgnoreCase("lookup"))
      {
        //Console has all permissions granted, so the check whether sender is console or other things can be removed
        if (!(sender.hasPermission("imanthe.lookup")))
          sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
          return true;
        {
        if (!(args.length > 1))
        {
          sender.sendMessage(ChatColor.RED + "Usage: /nicknames lookup <nick>");
          return true;
        }
        if (this.nickManager.allowingDuplicates())
        {
          sender.sendMessage(ChatColor.RED + "Can't lookup a nick when duplicate nicknames are allowed!");
          return true;
        }
        sender.sendMessage(ChatColor.GRAY + "User with the nickname " + args[1] + " is called " + this.nickManager.getPlayerFromNickName(args[1]));
        return true;
      }
    }
    return false;
  }
}
