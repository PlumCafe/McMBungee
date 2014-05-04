package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Utility;
import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class R
        extends Command {
    private main m = new main();

    public R(main This) {
        super("r", "");
        this.m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        //Argument Length Check
        if (args.length >= 1) {
            sender.sendMessage(Utility.prefix().append("Usage: ").color(ChatColor.RED).append("/r [Message]").color(ChatColor.AQUA).create());
        }
        //Has Anyone To Reply Check
        if (this.m.lstm.containsKey(sender)) {
            sender.sendMessage(Utility.prefix().append("Nobody to reply to!").color(ChatColor.RED).create());
        }
        try {
            CommandSender rec = this.m.lstm.get(sender);
            //Combine Message
            int w = 0;
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
            //Send To Sender
            sender.sendMessage(new ComponentBuilder("To ").color(ChatColor.LIGHT_PURPLE).
                    append(this.m.getPlayerDisplay(rec)).bold(false).
                    append(":").color(ChatColor.LIGHT_PURPLE).
                    append(msg).color(ChatColor.GRAY).create());
        } catch (Exception ex) {
            sender.sendMessage(Utility.prefix().append("That player is offline!").color(ChatColor.RED).create());
            ex.printStackTrace();
        }
    }
}
