package net.wouto.tntrun.game;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Collection;

public abstract class GameState implements Listener {

    private TNTGame game;
    private Collection<Player> players;

    public GameState(TNTGame game, Collection<Player> players) {
        this.players = players;
        this.game = game;
    }

    public final TNTGame getGame() {
        return game;
    }

    public final Collection<Player> getPlayers() {
        return players;
    }

    public abstract void initialize();

    public abstract void start();

    public abstract void close();

    public abstract GameState getNextState();

}
