package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Database;
import net.mcmortals.mcmbungee.Utility.DatabasePlayer;
import net.mcmortals.mcmbungee.Utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Mcm extends Command {

    public Mcm() {
        super("mcm", "");
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Utility.prefix().append("MCMortals created by Bocktrow and Red_Epicness!").color(ChatColor.GREEN).create());
            return;
        }
        if (args[0].equalsIgnoreCase("addtokens")) {
            if (!Utility.hasPermission(sender, 7)) {
                sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
                return;
            }
            if (args.length != 3){
                sender.sendMessage(Utility.prefix().append("Usage: §b/mcm addtokens [Player] [Tokens]").color(ChatColor.RED).create());
                return;
            }
            DatabasePlayer player = Database.getPlayer(args[1]);
            if (!player.exists()){
                sender.sendMessage(Utility.prefix().append("No such players has ever joined!").color(ChatColor.RED).create());
                return;
            }
            try {
                player.addTokens(Integer.parseInt(args[2]));
                sender.sendMessage(Utility.prefix()
                        .append("Incremented " + args[1] + "'s tokens by " + args[2] + "(" + player.getTokens() + ")").color(ChatColor.GREEN).create());
            } catch (Exception ex) {
                sender.sendMessage(Utility.prefix().append("NaN: "+args[2]).color(ChatColor.RED).create());
            }
            return;
        }
        if (args[0].equalsIgnoreCase("settokens")) {
            if (!Utility.hasPermission(sender, 7)) {
                sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
                return;
            }
            if (args.length != 3) {
                sender.sendMessage(Utility.prefix().append("Usage: §b/mcm settokens [Player] [Tokens]").color(ChatColor.RED).create());
                return;
            }
            DatabasePlayer player = Database.getPlayer(args[1]);
            if (!player.exists()) {
                sender.sendMessage(Utility.prefix().append("No such players has ever joined!").color(ChatColor.RED).create());
                return;
            }
            try {
                player.setTokens(Integer.parseInt(args[2]));
                sender.sendMessage(Utility.prefix().append("Set " + args[1] + "'s tokens to " + args[2] + "").color(ChatColor.GREEN).create());
            }catch(Exception e){
                sender.sendMessage(Utility.prefix().append("NaN: "+args[2]).color(ChatColor.RED).create());
            }
            return;
        }
        if (args[0].equalsIgnoreCase("setrank")) {
            if (!Utility.hasPermission(sender, 9)){
                sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
                return;
            }
            if (args.length != 3){
                sender.sendMessage(Utility.prefix().append("Usage: §b/mcm setrank [Player] [RankID]").color(ChatColor.RED).create());
                return;
            }
            DatabasePlayer player = Database.getPlayer(args[1]);
            if (!player.exists()) {
                sender.sendMessage(Utility.prefix().append("No such players has ever joined!").color(ChatColor.RED).create());
                return;
            }
            try {
                player.setRank(Integer.parseInt(args[2]));
                Utility.setRank(args[1], Integer.parseInt(args[2]));
                Utility.sendToStaff(ChatColor.AQUA + "Set " + args[1] + "'s rank to " + getRank(Integer.parseInt(args[2])) + ChatColor.GREEN + "");

            } catch (Exception ex) {
                sender.sendMessage(Utility.prefix().append("NaN: "+args[2]).color(ChatColor.RED).create());
            }
        }
    }

    public static String getRank(Integer id) {
        switch (id){
            case (10): return ChatColor.DARK_RED + "Operator";
            case (9):return ChatColor.DARK_PURPLE + "Developer";
            case (8):return ChatColor.RED + "Administrator";
            case (7):return ChatColor.DARK_GREEN + "Moderator";
            case (6):return ChatColor.BLUE + "Helper";
            case (5):return ChatColor.GREEN + "Host";
            case (4):return ChatColor.DARK_AQUA + "Builder";
            case (3):return ChatColor.GOLD + "YouTuber";
            case (2):return ChatColor.LIGHT_PURPLE + "Immortal";
            case (1):return ChatColor.GREEN + "Mortals";
            default:return "Normal";
        }
    }
}
