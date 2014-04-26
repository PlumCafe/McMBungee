package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;

public class BanCommand
        extends Command {
    main m = new main();

    public BanCommand(main This) {
        super("ban", "");
        this.m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        try {
            if (m.hasPermission(sender,7)) {
                if (args.length>=2) {
                    Statement statement = this.m.connect.createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[0]+ "'");
                    if (res.next()) {
                        if (res.getInt("Banned")==1) {
                            sender.sendMessage(prefix().append("This player is already banned!").color(ChatColor.RED).create()); return;
                        }
                        int w = 1;
                        String msg = "";
                        do {
                            msg = msg + " " + args[w];
                            w++;
                        } while (w < args.length);
                        statement.executeUpdate("UPDATE McMPData SET Banned=1 WHERE PlayerName='" + args[0] + "'");
                        statement.executeUpdate("UPDATE McMPData SET BanReason='" + msg + "' WHERE PlayerName='" + args[0] + "'");
                        statement.executeUpdate("UPDATE McMPData SET BanUntil=-1 WHERE PlayerName='" + args[0]+ "'");
                        m.sendToStaff(ChatColor.AQUA + sender.getName() + " banned " + args[0] + " for" + msg + ".");
                        statement.executeUpdate("INSERT INTO McMInfractions (PlayerName, Enforcer, Type, Reason) VALUES " +
                                "('" + args[0] + "', '" + sender.getName() + "', '" + "Permanent ban" + "', '" + msg + "')");
                        try {
                            ProxyServer.getInstance().getPlayer(args[0]).disconnect("§cYou have been banned from the server!§r\n §cReason:§6" + msg + "\n§6Appeal on http://www.mcmortals.net!");
                        } catch (Exception ex) {}
                    } else sender.sendMessage(prefix().append("No such player has ever joined!").color(ChatColor.RED).create());
                } else sender.sendMessage(prefix().append("Usage: §b/ban [Player] [Reason]").color(ChatColor.RED).create());
            } else sender.sendMessage(prefix().append("You cannot do that!").color(ChatColor.RED).create());
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
    }


}
