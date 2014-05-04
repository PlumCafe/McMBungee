package net.mcmortals.mcmbungee.Utility;

public class Database {

    public static DatabasePlayer getPlayer(String name){
        return new DatabasePlayer(name);
    }

}
