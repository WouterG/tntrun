package net.wouto.tntrun.game;

import net.wouto.tntrun.Config;
import net.wouto.tntrun.TNTRun;
import net.wouto.tntrun.game.state.StateLobby;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

public class TNTGame {

    private GameArea area;
    private GameState gameState;
    private JobManager jobManager;

    public TNTGame() {
        this.gameState = new StateLobby(this, new ArrayList<>());
        this.jobManager = new JobManager(this);
        this.area = new GameArea(Config.GAME_AREA_LOC1, Config.GAME_AREA_LOC2);
    }

    public GameArea getArea() {
        return area;
    }

    public boolean isState(GameStateType type) {
        return type.getStateType().isAssignableFrom(this.gameState.getClass());
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    private void setGameState(GameState state) {
        if (this.gameState != null) {
            HandlerList.unregisterAll(this.gameState);
            this.gameState.close();
        }
        this.gameState = state;
        Bukkit.getServer().getPluginManager().registerEvents(this.gameState, TNTRun.getInstance());
        this.gameState.initialize();
        this.gameState.start();
    }

    public void nextGameState() {
        if (this.gameState == null) {
            return;
        }
        GameState next = this.gameState.getNextState();
        if (next == null) {
            Bukkit.getServer().shutdown();
            return;
        }
        this.setGameState(next);
    }

    public GameState getGameState() {
        return gameState;
    }

}
