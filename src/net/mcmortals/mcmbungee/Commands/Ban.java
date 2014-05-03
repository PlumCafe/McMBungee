package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Database;
import net.mcmortals.mcmbungee.Utility.DatabasePlayer;
import net.mcmortals.mcmbungee.Utility.Utility;
import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ban extends Command {

    private main m = new main();

    public Ban() {
        super("ban", "");
    }

    public void execute(CommandSender sender, String[] args) {
        try {
            //Permissions Check
            if (!m.hasPermission(sender,7)) {
                sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
                return;
            }
            //Argument Length Check
            if (args.length>=2) {
                sender.sendMessage(Utility.prefix().append("Usage: §b/ban [Player] [Reason]").color(ChatColor.RED).create());
                return;
            }
            Statement statement = this.m.connect.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[0]+ "'");
            //Has Joined Check
            if (!res.isBeforeFirst()) {
                sender.sendMessage(Utility.prefix().append("No such player has ever joined!").color(ChatColor.RED).create());
                return;
            }
            //Is Banned Check
            if (res.getInt("Banned")==1) {
                sender.sendMessage(Utility.prefix().append("This player is already banned!").color(ChatColor.RED).create());
                return;
            }
            //Combine Message
            int w = 1;
            String msg = "";
            do {
                msg = msg + " " + args[w];
                w++;
            } while (w < args.length);
            //Ban Player
            DatabasePlayer p = new Database(m).getPlayer(args[0]); p.ban(msg, -1);
            //Alert Staff
            m.sendToStaff(ChatColor.AQUA + sender.getName() + " banned " + args[0] + " for" + msg + ".");
            //Register An Infraction
            statement.executeUpdate("INSERT INTO McMInfractions (PlayerName, Enforcer, Type, Reason) VALUES " +
                    "('" + args[0] + "', '" + sender.getName() + "', '" + "Permanent ban" + "', '" + msg + "')");
            //Disconnect The Player
            try {
                ProxyServer.getInstance().getPlayer(args[0]).disconnect(new ComponentBuilder("You have been banned from the server!").color(ChatColor.RED)
                        .append("\n").append("Reason:").color(ChatColor.RED).append(msg).color(ChatColor.GOLD).append("\n")
                        .append("Appeal on http://www.mcmortals.net!").color(ChatColor.GOLD).create());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
