package net.mcmortals.mcmbungee.Commands;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class McMCommand
        extends Command {
    main m = new main();

    public McMCommand(main This) {
        super("mcm", "", new String[0]);
        this.m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(prefix().append("MCMortals created by Bocktrow, Red_Epicness, HeavyMine13!").color(ChatColor.GREEN).create());
        } else if (args[0].equalsIgnoreCase("addtokens")) {
            if (this.m.hasPermission(sender, Integer.valueOf(6)).booleanValue()) {
                if (args.length == 3) {
                    try {
                        Statement statement = this.m.connect.createStatement();
                        ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[1] + "'");
                        if (res.next()) {
                            statement.executeUpdate("UPDATE  SET Tokens='" + (res.getInt("Tokens") + Integer.parseInt(args[2])) + "' WHERE PlayerName='" + args[1] + "'");
                            sender.sendMessage(prefix().append("Incremented " + args[1] + "'s tokens by " + args[2] + "").color(ChatColor.GREEN).create());
                        } else {
                            sender.sendMessage(prefix().append("No such players has ever joined!").color(ChatColor.RED).create());
                        }
                    } catch (Exception ex) {
                        sender.sendMessage(prefix().append("An error occured!").color(ChatColor.RED).create());
                    }
                } else {
                    sender.sendMessage(prefix().append("Usage: /mcm addtokens [Player] [Tokens]").color(ChatColor.RED).create());
                }
            } else {
                sender.sendMessage(prefix().append("You cannot do that!").color(ChatColor.RED).create());
            }
            if (args.length == 3) {
                try {
                    Statement statement = this.m.connect.createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM MCMPlayers WHERE PlayerName='" + args[1] + "'");
                    if (res.next()) {
                        statement.executeUpdate("UPDATE MCMPlayers SET Tokens='" + (res.getInt("Tokens") + Integer.parseInt(args[2])) + "' WHERE PlayerName='" + args[1] + "'");
                        sender.sendMessage(prefix().append("Incremented " + args[1] + "'s tokens by " + args[2] + "").color(ChatColor.GREEN).create());
                    } else {
                        sender.sendMessage(prefix().append("No such players has ever joined!").color(ChatColor.RED).create());
                    }
                } catch (Exception ex) {
                    sender.sendMessage(prefix().append("An error occured!").color(ChatColor.RED).create());
                }
            } else {
                sender.sendMessage(prefix().append("Usage: /mcm addtokens [Player] [Tokens]").color(ChatColor.RED).create());
            }
        } else if (args[0].equalsIgnoreCase("settokens")) {
            if (this.m.hasPermission(sender, Integer.valueOf(6)).booleanValue()) {
                if (args.length == 3) {
                    try {
                        Statement statement = this.m.connect.createStatement();
                        ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[1] + "'");
                        if (res.next()) {
                            statement.executeUpdate("UPDATE McMPData SET Tokens='" + Integer.parseInt(args[2]) + "' WHERE PlayerName='" + args[1] + "'");
                            sender.sendMessage(prefix().append("Set " + args[1] + "'s tokens to " + args[2] + "").color(ChatColor.GREEN).create());
                        } else {
                            sender.sendMessage(prefix().append("No such players has ever joined!").color(ChatColor.RED).create());
                        }
                    } catch (Exception ex) {
                        sender.sendMessage(prefix().append("An error occured!").color(ChatColor.RED).create());
                    }
                } else {
                    sender.sendMessage(prefix().append("Usage: /mcm settokens [Player] [Tokens]").color(ChatColor.RED).create());
                }
            } else {
                sender.sendMessage(prefix().append("You cannot do that!").color(ChatColor.RED).create());
            }
        } else if (args[0].equalsIgnoreCase("setrank")) {
            if (this.m.hasPermission(sender, Integer.valueOf(9)).booleanValue()) {
                if (args.length == 3) {
                    try {
                        Statement statement = this.m.connect.createStatement();
                        ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[1] + "'");
                        if (res.next()) {
                            statement.executeUpdate("UPDATE McMPData SET Rank='" + Integer.parseInt(args[2]) + "' WHERE PlayerName='" + args[1] + "'");
                            this.m.sendToStaff(ChatColor.GREEN + "Set " + args[1] + "'s rank to " + getRank(Integer.valueOf(Integer.parseInt(args[2]))) + ChatColor.GREEN + "");
                        } else {
                            sender.sendMessage(prefix().append("No such players has ever joined!").color(ChatColor.RED).create());
                        }
                    } catch (Exception ex) {
                        sender.sendMessage(prefix().append("An error occured!").color(ChatColor.RED).create());
                    }
                } else {
                    sender.sendMessage(prefix().append("Usage: /mcm setrank [Player] [RankID]").color(ChatColor.RED).create());
                }
            } else {
                sender.sendMessage(prefix().append("You cannot do that!").color(ChatColor.RED).create());
            }
        }
    }

    public String getRank(Integer id) {
        if (id.intValue() == 9) {
            return ChatColor.DARK_RED + "Operator";
        }
        if (id.intValue() == 8) {
            return ChatColor.DARK_PURPLE + "Developer";
        }
        if (id.intValue() == 7) {
            return ChatColor.RED + "Administrator";
        }
        if (id.intValue() == 6) {
            return ChatColor.DARK_GREEN + "Moderator";
        }
        if (id.intValue() == 5) {
            return ChatColor.BLUE + "Helper";
        }
        if (id.intValue() == 4) {
            return ChatColor.DARK_AQUA + "Builder";
        }
        if (id.intValue() == 3) {
            return ChatColor.GOLD + "YouTuber";
        }
        if (id.intValue() == 2) {
            return ChatColor.DARK_RED + "Legend";
        }
        if (id.intValue() == 1) {
            return ChatColor.GREEN + "VIP";
        }
        return "Normal";
    }

    public ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
    }
}
