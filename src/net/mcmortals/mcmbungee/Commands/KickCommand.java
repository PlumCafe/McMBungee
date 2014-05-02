package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Utility;
import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Statement;

public class KickCommand
        extends Command {
    private main m = new main();

    public KickCommand(main This) {
        super("kick", "");
        this.m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        try {
            if (m.hasPermission(sender,6)) {
                if (args.length>=2) {
                    try {
                        int w = 1;
                        String msg = "";
                        do {
                            msg = msg + " " + args[w];
                            w++;
                        } while (w < args.length);
                        ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
                        p.disconnect("§cYou have been kicked from the server!§r\n §cReason:§6" + msg);
                        Statement statement = m.connect.createStatement();
                        statement.executeUpdate("INSERT INTO McMInfractions (PlayerName, Enforcer, Type, Reason) VALUES " +
                                "('" + args[0] + "', '" + sender.getName() + "', '" + "Kick" + "', '" + msg + "')");
                        m.sendToStaff(ChatColor.AQUA + sender.getName() + " kicked " + args[0] + " for " + msg + ".");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else sender.sendMessage(Utility.prefix().append("Usage: §b/kick [Player] [Reason]").color(ChatColor.RED).create());
            } else sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
