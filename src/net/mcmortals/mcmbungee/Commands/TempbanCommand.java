package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

public class TempbanCommand
        extends Command {
    private main m = new main();

    public TempbanCommand(main This) {
        super("tempban", "", "tban");
        this.m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        try {
            if (m.hasPermission(sender,7)) {
                if (args.length>=3) {
                    Statement statement = this.m.connect.createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[0]+ "'");
                    if (res.next()) {
                        if (res.getInt("Banned")==1) {
                            sender.sendMessage(prefix().append("This player is already banned!").color(ChatColor.RED).create()); return;
                        }
                        if (textToSec(args[1])==-1) {
                            sender.sendMessage(prefix().append("Incorrect time input!").color(ChatColor.RED).create()); return;
                        }
                        Calendar c= Calendar.getInstance();
                        c.add(Calendar.SECOND, textToSec(args[1]));
                        int w = 2;
                        String msg = "";
                        do {
                            msg = msg + " " + args[w];
                            w++;
                        } while (w < args.length);
                        statement.executeUpdate("UPDATE McMPData SET Banned=1 WHERE PlayerName='" + args[0] + "'");
                        statement.executeUpdate("UPDATE McMPData SET BanReason='" + msg + "' WHERE PlayerName='" + args[0] + "'");
                        statement.executeUpdate("UPDATE McMPData SET BanUntil=" + c.getTimeInMillis() + " WHERE PlayerName='" + args[0]+ "'");
                        statement.executeUpdate("INSERT INTO McMInfractions (PlayerName, Enforcer, Type, Time, Reason) VALUES " +
                        "('" + args[0] + "', '" + sender.getName() + "', '" + "Temporary ban" + "', '" + args[1] + "', '" + msg + "')");
                        m.sendToStaff(ChatColor.AQUA + sender.getName() + " banned " + args[0] + " for " + args[1] + " for" + msg + ".");
                        try {
                            ProxyServer.getInstance().getPlayer(args[0]).disconnect("§cYou have been temporarily banned from the server!§r\n §cReason:§6" + msg + "\n§cBanned until: §6" + c.getTime().toGMTString().replace("GMT", "UTC") +"\n§6Appeal on http://www.mcmortals.net!");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else sender.sendMessage(prefix().append("No such player has ever joined!").color(ChatColor.RED).create());
                } else sender.sendMessage(prefix().append("Usage: §b/tempban [Player] [Time] [Reason]").color(ChatColor.RED).create());
            } else sender.sendMessage(prefix().append("You cannot do that!").color(ChatColor.RED).create());
        } catch (Exception ex) {ex.printStackTrace();}
    }

    ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
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
