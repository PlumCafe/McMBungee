package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Database;
import net.mcmortals.mcmbungee.Utility.DatabasePlayer;
import net.mcmortals.mcmbungee.Utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class Ban extends Command {

    public Ban() {
        super("ban", "");
    }

    public void execute(CommandSender sender, String[] args) {
        //Permissions Check
        if (!Utility.hasPermission(sender,7)) {
            sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create()); return;
        }
        //Argument Length Check
        if (!(args.length >= 2)) {
            sender.sendMessage(Utility.prefix().append("Usage: §b/ban [Player] [Reason]").color(ChatColor.RED).create()); return;
        }
        DatabasePlayer player = Database.getPlayer(args[0]);
        //Has Joined Check
        if (player.exists()) {
            sender.sendMessage(Utility.prefix().append("No such player has ever joined!").color(ChatColor.RED).create()); return;
        }
        //Is Banned Check
        if (player.isBanned()) {
            sender.sendMessage(Utility.prefix().append("This player is already banned!").color(ChatColor.RED).create()); return;
        }
        //Combine Message
        int w = 1;
        String msg = "";
        do {
            msg = msg + " " + args[w];
            w++;
        } while (w < args.length);
        //Ban Player
        player.ban(msg, -1);
        //Alert Staff
        Utility.sendToStaff(ChatColor.AQUA + sender.getName() + " banned " + args[0] + " for" + msg + ".");
        //Register An Infraction
        Database.addInfraction(args[0], sender.getName(), Database.InfractionType.PERMANENT_BAN, "",  msg);
        //Disconnect The Player
        if(Utility.isOnline(args[0])) {
            ProxyServer.getInstance().getPlayer(args[0]).disconnect(new ComponentBuilder("You have been banned from the server!").color(ChatColor.RED)
                    .append("\n").append("Reason:").color(ChatColor.RED).append(msg).color(ChatColor.GOLD).append("\n")
                    .append("Appeal on http://www.mcmortals.net!").color(ChatColor.GOLD).create());
        }
    }
}
