package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Utility;
import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class MessageR
        extends Command {
    private main m = new main();

    public MessageR(main This) {
        super("r", "");
        this.m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 1) {
            if (this.m.lstm.containsKey(sender)) {
                try {
                    CommandSender rec = this.m.lstm.get(sender);
                    if (rec.equals(sender)) {
                        sender.sendMessage(Utility.prefix().append("Cannot message yourself!").color(ChatColor.RED).create());
                    }
                    if ((sender.hasPermission("mcm.yt") & !sender.hasPermission("mcm.helper"))) {
                        sender.sendMessage(Utility.prefix().append("You cannot directly message ").color(ChatColor.RED).append("You").color(ChatColor.WHITE).append("Tubers.").color(ChatColor.RED).create());
                    }
                    int w = 0;
                    String msg = "";
                    do {
                        msg = msg + " " + args[w];
                        w++;
                    } while (w < args.length);
                    rec.sendMessage(
                            new ComponentBuilder("From ").color(ChatColor.LIGHT_PURPLE).
                                    append(this.m.getPlayerDisplay(sender)).bold(false).
                                    append(":").color(ChatColor.LIGHT_PURPLE).
                                    append(msg).color(ChatColor.GRAY).create()
                    );
                    sender.sendMessage(
                            new ComponentBuilder("To ").color(ChatColor.LIGHT_PURPLE).
                                    append(this.m.getPlayerDisplay(rec)).bold(false).
                                    append(":").color(ChatColor.LIGHT_PURPLE).
                                    append(msg).color(ChatColor.GRAY).create()
                    );
                    this.m.lstm.put(rec, sender);
                } catch (Exception ex) {
                    sender.sendMessage(Utility.prefix().append("That player is offline!").color(ChatColor.RED).create());
                    ex.printStackTrace();
                }
            } else {
                sender.sendMessage(Utility.prefix().append("Nobody to reply to!").color(ChatColor.RED).create());
            }
        } else {
            sender.sendMessage(Utility.prefix().append("Usage: ").color(ChatColor.RED).append("/r [Message]").color(ChatColor.AQUA).create());
        }
    }
}
