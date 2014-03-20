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

public class KickCommand
        extends Command {
    main m = new main();

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
                        p.disconnect("§4[§cMcM§4]\n\n§c§lYou have been kicked from the server!§r\n\n §eReason:§f" + msg + "\n\n§6Fell free to reconnect, provided you will follow the rules!");
                        m.sendToStaff(ChatColor.AQUA + sender.getName() + " kicked " + args[0] + " for " + msg + ".");
                    } catch (Exception ex) {}
                } else sender.sendMessage(prefix().append("Usage: /kick [Player] [Reason]").color(ChatColor.RED).create());
            } else sender.sendMessage(prefix().append("You cannot do that!").color(ChatColor.RED).create());
        } catch (Exception ex) {}
    }

    public ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
    }
}
