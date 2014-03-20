package net.mcmortals.mcmbungee.Commands;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;

public class Staff
        extends Command {
    main m = null;

    public Staff(main This) {
        super("staff", "");
        this.m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(prefix() + ChatColor.GOLD + "" + ChatColor.BOLD + "Online staff:");
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (hasPermission(p, Integer.valueOf(4))) {
                sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "> " + this.m.getPlayerDisplay(p, ChatColor.WHITE, false) + ChatColor.GRAY + "" + ChatColor.ITALIC + " (Connected to " + p.getServer().getInfo().getName() + ")");
            }
        }
    }

    public String prefix() {
        return ChatColor.DARK_RED + "[" + ChatColor.RED + "McM" + ChatColor.DARK_RED + "]";
    }

    public Boolean hasPermission(ProxiedPlayer p, Integer i) {
        try {
            Statement statement = this.m.connect.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + p.getName() + "'");
            if ((res.next()) && (res.getInt("Rank") >= i.intValue())) return true;
            return false;
        } catch (Exception ex) {
        }
        return false;
    }
}
