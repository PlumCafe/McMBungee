package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Database;
import net.mcmortals.mcmbungee.Utility.DatabasePlayer;
import net.mcmortals.mcmbungee.Utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Calendar;

public class Tempban extends Command {

    public Tempban() {
        super("tempban", "", "tban");
    }

    public void execute(CommandSender sender, String[] args) {
        //Permissions Check
        if (!Utility.hasPermission(sender,7)) {
            sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
        }
        //Argument Length Check
        if (!(args.length>=3)) {
            sender.sendMessage(Utility.prefix().append("Usage: §b/tempban [Player] [Time] [Reason]").color(ChatColor.RED).create());
        }
        DatabasePlayer player = Database.getPlayer(args[0]);
        //Has Joined Check
        if (!player.exists()) {
            sender.sendMessage(Utility.prefix().append("No such player has ever joined!").color(ChatColor.RED).create());
        }
        //Is Banned Check
        if (player.isBanned()) {
            sender.sendMessage(Utility.prefix().append("This player is already banned!").color(ChatColor.RED).create()); return;
        }
        //Is Time Input Correct
        if (textToSec(args[1])==-1) {
            sender.sendMessage(Utility.prefix().append("Incorrect time input!").color(ChatColor.RED).create()); return;
        }
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, textToSec(args[1]));
        //Combine Message
        int w = 2;
        String msg = "";
        do {
            msg = msg + " " + args[w];
            w++;
        } while (w < args.length);
        //Ban Player
        player.ban(msg, c.getTimeInMillis());
        //Register an infraction
        Database.addInfraction(args[0], sender.getName(), Database.InfractionType.Temporary_Ban, args[1], msg);
        //Alert Staff
        Utility.sendToStaff(ChatColor.AQUA + sender.getName() + " banned " + args[0] + " for " + args[1] + " for" + msg + ".");
        //Is Online Check
        if(Utility.isOnline(args[0])) {
            //Disconnect Player
            ProxyServer.getInstance().getPlayer(args[0]).disconnect("§cYou have been temporarily banned from the server!§r\n §cReason:§6"
                    + msg + "\n§cBanned until: §6" + c.getTime().toGMTString().replace("GMT", "UTC") + "\n§6Appeal on http://www.mcmortals.net!");
        }
    }

    int textToSec(String m) {
        try {
            if (m.substring(m.length()-1).equalsIgnoreCase("S")) {
                return Integer.parseInt(m.substring(0,m.length()-1));
            }
            if (m.substring(m.length()-1).equalsIgnoreCase("M")) {
                return Integer.parseInt(m.substring(0,m.length()-1))*60;
            }
            if (m.substring(m.length()-1).equalsIgnoreCase("H")) {
                return Integer.parseInt(m.substring(0,m.length()-1))*3600;
            }
            if (m.substring(m.length()-1).equalsIgnoreCase("D")) {
                return Integer.parseInt(m.substring(0,m.length()-1))*3600*24;
            }
        }
        catch (Exception ignored) {

        }
        return -1;
    } 
    

}
