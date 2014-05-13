package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Msg extends Command {

    public Msg() {
        super("msg", "", "tell", "w");
    }

    public void execute(CommandSender sender, String[] args) {
        //Argument Length Check
        if (!(args.length >= 2)) {
            sender.sendMessage(Utility.prefix().append("Usage: ").color(ChatColor.RED).append("/msg [Receiver] [Message]").color(ChatColor.AQUA).create());
        }
        //Is Online Check
        if(!Utility.isOnline(args[0])){
            sender.sendMessage(Utility.prefix().append("That player is offline!").color(ChatColor.RED).create());
            return;
        }
        ProxiedPlayer rec = ProxyServer.getInstance().getPlayer(args[0]);
        //Is Target Self Check
        if (rec.equals(sender)) {
            sender.sendMessage(Utility.prefix().append("Cannot message yourself!").color(ChatColor.RED).create());
        }
        //Can Msg YT Check
        if (Utility.hasPermission(sender,2) && !Utility.hasPermission(sender,6)) {
            sender.sendMessage(Utility.prefix().append("You cannot directly message ").color(ChatColor.RED)
                    .append("YouTubers.").color(ChatColor.GOLD).create());
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
                append(Utility.getPlayerDisplay(sender)).bold(false).
                append(":").color(ChatColor.LIGHT_PURPLE).
                append(msg).color(ChatColor.GRAY).create());
        //Send to Sender
        sender.sendMessage(new ComponentBuilder("To ").color(ChatColor.LIGHT_PURPLE).
                append(Utility.getPlayerDisplay(rec)).bold(false).
                append(":").color(ChatColor.LIGHT_PURPLE).
                append(msg).color(ChatColor.GRAY).create());
        //Add To Reply List
        Utility.replies.put(rec, sender);
    }
}
