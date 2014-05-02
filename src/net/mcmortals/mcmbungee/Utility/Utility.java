package net.mcmortals.mcmbungee.Utility;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class Utility {

    public static ComponentBuilder prefix() {
        return new ComponentBuilder("[").color(ChatColor.DARK_RED).append("McM").color(ChatColor.RED).append("] ").color(ChatColor.DARK_RED);
    }

}
