package net.mcmortals.mcmbungee.Utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database {

    private final static ArrayList<DatabasePlayer> cachedPlayers = new ArrayList<DatabasePlayer>();

    public static DatabasePlayer getPlayer(String name){
        /*for(DatabasePlayer player : cachedPlayers){
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
        return p;*/
        return new DatabasePlayer(name);
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
            if(type.getValue().equals("all")){
                ResultSet res1 = Utility.getConnection().createStatement()
                        .executeQuery("SELECT * FROM McMInfractions WHERE PlayerName='" + player + "'");
                if (!res1.isBeforeFirst()) {
                    return null;
                }
                while (res1.next()) {
                    infractions.add(new Infraction
                            (InfractionType.valueOf(res1.getString("Type").replace(' ', '_')),
                                    res1.getString("Enforcer"), res1.getString("Time"), res1.getString("Reason")));
                }
            }
            else {
                ResultSet res1 = Utility.getConnection().createStatement()
                        .executeQuery("SELECT * FROM McMInfractions WHERE PlayerName='" + player + "' AND Type='" + type.getValue() + "'");
                if (!res1.isBeforeFirst()) {
                    return null;
                }
                while (res1.next()) {
                    infractions.add(new Infraction(type, res1.getString("Enforcer"), res1.getString("Time"), res1.getString("Reason")));
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
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
        Temporary_Ban("Temporary Ban"), Permanent_Ban("Permanent Ban"), Kick("Kick"), Mute("Mute"), all("all");
        private final String value;

        private InfractionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}


