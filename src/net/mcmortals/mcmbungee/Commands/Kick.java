package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Database;
import net.mcmortals.mcmbungee.Utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class Kick extends Command {

    public Kick() {
        super("kick", "");
    }

    public void execute(CommandSender sender, String[] args) {
        //Permissions Check
        if (!Utility.hasPermission(sender,6)) {
            sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create()); return;
        }
        //Argument Length Check
        if (!(args.length>=2)) {
            sender.sendMessage(Utility.prefix().append("Usage: §b/kick [Player] [Reason]").color(ChatColor.RED).create()); return;
        }
        //Is Online Check
        if(!Utility.isOnline(args[0])){
            sender.sendMessage(new ComponentBuilder("That player is not online.").color(ChatColor.RED).create());
            return;
        }
        String msg = "";
        //Combine Message
        int w = 1;
        do {
            msg = msg + " " + args[w];
            w++;
        } while (w < args.length);
        //Kick Player
        ProxyServer.getInstance().getPlayer(args[0]).disconnect(new ComponentBuilder("You have been kicked from the server!").color(ChatColor.RED).append("\n")
                    .append("Reason:").color(ChatColor.RED).append(msg).color(ChatColor.GOLD).create());
        //Register an Infraction
        Database.addInfraction(args[0], sender.getName(), Database.InfractionType.KICK, "", msg);
        //Alert Staff
        Utility.sendToStaff(ChatColor.AQUA + sender.getName() + " kicked " + args[0] + " for" + msg + ".");
    }
}
