package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Utility;
import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;
import java.sql.Statement;

public class Kick extends Command {

    private main m;

    public Kick(main main) {
        super("kick", "");
        m = main;
    }

    public void execute(CommandSender sender, String[] args) {
        //Permissions Check
        if (Utility.hasPermission(sender,6)) {
            sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
            return;
        }
        //Argument Length Check
        if (args.length>=2) {
            sender.sendMessage(Utility.prefix().append("Usage: §b/kick [Player] [Reason]").color(ChatColor.RED).create());
            return;
        }
        String msg = "";
        try {
            //Combine Message
            int w = 1;
            do {
                msg = msg + " " + args[w];
                w++;
            } while (w < args.length);
            //Kick Player
            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
            p.disconnect(new ComponentBuilder("You have been kicked from the server!").color(ChatColor.RED).append("\n")
                    .append("Reason:").color(ChatColor.RED).append(msg).color(ChatColor.GOLD).create());
        } catch (Exception e){
            //Report If Target Is Offline
            e.printStackTrace();
            sender.sendMessage(new ComponentBuilder("That player is not online.").color(ChatColor.RED).create());
        }
        try {
            //Register an Infraction
            Statement statement = Utility.getConnection().createStatement();
            statement.executeUpdate("INSERT INTO McMInfractions (PlayerName, Enforcer, Type, Reason) VALUES " +
                    "('" + args[0] + "', '" + sender.getName() + "', '" + "Kick" + "', '" + msg + "')");
            //Alert Staff
            m.sendToStaff(ChatColor.AQUA + sender.getName() + " kicked " + args[0] + " for " + msg + ".");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
