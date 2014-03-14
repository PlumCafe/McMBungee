package net.mcmortals.mcmbungee.Commands;

import net.mcmortals.mcmbungee.main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class MessageTell
        extends Command {
    main m = new main();

    public MessageTell(main This) {
        super("tell", "", new String[0]);
        this.m = This;
    }

    public void execute(CommandSender sender, String[] args) {
        MessageMsg msg = new MessageMsg(this.m);
        msg.execute(sender, args);
    }
}
