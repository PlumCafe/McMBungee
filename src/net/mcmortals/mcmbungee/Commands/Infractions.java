package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Database;
import net.mcmortals.mcmbungee.Utility.Infraction;
import net.mcmortals.mcmbungee.Utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class Infractions extends Command {

    public Infractions() {
        super("infractions", "", "infr");
    }

    public void execute(CommandSender sender, String[] args) {
        //Permissions Check
        if (!Utility.hasPermission(sender, 6)) {
            sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
            return;
        }
        //Argument Length Check
        if (args.length != 1) {
            sender.sendMessage(Utility.prefix().append("Usage: ").color(ChatColor.RED).append("/infractions [Player]").color(ChatColor.AQUA).create());
            return;
        }
        sender.sendMessage(Utility.prefix().append("Infractions for: ").color(ChatColor.GOLD).bold(true).append(args[0]).create());
        try {
            for (Infraction i : Database.getInfractions(args[0], Database.InfractionType.all)) {
                sender.sendMessage(new ComponentBuilder(i.getType() + ": ").color(ChatColor.RED).bold(true).append("Reason:")
                        .color(ChatColor.AQUA).bold(false).append(i.getReason()).color(ChatColor.GOLD).create());
                String time = i.getTime();
                ComponentBuilder m = new ComponentBuilder("- Issued by ").color(ChatColor.AQUA).append(i.getEnforcer()).color(ChatColor.GOLD);
                if (time == null) {
                    sender.sendMessage(m.create());
                } else {
                    sender.sendMessage(m.append(" for ").color(ChatColor.AQUA).append(time).color(ChatColor.GOLD).create());
                }
            }
        }catch(Exception e){
            sender.sendMessage(Utility.prefix().append("That player has no registered Infractions!").color(ChatColor.RED).create());
            e.printStackTrace();
        }
    }
}