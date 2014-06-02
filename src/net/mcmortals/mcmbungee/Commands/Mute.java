package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Database;
import net.mcmortals.mcmbungee.Utility.DatabasePlayer;
import net.mcmortals.mcmbungee.Utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Calendar;

public class Mute extends Command {

    public Mute() {
        super("mute", "");
    }

    public void execute(CommandSender sender, String[] args) {
        //Permissions Check
        if (!Utility.hasPermission(sender,6)) {
            sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
            return;
        }
        //Argument Length Check
        if (!(args.length>=3)) {
            sender.sendMessage(Utility.prefix().append("Usage: §b/mute [Player] [Time] [Reason]").color(ChatColor.RED).create());
            return;
        }
        DatabasePlayer player = Database.getPlayer(args[0]);
        //Has Joined Check
        if (!player.exists()) {
            sender.sendMessage(Utility.prefix().append("No such player has ever joined!").color(ChatColor.RED).create());
            return;
        }
        //Is Muted Check
        if (player.isMuted()) {
            sender.sendMessage(Utility.prefix().append("This player is already muted!").color(ChatColor.RED).create());
            return;
        }
        //Correct Time Input Check
        if (textToSec(args[1]) == -1) {
            sender.sendMessage(Utility.prefix().append("Incorrect time input!").color(ChatColor.RED).create());
            return;
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
        //Mute Player
        player.mute(msg, c.getTimeInMillis());
        //Register Infraction
        Database.addInfraction(args[0], sender.getName(), Database.InfractionType.Mute, textToSec(args[1]) == 864000 ? "10d" : args[1], msg);
        //Alert Staff
        Utility.sendToStaff(ChatColor.AQUA + sender.getName() + " muted " + args[0] + " for " + (textToSec(args[1]) == 864000 ? "10d" : args[1]) + " for" + msg + ".");
        if(Utility.isOnline(args[0])) {
            //Alert Victim
            ProxyServer.getInstance().getPlayer(args[0]).sendMessage("§4[§cMcM§4] §cYou have been muted! Reason:§6" + msg);
            ProxyServer.getInstance().getPlayer(args[0]).sendMessage("§4[§cMcM§4] §cYour mute expires on: §6" + c.getTime().toGMTString().replace("GMT", "UTC"));
        }
    }

    int textToSec(String m) {
        try {
            if (m.substring(m.length()-1).equalsIgnoreCase("S")) {
                return Integer.parseInt(m.substring(0,m.length()-1)) < 864001 ? Integer.parseInt(m.substring(0,m.length()-1)) : 864000;
            }
            if (m.substring(m.length()-1).equalsIgnoreCase("M")) {
                return Integer.parseInt(m.substring(0,m.length()-1))*60 < 864001 ? Integer.parseInt(m.substring(0,m.length()-1))*60 : 864000;
            }
            if (m.substring(m.length()-1).equalsIgnoreCase("H")) {
                return Integer.parseInt(m.substring(0,m.length()-1))*3600 < 864001 ? Integer.parseInt(m.substring(0,m.length()-1))*3600 : 864000;
            }
            if (m.substring(m.length()-1).equalsIgnoreCase("D")) {
                return Integer.parseInt(m.substring(0,m.length()-1))*3600*24 < 864001 ? Integer.parseInt(m.substring(0,m.length()-1))*3600*24 : 864000;
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    } 
    

}
