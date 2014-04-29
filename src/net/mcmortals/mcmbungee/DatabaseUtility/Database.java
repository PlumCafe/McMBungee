package net.mcmortals.mcmbungee.DatabaseUtility;

import net.mcmortals.mcmbungee.main;

import java.sql.Connection;

public class Database {

    Connection c;

    public Database(main main){
        c = main.connect;
    }

    public DatabasePlayer getPlayer(String name){
        return new DatabasePlayer(name, c);
    }

}
