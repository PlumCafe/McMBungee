package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Utility;
import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;

public class McMCommand extends Command {

    private main m;

    public McMCommand(main main) {
        super("mcm", "");
        m = main;
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Utility.prefix().append("MCMortals created by Bocktrow and Red_Epicness!").color(ChatColor.GREEN).create());
        } else if (args[0].equalsIgnoreCase("addtokens")) {
            if (Utility.hasPermission(sender, 7)) {
                if (args.length == 3) {
                    try {
                        Statement statement = Utility.getConnection().createStatement();
                        ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[1] + "'");
                        if (res.next()) {
                            statement.executeUpdate("UPDATE McMPData SET Tokens='" + (res.getInt("Tokens") + Integer.parseInt(args[2])) + "' WHERE PlayerName='" + args[1] + "'");
                            sender.sendMessage(Utility.prefix().append("Incremented " + args[1] + "'s tokens by " + args[2] + "").color(ChatColor.GREEN).create());
                        } else {
                            sender.sendMessage(Utility.prefix().append("No such players has ever joined!").color(ChatColor.RED).create());
                        }
                    } catch (Exception ex) {
                        sender.sendMessage(Utility.prefix().append("An error occured!").color(ChatColor.RED).create());
                    }
                } else {
                    sender.sendMessage(Utility.prefix().append("Usage: §b/mcm addtokens [Player] [Tokens]").color(ChatColor.RED).create());
                }
            } else {
                sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
            }
            if (args.length == 3) {
                try {
                    Statement statement = Utility.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM MCMPlayers WHERE PlayerName='" + args[1] + "'");
                    if (res.next()) {
                        statement.executeUpdate("UPDATE MCMPlayers SET Tokens='" + (res.getInt("Tokens") + Integer.parseInt(args[2])) + "' WHERE PlayerName='" + args[1] + "'");
                        sender.sendMessage(Utility.prefix().append("Incremented " + args[1] + "'s tokens by " + args[2] + "").color(ChatColor.GREEN).create());
                    } else {
                        sender.sendMessage(Utility.prefix().append("No such players has ever joined!").color(ChatColor.RED).create());
                    }
                } catch (Exception ex) {
                    sender.sendMessage(Utility.prefix().append("An error occured!").color(ChatColor.RED).create());
                }
            } else {
                sender.sendMessage(Utility.prefix().append("Usage: §b/mcm addtokens [Player] [Tokens]").color(ChatColor.RED).create());
            }
        } else if (args[0].equalsIgnoreCase("settokens")) {
            if (Utility.hasPermission(sender, 7)) {
                if (args.length == 3) {
                    try {
                        Statement statement = Utility.getConnection().createStatement();
                        ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[1] + "'");
                        if (res.next()) {
                            statement.executeUpdate("UPDATE McMPData SET Tokens='" + Integer.parseInt(args[2]) + "' WHERE PlayerName='" + args[1] + "'");
                            sender.sendMessage(Utility.prefix().append("Set " + args[1] + "'s tokens to " + args[2] + "").color(ChatColor.GREEN).create());
                        } else {
                            sender.sendMessage(Utility.prefix().append("No such players has ever joined!").color(ChatColor.RED).create());
                        }
                    } catch (Exception ex) {
                        sender.sendMessage(Utility.prefix().append("An error occured!").color(ChatColor.RED).create());
                    }
                } else {
                    sender.sendMessage(Utility.prefix().append("Usage: §b/mcm settokens [Player] [Tokens]").color(ChatColor.RED).create());
                }
            } else {
                sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
            }
        } else if (args[0].equalsIgnoreCase("setrank")) {
            if (Utility.hasPermission(sender, 9)) {
                if (args.length == 3) {
                    try {
                        Statement statement = Utility.getConnection().createStatement();
                        ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[1] + "'");
                        if (res.next()) {
                            statement.executeUpdate("UPDATE McMPData SET Rank='" + Integer.parseInt(args[2]) + "' WHERE PlayerName='" + args[1] + "'");
                            this.m.sendToStaff(ChatColor.AQUA + "Set " + args[1] + "'s rank to " + getRank(Integer.parseInt(args[2])) + ChatColor.GREEN + "");
                        } else {
                            sender.sendMessage(Utility.prefix().append("No such players has ever joined!").color(ChatColor.RED).create());
                        }
                    } catch (Exception ex) {
                        sender.sendMessage(Utility.prefix().append("An error occured!").color(ChatColor.RED).create());
                    }
                } else {
                    sender.sendMessage(Utility.prefix().append("Usage: §b/mcm setrank [Player] [RankID]").color(ChatColor.RED).create());
                }
            } else {
                sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
            }
        }
    }

    public static String getRank(Integer id) {
        if (id == 10) {
            return ChatColor.DARK_RED + "Operator";
        }
        if (id == 9) {
            return ChatColor.DARK_PURPLE + "Developer";
        }
        if (id == 8) {
            return ChatColor.RED + "Administrator";
        }
        if (id == 7) {
            return ChatColor.DARK_GREEN + "Moderator";
        }
        if (id == 6) {
            return ChatColor.BLUE + "Helper";
        }
        if (id == 5) {
            return ChatColor.GREEN + "Host";
        }
        if (id == 4) {
            return ChatColor.DARK_AQUA + "Builder";
        }
        if (id == 3) {
            return ChatColor.DARK_RED + "Legend";
        }
        if (id == 2) {
            return ChatColor.GOLD + "YouTuber";
        }
        if (id == 1) {
            return ChatColor.GREEN + "VIP";
        }
        return "Normal";
    }
}
