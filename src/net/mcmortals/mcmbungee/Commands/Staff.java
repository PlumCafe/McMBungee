package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Staff extends Command {

    public Staff() {
        super("staff", "");
    }

    public void execute(CommandSender sender, String[] args) {
        //Send Online Staff :O
        sender.sendMessage(Utility.prefix().append("Online Staff:").color(ChatColor.GOLD).bold(true).create());
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (Utility.hasPermission(p, 4)) {
                sender.sendMessage(new ComponentBuilder("> ").color(ChatColor.GOLD).bold(true).append(Utility.getPlayerDisplay(p))
                        .append(" (Connected to " + p.getServer().getInfo().getName() + ")").color(ChatColor.GRAY).italic(true).create());
            }
        }
    }
}
