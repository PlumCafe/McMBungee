package net.mcmortals.mcmbungee.Clans;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class CCommand extends Command {

    private main m = null;
    public CCommand(main This) {
        super("clan", "", "c", "clans");
        m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            if(args.length==0) {
            // CREATE COMMAND
            } else if (args[0].equalsIgnoreCase("create")) {
                if (args.length==2) {
                    if (!isInAClan((ProxiedPlayer) sender)) {
                        try {
                            Statement s = this.m.connect.createStatement();
                            String jc = randomString(8);
                            s.executeUpdate("INSERT INTO McMClan (Name, Owner, JoinCode) VALUES ('" + args[1] +"', '" + sender.getName() +"', '" + jc + "')");
                            ResultSet res = s.executeQuery("SELECT * FROM McMClan WHERE Name='" + args[1] + "'");
                            if (res.next()) {
                                s.executeUpdate("UPDATE McMPData SET ClanID=" + res.getInt("ID") + " WHERE PlayerName='" + sender.getName() + "'");
                                sender.sendMessage(prefix().append("Created clan ").color(ChatColor.GREEN).append(args[1]).color(ChatColor.GREEN).bold(true).append(" successfully!").color(ChatColor.GREEN).bold(false).create());
                                sender.sendMessage(prefix().append("Give this: ").color(ChatColor.GREEN).append(jc).color(ChatColor.GREEN).bold(true).append(" to players that want to join your clan. Then they can just do /c join and then this code.").color(ChatColor.GREEN).bold(false).create());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();}
                    } else sender.sendMessage(prefix().append("You are already in a clan! Do /c leave to leave it.").color(ChatColor.RED).create());
                } else sender.sendMessage(prefix().append("Usage: /c create [Clan Name]").color(ChatColor.RED).create());
            //INFO COMMAND
            } else if (args[0].equalsIgnoreCase("info")) {
                if (args.length==1) {
                    if (isInAClan((ProxiedPlayer) sender)) {
                        try {
                            Statement s = this.m.connect.createStatement();
                            ResultSet res = s.executeQuery("SELECT * FROM McMClan WHERE ID='" + getClan((ProxiedPlayer) sender) + "'");
                            if (res.next()) {
                                sender.sendMessage(prefix().append(" == Clan information == ").color(ChatColor.GOLD).bold(true).create());
                                sender.sendMessage(prefix().append("Clan name: ").color(ChatColor.AQUA).append(res.getString("Name")).color(ChatColor.GREEN).bold(true).create());
                                sender.sendMessage(prefix().append("Clan owner: ").color(ChatColor.AQUA).append(res.getString("Owner")).color(ChatColor.GREEN).create());
                                sender.sendMessage(prefix().append("Clan members: ").color(ChatColor.AQUA).create());
                                ResultSet re = s.executeQuery("SELECT * FROM McMPData WHERE ClanID='" + getClan((ProxiedPlayer) sender) + "'");
                                while (re.next()) { sender.sendMessage(prefix().append("> ").color(ChatColor.GOLD).bold(true).append(re.getString("PlayerName")).color(ChatColor.GREEN).bold(false).create()); }
                                if (res.getString("Owner").equals(sender.getName())) {
                                    sender.sendMessage(prefix().append(" ====== Owner info ====== ").color(ChatColor.RED).bold(false).create());
                                    sender.sendMessage(prefix().append("Join code: ").color(ChatColor.AQUA).append(res.getString("JoinCode")).color(ChatColor.GREEN).create());
                                }
                                sender.sendMessage(prefix().append(" =================== ").color(ChatColor.GOLD).bold(true).create());
                            }
                        } catch (Exception ex) {}
                    } else sender.sendMessage(prefix().append("You are not in a clan!").color(ChatColor.RED).create());
                } else sender.sendMessage(prefix().append("Usage: /c info").color(ChatColor.RED).create());
            // DISBAND COMMAND :@
            } else if (args[0].equalsIgnoreCase("disband")) {
                if (args.length==1) {
                    if (isInAClan((ProxiedPlayer) sender)) {
                        try {
                            Statement s = this.m.connect.createStatement();
                            ResultSet res = s.executeQuery("SELECT * FROM McMClan WHERE ID='" + getClan((ProxiedPlayer) sender) + "'");
                            if (res.next()) {
                                int id= res.getInt("ID");
                                String own= res.getString("Owner");
                                String name = res.getString("Name");
                                res.close();
                                if (own.equalsIgnoreCase(sender.getName())) {
                                    s.executeUpdate("DELETE FROM McMClan WHERE ID=" + id);
                                    s.executeUpdate("UPDATE McMPData SET ClanID=-1 WHERE ClanID=" + id);
                                    sender.sendMessage(prefix().append("Clan ").color(ChatColor.GOLD).append(name).color(ChatColor.GOLD).bold(true).append(" deleted successfully!").color(ChatColor.GOLD).bold(false).create());
                                } else sender.sendMessage(prefix().append("You are not the owner of your clan!").color(ChatColor.RED).create());
                            } else sender.sendMessage(prefix().append("Some error occurred! Report this to staff immediately!").color(ChatColor.RED).create());
                        } catch (Exception ex) {ex.printStackTrace();}
                    } else sender.sendMessage(prefix().append("You are not in a clan!").color(ChatColor.RED).create());
                } else sender.sendMessage(prefix().append("Usage: /c disband").color(ChatColor.RED).create());
            //LEAVE COMMAND :@
            } else if (args[0].equalsIgnoreCase("leave")) {
                if (args.length==1) {
                    if (isInAClan((ProxiedPlayer) sender)) {
                        try {
                            Statement s = this.m.connect.createStatement();
                            ResultSet res = s.executeQuery("SELECT * FROM McMClan WHERE ID='" + getClan((ProxiedPlayer) sender) + "'");
                            if (res.next()) {
                                String own= res.getString("Owner");
                                String name = res.getString("Name");
                                res.close();
                                if (!own.equalsIgnoreCase(sender.getName())) {
                                    s.executeUpdate("UPDATE McMPData SET ClanID=-1 WHERE PlayerName='" + sender.getName() + "'");
                                    sender.sendMessage(prefix().append("Left clan ").color(ChatColor.GOLD).append(name).color(ChatColor.GOLD).bold(true).append(" successfully!").color(ChatColor.GOLD).bold(false).create());
                                } else sender.sendMessage(prefix().append("You cannot leave your own clan! Disband it instead with /c disband or transfer it by using /c transfer [Player].").color(ChatColor.RED).create());
                            } else sender.sendMessage(prefix().append("Some error occurred! Report this to staff immediately!").color(ChatColor.RED).create());
                        } catch (Exception ex) {ex.printStackTrace();}
                    } else sender.sendMessage(prefix().append("You are not in a clan!").color(ChatColor.RED).create());
                } else sender.sendMessage(prefix().append("Usage: /c leave").color(ChatColor.RED).create());
            //TRANSFER COMMAND
            } else if (args[0].equalsIgnoreCase("transfer")) {
                if (args.length==2) {
                    if (isInAClan((ProxiedPlayer) sender)) {
                        try {
                            Statement s = this.m.connect.createStatement();
                            ResultSet res = s.executeQuery("SELECT * FROM McMClan WHERE ID='" + getClan((ProxiedPlayer) sender) + "'");
                            if (res.next()) {
                                int id = res.getInt("ID");
                                String own= res.getString("Owner");
                                String name = res.getString("Name");
                                res.close();
                                ResultSet re = s.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + args[1] + "'");
                                if (re.next()){
                                    if (re.getInt("ClanID")!=id ) { sender.sendMessage(prefix().append("You can only transfer the clan to a member of your clan!").color(ChatColor.RED).create()); return;}
                                } else {sender.sendMessage(prefix().append("You can only transfer the clan to a member of your clan!").color(ChatColor.RED).create()); return;}
                                if (own.equalsIgnoreCase(sender.getName())) {
                                    s.executeUpdate("UPDATE McMClan SET Owner='" + args[1] +"' WHERE ID=" + id );
                                    sender.sendMessage(prefix().append("Transferred clan ").color(ChatColor.GOLD).append(name).color(ChatColor.GOLD).bold(true).append(" to " + args[1] +" successfully!").color(ChatColor.GOLD).bold(false).create());
                                } else sender.sendMessage(prefix().append("You are not the owner of your clan!").color(ChatColor.RED).create());
                            } else sender.sendMessage(prefix().append("Some error occurred! Report this to staff immediately!").color(ChatColor.RED).create());
                        } catch (Exception ex) {ex.printStackTrace();}
                    } else sender.sendMessage(prefix().append("You are not in a clan!").color(ChatColor.RED).create());
                } else sender.sendMessage(prefix().append("Usage: /c transfer [Player]").color(ChatColor.RED).create());
            //JOIN COMMAND
            } else if (args[0].equalsIgnoreCase("join")) {
                if (args.length==2) {
                    if (!isInAClan((ProxiedPlayer) sender)) {
                        try {
                            Statement s = this.m.connect.createStatement();
                            ResultSet res = s.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + sender.getName() + "'");
                            if (res.next()) {
                                if (res.getInt("ClanID")==-1) {
                                    ResultSet re = s.executeQuery("SELECT * FROM McMClan WHERE JoinCode='" + args[1] + "'");
                                    String m= re.getString("Name");
                                    if (re.next()){
                                        s.executeUpdate("UPDATE McMPData SET ClanID=" + re.getInt("ID") + " WHERE PlayerName='" + sender.getName() + "'");
                                        sender.sendMessage(prefix().append("Joined clan ").color(ChatColor.GOLD).append(m).color(ChatColor.GOLD).bold(true).append(" successfully!").color(ChatColor.GOLD).bold(false).create());
                                    } else sender.sendMessage(prefix().append("Invalid join code!").color(ChatColor.RED).create());
                                } else sender.sendMessage(prefix().append("You are already in a clan!").color(ChatColor.RED).create());
                            } else sender.sendMessage(prefix().append("Some error occurred! Report this to staff immediately!").color(ChatColor.RED).create());
                        } catch (Exception ex) {ex.printStackTrace();}
                    } else sender.sendMessage(prefix().append("You are already in a clan!").color(ChatColor.RED).create());
                } else sender.sendMessage(prefix().append("Usage: /c join [JoinCode]").color(ChatColor.RED).create());
            }
        // END OF COMMANDS
        } else sender.sendMessage(prefix().append("You cannot create a class console!").color(ChatColor.RED).create());
    }

    public ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("Clan").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
    }

    boolean isInAClan(ProxiedPlayer p) {
        try {
            Statement statement = this.m.connect.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + p.getName() + "'");
            if (res.next()) {
                return res.getInt("ClanID")!=-1;
            }
        } catch (Exception ex) {return false;}
        return false;
    }

    Integer getClan(ProxiedPlayer p) {
        try {
            Statement statement = this.m.connect.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM McMPData WHERE PlayerName='" + p.getName() + "'");
            if (res.next()) {
                return res.getInt("ClanID");
            }
        } catch (Exception ex) {
            ex.printStackTrace();}
        return -1;
    }

    String randomString(final int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}
