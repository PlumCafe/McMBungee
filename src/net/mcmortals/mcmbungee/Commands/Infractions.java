package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.Utility.Database;
import net.mcmortals.mcmbungee.Utility.DatabasePlayer;
import net.mcmortals.mcmbungee.Utility.Utility;
import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.Statement;

public class Infractions extends Command {

    private main m = new main();

    public Infractions() {
        super("infractions", "", "infr");
    }

    public void execute(CommandSender sender, String[] args) {
        //Permissions Check
        if (!m.hasPermission(sender, 6)) {
            sender.sendMessage(Utility.prefix().append("You cannot do that!").color(ChatColor.RED).create());
            return;
        }
        //Argument Length Check
        if (args.length != 1) {
            sender.sendMessage(Utility.prefix().append("Usage: ").color(ChatColor.RED).append("/infr [Player]").color(ChatColor.AQUA).create());
            return;
        }
        DatabasePlayer dp = new Database(m).getPlayer(args[0]);
        //Has Infractions Check
        if (!dp.exists()) {
            sender.sendMessage(Utility.prefix().append("No infractions found for ").color(ChatColor.RED).append(args[0]).color(ChatColor.AQUA).create());
            return;
        } 
        try {
            sender.sendMessage(Utility.prefix().append("Infractions for: ").color(ChatColor.GOLD).bold(true).append(args[0]).create());
            Statement s = m.connect.createStatement();
            ResultSet res1 = s.executeQuery("SELECT * FROM McMInfractions WHERE PlayerName='" + args[0] + "'");
            while (res1.next()) {
                sender.sendMessage(new ComponentBuilder(res1.getString("Type") + ": ").color(ChatColor.RED).bold(true).append("Reason: ").color(ChatColor.AQUA).bold(false).append(res1.getString("Reason")).color(ChatColor.GOLD).create());
                String time = res1.getString("Time");
                ComponentBuilder m = new ComponentBuilder("- Issued by " ).color(ChatColor.AQUA).append(res1.getString("Enforcer")).color(ChatColor.GOLD);
                if (time == null) {
                    sender.sendMessage(m.create());
                } else {
                    sender.sendMessage(m.append(" for ").color(ChatColor.AQUA).append(time).color(ChatColor.GOLD).create());
                }
            }                    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}