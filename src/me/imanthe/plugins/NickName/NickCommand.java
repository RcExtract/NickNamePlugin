package me.imanthe.plugins.NickName;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public final class NickCommand
  implements CommandExecutor
{
  private final NickNameManager nickManager;
  
  public NickCommand(NickNameManager nickManager)
  {
    this.nickManager = nickManager;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    String cn = cmd.getName().toLowerCase();
    if (cn.equals("nick"))
    {
      if ((sender instanceof Player))
      {
        Player player = (Player)sender;
        if ((args.length < 1) || (args.length > 2)) {
          player.sendMessage(ChatColor.RED + "Usage: /nick <nickname> or /nick <playername> <nickname>");
        } else if (args[0].equalsIgnoreCase("off"))
        {
          if (args.length == 1)
          {
            if (player.hasPermission("imanthe.nick.self"))
            {
              if (this.nickManager.setNickName(player.getUniqueId(), null))
              {
                player.setDisplayName(player.getName());
                player.setPlayerListName(player.getName());
                player.sendMessage(ChatColor.GREEN + "Nickname successfully removed!");
              }
              else
              {
                player.sendMessage(ChatColor.RED + "Failed to change nickname");
              }
            }
            else {
              player.sendMessage(ChatColor.RED + 
                "You don't have permission to change your nickname!");
            }
          }
          else if (player.hasPermission("imanthe.nick.other"))
          {
            String other = args[1];
            
            Player target = Bukkit.getPlayer(other);
            if (target == null)
            {
              sender.sendMessage(ChatColor.RED + "That player isn't online!");
            }
            else if (this.nickManager.setNickName(target.getUniqueId(), null))
            {
              target.setDisplayName(target.getName());
              target.setPlayerListName(target.getName());
              sender.sendMessage(ChatColor.GRAY + "Disabled nickname for " + ChatColor.RED + other);
              player.sendMessage(ChatColor.GRAY + "Your nickname was disabled");
            }
            else
            {
              sender.sendMessage(ChatColor.GRAY + "Couldn't disable nickname for " + 
                ChatColor.RED + other);
            }
          }
        }
        else if (args.length == 1)
        {
          if (player.hasPermission("imanthe.nick.self"))
          {
            String nick = ChatColor.translateAlternateColorCodes('&', args[0]);
            if (this.nickManager.setNickName(player.getUniqueId(), nick))
            {
              player.setDisplayName(player.getName());
              player.setDisplayName(nick);
              player.setPlayerListName(nick);
              player.sendMessage(ChatColor.GREEN + "Nickname successfully changed!");
            }
            else
            {
              player.sendMessage(ChatColor.RED + "Couldn't set nick! Is it already taken?");
            }
          }
          else
          {
            player.sendMessage(ChatColor.RED + 
              "You don't have permission to change your nickname!");
          }
        }
        else if (args.length == 2) {
          if (player.hasPermission("imanthe.nick.other"))
          {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null)
            {
              String nick = ChatColor.translateAlternateColorCodes('&', args[1]);
              if (this.nickManager.setNickName(target.getUniqueId(), nick))
              {
                target.setDisplayName(nick);
                target.setPlayerListName(nick);
                sender.sendMessage(ChatColor.GRAY + "Set " + ChatColor.RED + target.getName() + ChatColor.GRAY + "'s nick to " + 
                  nick);
              }
              else
              {
                sender.sendMessage(ChatColor.RED + 
                  "Couldn't set nick! Is it already taken?");
              }
            }
            else
            {
              player.sendMessage(ChatColor.GRAY + "Player " + ChatColor.RED + args[0] + 
                ChatColor.GRAY + " is currently offline. :(");
            }
          }
          else
          {
            player.sendMessage(ChatColor.RED + 
              "You don't have permission to change other people's nicknames!");
          }
        }
      }
      else
      {
        sender.sendMessage("[NickNames] Only players can have nicknames!");
      }
    }
    else if (cn.equals("nicknames")) {
      if (args.length == 0)
      {
        sender.sendMessage(ChatColor.GOLD + "[NickNames]" + ChatColor.GREEN + 
          " /nick <nickname> - Allows changing your nick.");
        sender.sendMessage(ChatColor.GREEN + 
          " /nick <playername> <nickname> - Allows changing <playername>'s nick.");
        sender.sendMessage(ChatColor.GOLD + "[NickNames]" + ChatColor.RED + 
          " /nick off - Allows removing your nick.");
        sender.sendMessage(ChatColor.GOLD + "[NickNames]" + ChatColor.RED + 
          " /nicknames lookup <nickname> - Look up <nickname>");
      }
      else
      {
        String arg = args[0].toLowerCase();
        if (arg.equals("lookup")) {
          if (((sender instanceof ConsoleCommandSender)) || (sender.hasPermission("imanthe.lookup")))
          {
            if (args.length > 1)
            {
              if (this.nickManager.allowingDuplicates()) {
                sender.sendMessage(ChatColor.RED + 
                  "Can't lookup a nick when duplicate nicknames are allowed!");
              } else {
                sender.sendMessage(ChatColor.GRAY + "User with the nickname " + args[1] + " is called " + 
                  this.nickManager.getPlayerFromNickName(args[1]));
              }
            }
            else {
              sender.sendMessage(ChatColor.RED + "Usage: /nicknames lookup <nick>");
            }
          }
          else {
            sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
          }
        }
      }
    }
    return true;
  }
}
