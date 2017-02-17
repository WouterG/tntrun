package net.wouto.tntrun.game;

import org.bukkit.Location;

import java.util.*;

public class GameArea {

    private Location locLow;
    private Location locHigh;

    private List<Location> allBlocks;

    public GameArea(Location loc1, Location loc2) {
        if (loc1.getWorld() != loc2.getWorld()) {
            throw new Error("Cannot create GameArea with 2 locations in different worlds");
        }
        this.locLow = new Location(loc1.getWorld(),
                loc1.getBlockX() <= loc2.getBlockX() ? loc1.getBlockX() : loc2.getBlockX(),
                loc1.getBlockY(),
                loc1.getBlockZ() <= loc2.getBlockZ() ? loc1.getBlockZ() : loc2.getBlockZ()
        );
        this.locHigh = new Location(loc1.getWorld(),
                loc1.getBlockX() > loc2.getBlockX() ? loc1.getBlockX() : loc2.getBlockX(),
                loc1.getBlockY(),
                loc1.getBlockZ() > loc2.getBlockZ() ? loc1.getBlockZ() : loc2.getBlockZ()
        );
        this.allBlocks = new ArrayList<>();
        for (int x = this.locLow.getBlockX(); x <= this.locHigh.getBlockX(); x++) {
            int y = this.locLow.getBlockY();
            for (int z = this.locLow.getBlockZ(); z <= this.locHigh.getBlockZ(); z++) {
                this.allBlocks.add(new Location(this.locLow.getWorld(), x, y, z));
            }
        }
    }

    public List<Location> getAllBlocks() {
        return allBlocks;
    }

    public int getBlockCount() {
        return this.allBlocks.size();
    }

    public Location getLocLow() {
        return locLow;
    }

    public Location getLocHigh() {
        return locHigh;
    }
}
