package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Utility;
import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

public class MuteCommand
        extends Command {
    private main m = new main();

    public MuteCommand() {
        super("mute", "");
    }

    public void execute(CommandSender sender, String[] args) {
        try {
            if (m.hasPermission(sender,6)) {
                if (args.length>=3) {
                    Statement statement = this.m.connect.createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[0]+ "'");
                    if (res.next()) {
                        if (res.getInt("Muted")==1) {
                            sender.sendMessage(Utility.prefix().append("This player is already muted!").color(ChatColor.RED).create()); return;
                        }
                        if (textToSec(args[1])==-1) {
                            sender.sendMessage(Utility.prefix().append("Incorrect time input!").color(ChatColor.RED).create()); return;
                        }
                        Calendar c= Calendar.getInstance();
                        c.add(Calendar.SECOND, textToSec(args[1]));
                        int w = 2;
                        String msg = "";
                        do {
                            msg = msg + " " + args[w];
                            w++;
                        } while (w < args.length);
                        statement.executeUpdate("UPDATE McMPData SET Muted=1 WHERE PlayerName='" + args[0] + "'");
                        statement.executeUpdate("UPDATE McMPData SET MuteReason='" + msg + "' WHERE PlayerName='" + args[0] + "'");
                        statement.executeUpdate("UPDATE McMPData SET MuteUntil=" + c.getTimeInMillis() + " WHERE PlayerName='" + args[0]+ "'");
                        statement.executeUpdate("INSERT INTO McMInfractions (PlayerName, Enforcer, Type, Time, Reason) VALUES " +
                        "('" + args[0] + "', '" + sender.getName() + "', '" + "Mute" + "', '" + args[1] + "', '" + msg + "')");
                        m.sendToStaff(ChatColor.AQUA + sender.getName() + " muted " + args[0] + " for " + args[1] + " for" + msg + ".");
                        try {
                            ProxyServer.getInstance().getPlayer(args[0]).sendMessage("§4[§cMcM§4] §cYou have been muted! Reason:§6" + msg);
                            ProxyServer.getInstance().getPlayer(args[0]).sendMessage("§4[§cMcM§4] §cYour mute expires on: §6" +c.getTime().toGMTString().replace("GMT", "UTC"));
                            m.muted.add(args[0].toLowerCase());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else sender.sendMessage(Utility.prefix().append("No such player has ever joined!").color(ChatColor.RED).create());
                } else sender.sendMessage(Utility.prefix().append("Usage: §b/mute [Player] [Time] [Reason]").color(ChatColor.RED).create());
            } else sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
        } catch (Exception ex) {ex.printStackTrace();}
    }

    int textToSec(String m) {
        try {
        if (m.substring(m.length()-1).equalsIgnoreCase("S")) {
            return Integer.parseInt(m.substring(0,m.length()-1));
        } else if (m.substring(m.length()-1).equalsIgnoreCase("M")) {
            return Integer.parseInt(m.substring(0,m.length()-1))*60;
        } else if (m.substring(m.length()-1).equalsIgnoreCase("H")) {
            return Integer.parseInt(m.substring(0,m.length()-1))*3600;
        } else if (m.substring(m.length()-1).equalsIgnoreCase("D")) {
            return Integer.parseInt(m.substring(0,m.length()-1))*3600*24;
        }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    } 
    

}
