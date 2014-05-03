package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.DatabaseUtility.DatabasePlayer;
import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

public class InfrCommand extends Command {

    private main m = null;

    public InfrCommand(main This) {
        super("infractions", "", "infr");
        m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!m.hasPermission(sender, 6)) {
            sender.sendMessage(prefix().append("You cannot do that!").color(ChatColor.RED).create());
            return;
        }
        if (args.length!=1) {
            sender.sendMessage(prefix().append("Usage: ").color(ChatColor.RED).append("/infr [Player]").color(ChatColor.AQUA).create());
            return;
        }
        try {
            DatabasePlayer dp = new DatabasePlayer(args[0], m.connect);
            if (dp.exists()) {
                int bans = 0; int kicks = 0; int mutes = 0;
                sender.sendMessage(prefix().append("Infractions for: ").color(ChatColor.GOLD).bold(true).append(args[0]).create());
                Statement s = m.connect.createStatement();
                ResultSet res1 = s.executeQuery("SELECT * FROM McMInfractions WHERE PlayerName='" + args[0] + "'");
                while (res1.next()) {
                    sender.sendMessage(new ComponentBuilder(res1.getString("Type") + ": ").color(ChatColor.RED).bold(true).append("Reason: ").color(ChatColor.AQUA).bold(false).append(res1.getString("Reason")).color(ChatColor.GOLD).create());
                    String time = res1.getString("Time");
                    if (time==null) {
                        sender.sendMessage(new ComponentBuilder("- Issued by " ).color(ChatColor.AQUA).append(res1.getString("Enforcer")).color(ChatColor.GOLD).create());
                    } else {
                        sender.sendMessage(new ComponentBuilder("- Issued by " ).color(ChatColor.AQUA).append(res1.getString("Enforcer")).color(ChatColor.GOLD).append(" for ").color(ChatColor.AQUA).append(time).color(ChatColor.GOLD).create());
                    }
                }
            } else sender.sendMessage(prefix().append("No info found for ").color(ChatColor.RED).append(args[0]).color(ChatColor.AQUA).create());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ");
    }

    public static String getPlayerName(String name, ChatColor t, int rank){
        if (t==null) t=ChatColor.WHITE;
            if ((rank==10)) {
                return "§4§l[Op] §b"  +  name + "§r";
            }
            if ((rank==9)) {
                return "§5§l[Dev] §b"+ name + "§r";
            }
            if ((rank==8)) {
                return "§c§l[Admin] §b" + name + "§r";
            }
            if ((rank==7)) {
                return "§2[Mod] §f" + ChatColor.WHITE +  t +   name + "§r";
            }
            if ((rank==6)) {
                return "§9[Helper] §f" + ChatColor.WHITE + t +  name + "§r";
            }
            if ((rank==5)) {
                return "§2[Host] " + ChatColor.WHITE +  t + name + "§r";
            }
            if ((rank==4)) {
                return "§3[Builder] §f" + ChatColor.WHITE + t +  name + "§r";
            }
            if ((rank==3)) {
                return "§4[Legend] §f" + ChatColor.WHITE + t +  name + "§r";
            }
            if ((rank==2)) {
                return "§6[YT] §f"+ ChatColor.WHITE + t +  name + "§r";
            }
            if ((rank==1)) {
                return "§a[VIP] §f" + ChatColor.WHITE + t + name + "§r";
            }
        return t + name + "§r";
    }
}