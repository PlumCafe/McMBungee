package net.mcmortals.mcmbungee.Commands;

import java.util.HashMap;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MessageMsg
        extends Command {
    main m = new main();

    public MessageMsg(main This) {
        super("msg", "", "tell", "w");
        this.m = This;
    }

    ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            try {
                ProxiedPlayer rec = ProxyServer.getInstance().getPlayer(args[0]);
                if (rec.equals(sender)) {
                    sender.sendMessage(prefix().append("Cannot message yourself!").color(ChatColor.RED).create());
                }
                if ((sender.hasPermission("mcm.yt") & !sender.hasPermission("mcm.helper"))) {
                    sender.sendMessage(prefix().append("You cannot directly message ").color(ChatColor.RED).append("You").color(ChatColor.WHITE).append("Tubers.").color(ChatColor.RED).create());
                }
                int w = 1;
                String msg = "";
                do {
                    msg = msg + " " + args[w];
                    w++;
                } while (w < args.length);
                if ((sender instanceof ProxiedPlayer)) {
                    ProxiedPlayer p = (ProxiedPlayer) sender;
                    rec.sendMessage(new ComponentBuilder(this.m.getPlayerDisplay(p, ChatColor.GRAY, false)).append(" -> ").color(ChatColor.GRAY).bold(true).append(this.m.getPlayerDisplay(rec, ChatColor.WHITE, false)).append(" : ").color(ChatColor.GRAY).bold(true).append(msg).create());


                    sender.sendMessage(new ComponentBuilder("�7" + this.m.getPlayerDisplay(p, ChatColor.GRAY, false)).append(" -> ").color(ChatColor.GRAY).bold(true).append(this.m.getPlayerDisplay(rec, ChatColor.WHITE, false)).append(" : ").color(ChatColor.GRAY).bold(true).append(msg).create());


                    this.m.lstm.put(rec, sender);
                } else {
                    rec.sendMessage(new ComponentBuilder("Console ").color(ChatColor.GRAY).append(" -> ").color(ChatColor.GRAY).bold(true).append(this.m.getPlayerDisplay(rec, ChatColor.WHITE, false)).append(" : ").color(ChatColor.GRAY).bold(true).append(msg).create());


                    sender.sendMessage(new ComponentBuilder("Console ").color(ChatColor.GRAY).append(" -> ").color(ChatColor.GRAY).bold(true).append(this.m.getPlayerDisplay(rec, ChatColor.WHITE, false)).append(" : ").color(ChatColor.GRAY).bold(true).append(msg).create());


                    this.m.lstm.put(rec, sender);
                }
            } catch (Exception ex) {
                sender.sendMessage(prefix().append("That player is offline!").color(ChatColor.RED).create());
            }
        } else {
            sender.sendMessage(prefix().append("Usage: ").color(ChatColor.RED).append("/msg [Receiver] [Message]").color(ChatColor.AQUA).create());
        }
    }
}
