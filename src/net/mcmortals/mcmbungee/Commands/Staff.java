package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Utility;
import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;

public class Staff
        extends Command {
    private main m = new main();

    public Staff() {
        super("staff", "");
    }

    public void execute(CommandSender sender, String[] args) {
        //Send Online Staff :O
        sender.sendMessage(Utility.prefix().append("Online Staff:").color(ChatColor.GOLD).bold(true).create());
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (hasPermission(p)) {
                sender.sendMessage(new ComponentBuilder("> ").color(ChatColor.GOLD).bold(true).append(this.m.getPlayerDisplay(p))
                        .append(" (Connected to " + p.getServer().getInfo().getName() + ")").color(ChatColor.GRAY).italic(true).create());
            }
        }
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
