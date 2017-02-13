package net.wouto.tntrun;

import org.bukkit.Location;
import org.bukkit.World;

public class Config {

    public static Location GAME_AREA_LOC1;
    public static Location GAME_AREA_LOC2;

    public static void init(TNTRun instance) {
        World w = instance.getServer().getWorlds().get(0);
        SPAWN_LOCATION = new Location(w, 0, 64, 0);
        GAME_START_LOCATION = new Location(w, 20, 64, 0);

        GAME_AREA_LOC1 = new Location(w, 20, 63, -40);
        GAME_AREA_LOC2 = new Location(w, 100, 63, 40);
    }

    public static int MINIMUM_PLAYERS = 2;
    public static int START_DELAY_SECONDS = 10;

    public static Location SPAWN_LOCATION;
    public static Location GAME_START_LOCATION;

}
