package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class Clan extends Command {

    public Clan() {
        super("clan", "", "c", "clans");
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(Utility.prefix().append("You cannot create a clan console!").color(ChatColor.RED).create());
        }
        //Help Messages
        if(args.length==0) {
            sender.sendMessage(Utility.prefix().append("§6§lClan commands:").create());
            sender.sendMessage(Utility.prefix().append("§6/c join [Join Code]§a: Join a clan using its join code.").create());
            sender.sendMessage(Utility.prefix().append("§aYou can ask the clan owner for the join code.").create());
            sender.sendMessage(Utility.prefix().append("§6/c leave§a: Leave the clan you are in.").create());
            sender.sendMessage(Utility.prefix().append("§6/c info§a: See your clan info.").create());
            sender.sendMessage(Utility.prefix().append("§6/c create [Clan Name]§a: Create a new clan.").create());
            sender.sendMessage(Utility.prefix().append("§6/c disband§a: Disband/delete your clan.").create());
            sender.sendMessage(Utility.prefix().append("§6/c kick [Player]§a: Kick a player from the clan.").create());
            sender.sendMessage(Utility.prefix().append("§6/c transfer [Player]§a: Transfer your clan's ownership.").create());
        }
        //Create Command
        else if (args[0].equalsIgnoreCase("create")) {
            //Argument Length Check
            if (args.length!=2) {
                sender.sendMessage(Utility.prefix().append("Usage: §b/c create [Clan Name]").color(ChatColor.RED).create());
                return;
            }
            //Is In A Clan Check
            if (isInAClan((ProxiedPlayer) sender)) {
                sender.sendMessage(Utility.prefix().append("You are already in a clan! Do /c leave to leave it.").color(ChatColor.RED).create());
                return;
            }
            try {
                Statement s = Utility.getConnection().createStatement();
                String jc = randomString();
                //Register A Clan
                s.executeUpdate("INSERT INTO McMClan (Name, Owner, JoinCode) VALUES ('" + args[1] +"', '" + sender.getName() +"', '" + jc + "')");
                ResultSet res = s.executeQuery("SELECT * FROM McMClan WHERE Name='" + args[1] + "'");
                if (res.isBeforeFirst()) {
                    s.executeUpdate("UPDATE McMPData SET ClanID=" + res.getInt("ID") + " WHERE PlayerName='" + sender.getName() + "'");
                    sender.sendMessage(Utility.prefix().append("Created clan ").color(ChatColor.GREEN).append(args[1]).color(ChatColor.GREEN).bold(true).append(" successfully!").color(ChatColor.GREEN).bold(false).create());
                    sender.sendMessage(Utility.prefix().append("Give this: ").color(ChatColor.GREEN).append(jc).color(ChatColor.GREEN).bold(true).append(" to players that want to join your clan. Then they can just do /c join and then this code.").color(ChatColor.GREEN).bold(false).create());
                }
            } catch (Exception ex) {
                ex.printStackTrace();}
        }
        //Info Command
        else if (args[0].equalsIgnoreCase("info")) {
            //Argument Length Check
            if (args.length!=1) {
                sender.sendMessage(Utility.prefix().append("Usage: §b/c info").color(ChatColor.RED).create());
                return;
            }
            //Is In A Clan Check
            if (!isInAClan((ProxiedPlayer) sender)) {
                sender.sendMessage(Utility.prefix().append("You are not in a clan!").color(ChatColor.RED).create());
                return;
            }
            try {
                Statement s = Utility.getConnection().createStatement();
                ResultSet res = s.executeQuery("SELECT * FROM McMClan WHERE ID='" + getClan((ProxiedPlayer) sender) + "'");
                if (res.isBeforeFirst()) {
                    String name = res.getString("Name");
                    String own = res.getString("Owner");
                    String jc = res.getString("JoinCode");
                    //Send Info Messages
                    sender.sendMessage(Utility.prefix().append(" == Clan information == ").color(ChatColor.GOLD).bold(true).create());
                    sender.sendMessage(Utility.prefix().append("Clan name: ").color(ChatColor.AQUA).append(name).color(ChatColor.GREEN).bold(true).create());
                    sender.sendMessage(Utility.prefix().append("Clan owner: ").color(ChatColor.AQUA).append(own).color(ChatColor.GREEN).create());
                    sender.sendMessage(Utility.prefix().append("Clan members: ").color(ChatColor.AQUA).create());
                    //Append All Players
                    res.beforeFirst();
                    while (res.next()) {
                        sender.sendMessage(Utility.prefix().append("> ").color(ChatColor.GOLD).bold(true)
                                .append(res.getString("PlayerName")).color(ChatColor.GREEN).bold(false).create());
                    }
                    //Append Owner Info
                    if (own.equals(sender.getName())) {
                        sender.sendMessage(Utility.prefix().append(" ====== Owner info ====== ").color(ChatColor.RED).bold(false).create());
                        sender.sendMessage(Utility.prefix().append("Join code: ").color(ChatColor.AQUA).append(jc).color(ChatColor.GREEN).create());
                    }
                    //End Message
                    sender.sendMessage(Utility.prefix().append(" =================== ").color(ChatColor.GOLD).bold(true).create());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //Disband Command
        else if (args[0].equalsIgnoreCase("disband")) {
            //Argument Length Check
            if (args.length!=1) {
                sender.sendMessage(Utility.prefix().append("Usage: §b/c disband").color(ChatColor.RED).create());
            }
            //Is In A Clan Check
            if (!isInAClan((ProxiedPlayer) sender)) {
                sender.sendMessage(Utility.prefix().append("You are not in a clan!").color(ChatColor.RED).create());
            }
            try {
                Statement s = Utility.getConnection().createStatement();
                ResultSet res = s.executeQuery("SELECT * FROM McMClan WHERE ID='" + getClan((ProxiedPlayer) sender) + "'");
                if (res.isBeforeFirst()) {
                    res.next();
                    int id = getClan((ProxiedPlayer)sender);
                    String own = res.getString("Owner");
                    String name = res.getString("Name");
                    //Is Owner Check
                    if (!own.equalsIgnoreCase(sender.getName())) {
                        sender.sendMessage(Utility.prefix().append("You are not the owner of this clan!").color(ChatColor.RED).create());
                        return;
                    }
                    //Alert Clan
                    sendClanMessage(id, getPlayerName(sender.getName(),ChatColor.GOLD) + ChatColor.GOLD  + " disbanded the clan!");
                    //Delete Clan
                    s.executeUpdate("DELETE FROM McMClan WHERE ID=" + id);
                    s.executeUpdate("UPDATE McMPData SET ClanID=-1 WHERE ClanID=" + id);
                    //Send Confirmation To Owner
                    sender.sendMessage(Utility.prefix().append("Clan ").color(ChatColor.GOLD).append(name).color(ChatColor.GOLD).bold(true)
                            .append(" got disbanded successfully!").color(ChatColor.GOLD).bold(false).create());
                } else sender.sendMessage(Utility.prefix().append("Some error occurred! Report this to staff immediately!").color(ChatColor.RED).create());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //Leave Command
        } else if (args[0].equalsIgnoreCase("leave")) {
            //Argument Length Check
            if (args.length!=1) {
                sender.sendMessage(Utility.prefix().append("Usage: §b/c leave").color(ChatColor.RED).create());
            }
            //Is In A Clan Check
            if (!isInAClan((ProxiedPlayer) sender)) {
                sender.sendMessage(Utility.prefix().append("You are not in a clan!").color(ChatColor.RED).create());
            }
            try {
                Statement s = Utility.getConnection().createStatement();
                ResultSet res = s.executeQuery("SELECT * FROM McMClan WHERE ID='" + getClan((ProxiedPlayer) sender) + "'");
                if (res.isBeforeFirst()) {
                    res.next();
                    String own= res.getString("Owner");
                    String name = res.getString("Name");
                    //Is Owner Check
                    if (own.equalsIgnoreCase(sender.getName())) {
                        sender.sendMessage(Utility.prefix()
                                .append("You cannot leave your own clan! Disband it instead with /c disband or transfer it by using /c transfer [Player].")
                                .color(ChatColor.RED).create());
                    }
                    //Alert Clan
                    sendClanMessage(res.getInt("ID"), getPlayerName(sender.getName(),ChatColor.GOLD) +  ChatColor.GOLD + " left the clan!");
                    //Remove Player From Clan
                    s.executeUpdate("UPDATE McMPData SET ClanID=-1 WHERE PlayerName='" + sender.getName() + "'");
                    //Send Confirmation To Player
                    sender.sendMessage(Utility.prefix().append("Left clan ").color(ChatColor.GOLD).append(name).color(ChatColor.GOLD).bold(true).append(" successfully!").color(ChatColor.GOLD).bold(false).create());
                } else sender.sendMessage(Utility.prefix().append("Some error occurred! Report this to staff immediately!").color(ChatColor.RED).create());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //Transfer Command
        } else if (args[0].equalsIgnoreCase("transfer")) {
            //Argument Length Check
            if (args.length!=2) {
                sender.sendMessage(Utility.prefix().append("Usage: §b/c transfer [Player]").color(ChatColor.RED).create());
            }
            //Is In A Clan Check
            if (!isInAClan((ProxiedPlayer) sender)) {
                sender.sendMessage(Utility.prefix().append("You are not in a clan!").color(ChatColor.RED).create());
            }
            try {
                Statement s = Utility.getConnection().createStatement();
                ResultSet res = s.executeQuery("SELECT * FROM McMClan WHERE ID='" + getClan((ProxiedPlayer) sender) + "'");
                if (res.next()) {
                    int id = res.getInt("ID");
                    String own= res.getString("Owner");
                    String name = res.getString("Name");
                    //Is Owner Check
                    if (!own.equalsIgnoreCase(sender.getName())) {
                        sender.sendMessage(Utility.prefix().append("You are not the owner of your clan!").color(ChatColor.RED).create());
                    }
                    ResultSet re = s.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[1] + "'"); re.next();
                    //Target Is Member Check
                    if (re.isBeforeFirst() || re.getInt("ClanID") != id ) {
                        sender.sendMessage(Utility.prefix()
                                .append("You can only transfer the clan to a member of your clan!").color(ChatColor.RED).create());
                        return;
                    }
                    //Alert Clan
                    sendClanMessage(id, getPlayerName(args[1],ChatColor.GREEN) + ChatColor.GREEN + " is now the clan owner!");
                    //Set New Owner
                    s.executeUpdate("UPDATE McMClan SET Owner='" + args[1] +"' WHERE ID=" + id );
                    //Send Confirmation To Player
                    sender.sendMessage(Utility.prefix().append("Transferred clan ").color(ChatColor.GOLD).append(name).color(ChatColor.GOLD).bold(true).append(" to " + args[1] +" successfully!").color(ChatColor.GOLD).bold(false).create());
                } else sender.sendMessage(Utility.prefix().append("Some error occurred! Report this to staff immediately!").color(ChatColor.RED).create());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //Kick Command
        else if (args[0].equalsIgnoreCase("kick")) {
            //Argument Length Check
            if (args.length!=2) {
                sender.sendMessage(Utility.prefix().append("Usage: §b/c kick [Player]").color(ChatColor.RED).create());
            }
            //Is In A Clan Check
            if (!isInAClan((ProxiedPlayer) sender)) {
                sender.sendMessage(Utility.prefix().append("You are not in a clan!").color(ChatColor.RED).create());
            }
            try {
                Statement s = Utility.getConnection().createStatement();
                ResultSet res = s.executeQuery("SELECT * FROM McMClan WHERE ID='" + getClan((ProxiedPlayer) sender) + "'");
                if (res.next()) {
                    int id = res.getInt("ID");
                    String own= res.getString("Owner");
                    //Is Owner Check
                    if (!own.equalsIgnoreCase(sender.getName())) {
                        sender.sendMessage(Utility.prefix().append("You are not the owner of your clan!").color(ChatColor.RED).create());
                    }
                    ResultSet re = s.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[1] + "'"); re.next();
                    //Target Is Member Check
                    if (re.isBeforeFirst() || re.getInt("ClanID")!=id) {
                        sender.sendMessage(Utility.prefix().append("That player is not in your clan!").color(ChatColor.RED).create());
                        return;
                    }
                    //Is Target Self Check
                    if (args[1].equalsIgnoreCase(sender.getName())) {
                        sender.sendMessage(Utility.prefix().append("Cannot kick yourself from your clan!").color(ChatColor.RED).create());
                        return;
                    }
                    //Alert Clan
                    sendClanMessage(id, getPlayerName(args[1],ChatColor.GOLD) + ChatColor.GOLD + " got kicked from the clan!");
                    //Kick Player
                    s.executeUpdate("UPDATE McMPData SET ClanID=-1 WHERE PlayerName='" + args[1] + "'");
                } else sender.sendMessage(Utility.prefix().append("Some error occurred! Report this to staff immediately!").color(ChatColor.RED).create());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //Join Command
        else if (args[0].equalsIgnoreCase("join")) {
            //Argument Length Check
            if (args.length!=2) {
                sender.sendMessage(Utility.prefix().append("Usage: §b/c join [JoinCode]").color(ChatColor.RED).create());
            }
            //Is In A Clan Check
            if (isInAClan((ProxiedPlayer) sender)) {
                sender.sendMessage(Utility.prefix().append("You are already in a clan!").color(ChatColor.RED).create());
            }
            try {
                Statement s = Utility.getConnection().createStatement();
                ResultSet res = s.executeQuery("SELECT * FROM McMClan WHERE JoinCode='" + args[1] + "'");
                if (res.next()){
                    //Alert Clan
                    sendClanMessage(res.getInt("ID"),getPlayerName(sender.getName(),ChatColor.GREEN) + ChatColor.GREEN + " joined the clan!");
                    String m= res.getString("Name");
                    //Join Clan
                    s.executeUpdate("UPDATE McMPData SET ClanID=" + res.getInt("ID") + " WHERE PlayerName='" + sender.getName() + "'");
                    //Send Confirmation To Player
                    sender.sendMessage(Utility.prefix().append("Joined clan ").color(ChatColor.GOLD)
                            .append(m).color(ChatColor.GOLD).bold(true).append(" successfully!").color(ChatColor.GOLD).create());
                } else sender.sendMessage(Utility.prefix().append("Invalid join code!").color(ChatColor.RED).create());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else sender.sendMessage(Utility.prefix().append("Unknown command, do §b/c §cfor help.").color(ChatColor.RED).create());

    }

    boolean isInAClan(ProxiedPlayer p) {
        try {
            Statement statement = Utility.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + p.getName() + "'");
            if (res.next()) {
                return res.getInt("ClanID")!=-1;
            }
        } catch (Exception ex) {return false;}
        return false;
    }

    Integer getClan(ProxiedPlayer p) {
        try {
            Statement statement = Utility.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + p.getName() + "'");
            if (res.next()) {
                return res.getInt("ClanID");
            }
        } catch (Exception ex) {
            ex.printStackTrace();}
        return -1;
    }

    String randomString() {
        String AB = "0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(5);
        for( int i = 0; i < 5; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    void sendClanMessage(Integer cid, String msg) {
        try {
            Statement statement = Utility.getConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE ClanID=" + cid);
            while (res.next()) {
                try {
                    ProxyServer.getInstance().getPlayer(res.getString("PlayerName")).sendMessage(Utility.prefix().append(msg).create());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    String getPlayerName(String name, ChatColor cc) {
        try {
        Statement s = Utility.getConnection().createStatement();
        ResultSet res = s.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + name + "'");
        if (!res.next()) return "§f" + cc + name + "§r";
        int rank = res.getInt("Rank");
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
            return "§2[Mod] §f" + cc + name + "§r";
        }
        if (rank==6) {
            return "§9[Helper] §f" + cc  + name + "§r";
        }
        if (rank==5) {
            return "§2[Host] §f" + cc  + name + "§r";
        }
        if (rank==4) {
            return "§3[Builder] §f" + cc  + name + "§r";
        }
        if (rank==3) {
            return "§4[Legend] §f" + cc  + name + "§r";
        }
        if (rank==2) {
            return "§6[YT] §f" + cc  + name + "§r";
        }
        if (rank==1) {
            return "§a[VIP] §f" + cc  + name + "§r";
        }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "§f" + cc  + name + "§r";
    }
}
