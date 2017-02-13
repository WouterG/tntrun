package net.wouto.tntrun.player;

import org.bukkit.entity.Player;

import java.util.UUID;

public class TNTPlayer {

    private Player handle;

    private UUID uuid;
    private String username;
    private boolean alive;

    public TNTPlayer(Player player) {
        this.handle = player;
        this.uuid = player.getUniqueId();
        this.username = player.getName();
        this.alive = true;
    }

    public Player getHandle() {
        return handle;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
