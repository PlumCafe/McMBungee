package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Database;
import net.mcmortals.mcmbungee.Utility.DatabasePlayer;
import net.mcmortals.mcmbungee.Utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Unmute extends Command {

    public Unmute() {
        super("unmute", "");
    }

    public void execute(CommandSender sender, String[] args) {
        //Permissions Check
        if (!Utility.hasPermission(sender,6)) {
            sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
            return;
        }
        //Argument Length Check
        if (args.length!=1) {
            sender.sendMessage(Utility.prefix().append("Usage: /unmute [Player]").color(ChatColor.RED).create());
            return;
        }
        DatabasePlayer player = Database.getPlayer(args[0]);
        //Has Joined Check
        if (!player.exists()) {
            sender.sendMessage(Utility.prefix().append("No such player has ever joined!").color(ChatColor.RED).create());
            return;
        }
        //Is Muted Check
        if (!player.isMuted()) {
            sender.sendMessage(Utility.prefix().append("This player is not muted!").color(ChatColor.RED).create());
            return;
        }
        //Unmute Player
        player.unmute();
        //Alert Staff
        Utility.sendToStaff(ChatColor.AQUA  + sender.getName() + " unmuted " + args[0] + ".");
    }
}
