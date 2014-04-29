package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;

public class Staff
        extends Command {
    private main m = null;

    public Staff(main This) {
        super("staff", "");
        this.m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(prefix() + ChatColor.GOLD + "" + ChatColor.BOLD + "Online staff:");
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (hasPermission(p)) {
                sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "> " + this.m.getPlayerDisplay(p) + ChatColor.GRAY + "" + ChatColor.ITALIC + " (Connected to " + p.getServer().getInfo().getName() + ")");
            }
        }
    }

    String prefix() {
        return ChatColor.DARK_RED + "[" + ChatColor.RED + "McM" + ChatColor.DARK_RED + "]";
    }

    Boolean hasPermission(ProxiedPlayer p) {
        try {
            Statement statement = this.m.connect.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + p.getName() + "'");
            return (res.next()) && (res.getInt("Rank") >= 4);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
