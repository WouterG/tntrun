package net.wouto.tntrun.game;

import org.bukkit.Location;

import java.util.*;

public class GameArea {

    private Location locLow;
    private Location locHigh;

    private List<Location> availableBlocks;

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
        this.availableBlocks = new ArrayList<>();
        for (int x = this.locLow.getBlockX(); x <= this.locHigh.getBlockX(); x++) {
            int y = this.locLow.getBlockY();
            for (int z = this.locLow.getBlockZ(); z <= this.locHigh.getBlockZ(); z++) {
                this.availableBlocks.add(new Location(this.locLow.getWorld(), x, y, z));
            }
        }
        Collections.shuffle(this.availableBlocks);
    }

    public int getBlockCount() {
        return this.availableBlocks.size();
    }

    public Collection<Location> getRandomBlocks(int count) {
        Collection<Location> r = new ArrayList<>();
        Iterator<Location> iterator = availableBlocks.iterator();
        for (int i = 0; i < count && iterator.hasNext(); i++) {
            r.add(iterator.next());
        }
        return r;
    }

    public Location getLocLow() {
        return locLow;
    }

    public Location getLocHigh() {
        return locHigh;
    }
}
