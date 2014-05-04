package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Utility;
import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Msg
        extends Command {
    private main m = new main();

    public Msg(main This) {
        super("msg", "", "tell", "w");
        this.m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        //Argument Length Check
        if (args.length >= 2) {
            sender.sendMessage(Utility.prefix().append("Usage: ").color(ChatColor.RED).append("/msg [Receiver] [Message]").color(ChatColor.AQUA).create());
        }
        try {
            ProxiedPlayer rec = ProxyServer.getInstance().getPlayer(args[0]);
            //Is Target Self Check
            if (rec.equals(sender)) {
                sender.sendMessage(Utility.prefix().append("Cannot message yourself!").color(ChatColor.RED).create());
            }
            //Can Msg YT Check
            if (m.hasPermission(sender,2) && !m.hasPermission(sender,6)) {
                sender.sendMessage(Utility.prefix().append("You cannot directly message ").color(ChatColor.RED)
                        .append("You").color(ChatColor.WHITE).append("Tubers.").color(ChatColor.RED).create());
            }
            //Combine Message
            int w = 1;
            String msg = "";
            do {
                msg = msg + " " + args[w];
                w++;
            } while (w < args.length);
            //Send To Reciever
            rec.sendMessage(new ComponentBuilder("From ").color(ChatColor.LIGHT_PURPLE).
                    append(this.m.getPlayerDisplay(sender)).bold(false).
                    append(":").color(ChatColor.LIGHT_PURPLE).
                    append(msg).color(ChatColor.GRAY).create());
            //Send to Sender
            sender.sendMessage(new ComponentBuilder("To ").color(ChatColor.LIGHT_PURPLE).
                    append(this.m.getPlayerDisplay(rec)).bold(false).
                    append(":").color(ChatColor.LIGHT_PURPLE).
                    append(msg).color(ChatColor.GRAY).create());
            //Add To Reply List
            this.m.lstm.put(rec, sender);
        } catch (Exception ex) {
            sender.sendMessage(Utility.prefix().append("That player is offline!").color(ChatColor.RED).create());
        }
    }
}
