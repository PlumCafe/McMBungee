package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Database;
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

public class Bna extends Command {

    private main m;

    public Bna(main main) {
        super("bna", "");
        m = main;
    }

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(new ComponentBuilder("Such Hypixel's, not working, still awesome!").color(ChatColor.GOLD).create());
    }
}
