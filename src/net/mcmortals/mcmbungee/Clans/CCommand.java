package net.mcmortals.mcmbungee.Clans;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;

public class CCommand extends Command {

    private main m = null;
    public CCommand(main This) {
        super("clan", "", "c", "clans");
        m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            if(args.length==0) {

            } else if (args[0].equalsIgnoreCase("create")) {
                if (args.length==2) {
                    if (!isInAClan((ProxiedPlayer) sender)) {
                        try {
                            Statement s = this.m.connect.createStatement();
                            s.executeUpdate("INSERT INTO McMClan (Name, Owner) VALUES ('" + args[1] +"', '" + sender.getName() +"');");
                            ResultSet res = s.executeQuery("SELECT * FROM McMClan WHERE Name='" + args[1] + "'");
                            if (res.next()) {
                                s.executeUpdate("UPDATE McMPData SET ClanID=" + res.getInt("ID") + "WHERE PlayerName='" + sender.getName() + "'");
                                sender.sendMessage(prefix().append("Created clan ").color(ChatColor.GREEN).append(args[1]).color(ChatColor.BOLD).append(" successfully!").color(ChatColor.GREEN).create());
                            }
                        } catch (Exception ex) {}
                    } else sender.sendMessage(prefix().append("You are already in a clan! Do /c leave to leave it.").color(ChatColor.RED).create());
                } else sender.sendMessage(prefix().append("Usage: /c create [Clan Name]").color(ChatColor.RED).create());
            }
        } else sender.sendMessage(prefix().append("You cannot create a class console!").color(ChatColor.RED).create());
    }

    public ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
    }

    boolean isInAClan(ProxiedPlayer p) {
        try {
            Statement statement = this.m.connect.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + p.getName() + "'");
            if (res.next()) {
                return res.getInt("ClanID")!=-1;
            }
        } catch (Exception ex) {return false;}
        return false;
    }
}
