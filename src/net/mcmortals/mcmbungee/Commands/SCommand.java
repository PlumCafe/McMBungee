package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class SCommand
        extends Command {
    private main m = new main();

    public SCommand(main This) {
        super("s", "");
        this.m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        try {
            if (m.hasPermission(sender,6)) {
                if (args.length>=1) {
                    int w = 0;
                    String msg = "";
                    do {
                        msg = msg + " " + args[w];
                        w++;
                    } while (w < args.length);
                    if (m.hasPermission(sender,8)) {
                        m.sendToStaff(m.getPlayerDisplay(sender) + " §b§l>§f" + msg);
                    } else m.sendToStaff(m.getPlayerDisplay(sender) + " §f§l>§f" + msg);
                } else sender.sendMessage(prefix().append("Usage: §b/s [Message]").color(ChatColor.RED).create());
            } else sender.sendMessage(prefix().append("You cannot do that!").color(ChatColor.RED).create());
        } catch (Exception ex) {ex.printStackTrace();}
    }

    ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
    }
}
