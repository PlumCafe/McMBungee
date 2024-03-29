package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class S extends Command {

    public S() {
        super("s", "");
    }

    public void execute(CommandSender sender, String[] args) {
        //Permissions Check
        if (!Utility.hasPermission(sender,6)) {
            sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create()); return;
        }
        //Argument Length Check
        if (!(args.length>=1)) {
            sender.sendMessage(Utility.prefix().append("Usage: §b/s [Message]").color(ChatColor.RED).create()); return;
        }
        //Combine Message
        int w = 0;
        String msg = "";
        do {
            msg = msg + " " + args[w];
            w++;
        } while (w < args.length);
            Utility.sendToStaff(Utility.getPlayerDisplay(sender) + " §b§l>§f" + msg);
    }
}
