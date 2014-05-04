package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

class OldLookup extends Command {

    private main m = null;

    public OldLookup(main This) {
        super("lookup", "","lp");
        m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        if (m.hasPermission(sender,6)) {
            if (args.length==1) {
                try {
                    Statement s = m.connect.createStatement();
                    ResultSet res = s.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[0] +"'") ;
                    if (res.next()) {
                        sender.sendMessage("§4[§cMcM§4] §6§lPlayer lookup: §f" + getPlayerName(res.getString("PlayerName"), res.getInt("Rank")));
                        sender.sendMessage("§6Rank: " + McMCommand.getRank(res.getInt("Rank")));
                        sender.sendMessage("§6Tokens: §b" + res.getInt("Tokens"));
                        sender.sendMessage("§6Tournament points: §b" + res.getInt("TournPoints"));
                        Date fsl = new Date(res.getLong("FirstLogin"));
                        Date lsl = new Date(res.getLong("LastLogin"));
                        if (!fsl.toGMTString().contains("1970")) {
                            sender.sendMessage("§6First join: §b" + fsl.toGMTString().replace("GMT", "UTC"));
                        } else sender.sendMessage("§6First join: §7Unknown");
                        if (!lsl.toGMTString().contains("1970")) {
                            sender.sendMessage("§6Last join: §b" + lsl.toGMTString().replace("GMT","UTC"));
                        } else sender.sendMessage("§6Last join: §7Unknown");
                        int inf = 0; int bans = 0; int kicks = 0; int mutes = 0;
                        Statement ss = m.connect.createStatement();
                        ResultSet ress = ss.executeQuery("SELECT * FROM McMInfractions WHERE PlayerName='" + args[0] + "'");
                        while (ress.next()) {
                            if (ress.getString("Type").equals("Permanent ban") || ress.getString("Type").equals("Temporary ban")) bans++;
                            if (ress.getString("Type").equals("Kick")) kicks++;
                            if (ress.getString("Type").equals("Mute")) mutes++;
                            inf++;
                        }
                        sender.sendMessage("§6Total infractions: §b" + inf);
                        sender.sendMessage("§6- Bans: §b" + bans);
                        sender.sendMessage("§6- Kicks: §b" + kicks);
                        sender.sendMessage("§6- Mutes: §b" + mutes);
                        sender.sendMessage("§6Is banned: §b" + yesorno(res.getInt("Banned")));
                        if (res.getInt("Banned")==1) {
                            sender.sendMessage("§6- Reason:§b" + res.getString("BanReason"));
                            if (res.getLong("BanUntil")!=-1) sender.sendMessage("§6- Until: §b" + new Date(res.getLong("BanUntil")).toGMTString().replace("GMT","UTC"));
                        }
                        res.close();
                    } else sender.sendMessage(prefix().append("No such player found!").color(ChatColor.RED).create());
                } catch (Exception ex) {ex.printStackTrace();}
            } else sender.sendMessage(prefix().append("Usage: §b/lookup [Player]").color(ChatColor.RED).create());
        } else sender.sendMessage(prefix().append("You cannot do that!").color(ChatColor.RED).create());
    }

    ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
    }

    private static String getPlayerName(String name, Integer rank) {
        try {
            if (rank==10) {
                return "§4§l[Op] §b"+ name + "§r";
            }
            if (rank==9) {
                return "§5§l[Dev] §b" + name + "§r";
            }
            if (rank==8) {
                return "§c§l[Admin] §b" + name + "§r";
            }
            if (rank==7) {
                return "§2[Mod] §f" + ChatColor.WHITE + name + "§r";
            }
            if (rank==6) {
                return "§9[Helper] §f" + ChatColor.WHITE + name + "§r";
            }
            if (rank==5) {
                return "§2[Host] §f" + ChatColor.WHITE + name + "§r";
            }
            if (rank==4) {
                return "§3[Builder] §f" + ChatColor.WHITE + name + "§r";
            }
            if (rank==3) {
                return "§4[Legend] §f" + ChatColor.WHITE + name + "§r";
            }
            if (rank==2) {
                return "§6[YT] §f" + ChatColor.WHITE + name + "§r";
            }
            if (rank==1) {
                return "§a[VIP] §f" + ChatColor.WHITE + name + "§r";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "§f" + ChatColor.WHITE + name + "§r";
    }

    String yesorno(int a) {
        if (a==1) return "Yes";
        return "No";
    }
}

