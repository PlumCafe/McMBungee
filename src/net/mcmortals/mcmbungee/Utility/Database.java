package net.mcmortals.mcmbungee.Utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Database {

    private final static ArrayList<DatabasePlayer> cachedPlayers = new ArrayList<DatabasePlayer>();

    public static DatabasePlayer getPlayer(String name){
        for(DatabasePlayer player : cachedPlayers){
            if(player.getName().equals(name)){
                return player;
            }
        }
        final DatabasePlayer p = new DatabasePlayer(name);
        cachedPlayers.add(p);
        Utility.getMainInstance().getProxy().getScheduler().schedule(Utility.getMainInstance(), new Runnable() {
            @Override
            public void run() {
                cachedPlayers.remove(p);
            }
        }, 5, TimeUnit.MINUTES);
        return p;
    }

    public static void removeCachedPlayer(String name){
        for(DatabasePlayer player : cachedPlayers){
            if(player.getName().equals(name)){
                cachedPlayers.remove(player);
                break;
            }
        }
    }

    public static ArrayList<Infraction> getInfractions(String player, InfractionType type){
        ArrayList<Infraction> infractions = new ArrayList<Infraction>();
        try{
            if(type.value.equals("all")){
                ResultSet res1 = Utility.getConnection().createStatement()
                        .executeQuery("SELECT * FROM McMInfractions WHERE PlayerName='" + player + "'");
                if (!res1.isBeforeFirst()) {
                    return null;
                }
                while (res1.next()) {
                    infractions.add(new Infraction
                            (InfractionType.valueOf(res1.getString("Type")), res1.getString("Enforcer"), res1.getString("Time"), res1.getString("Reason")));
                }
            }
            else {
                ResultSet res1 = Utility.getConnection().createStatement()
                        .executeQuery("SELECT * FROM McMInfractions WHERE PlayerName='" + player + "' AND Type='" + type + "'");
                if (!res1.isBeforeFirst()) {
                    return null;
                }
                while (res1.next()) {
                    infractions.add(new Infraction(type, res1.getString("Enforcer"), res1.getString("Time"), res1.getString("Reason")));
                }
            }
        }catch(SQLException ignored){

        }
        return infractions;
    }

    public static void addInfraction(String PlayerName, String Enforcer, InfractionType Type, String Time, String Reason){
        try{
            Utility.getConnection().createStatement().executeUpdate(
                "INSERT INTO McMInfractions (PlayerName, Enforcer, Type, Time, Reason) VALUES ('"+PlayerName+"', '"+Enforcer+"', '"+Type+"', '"+Time+"', '"+Reason+"')"
            );
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public enum InfractionType{
        TEMP_BAN("Temporary Ban"), PERMANENT_BAN("Permanent Ban"), KICK("Kick"), MUTE("Mute"), ALL("all");
        public final String value;

        private InfractionType(String value) {
            this.value = value;
        }
    }
}


