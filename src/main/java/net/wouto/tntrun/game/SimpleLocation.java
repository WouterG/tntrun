package net.wouto.tntrun.game;

import org.bukkit.Location;

public class SimpleLocation {

    private int x;
    private int y;
    private int z;

    public SimpleLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean is(Location location) {
        return (
            location.getBlockX() == this.x &&
            location.getBlockY() == this.y &&
            location.getBlockZ() == this.z
        );
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
