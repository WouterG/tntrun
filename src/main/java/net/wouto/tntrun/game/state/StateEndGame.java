package net.wouto.tntrun.game.state;

import net.wouto.tntrun.game.GameState;
import net.wouto.tntrun.game.TNTGame;
import org.bukkit.entity.Player;

import java.util.Collection;

public class StateEndGame extends GameState {

    public StateEndGame(TNTGame game, Collection<Player> players) {
        super(game, players);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void start() {

    }

    @Override
    public void close() {

    }

    @Override
    public GameState getNextState() {
        return null;
    }
}
