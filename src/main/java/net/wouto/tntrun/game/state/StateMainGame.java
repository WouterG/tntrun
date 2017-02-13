package net.wouto.tntrun.game.state;

import net.wouto.tntrun.game.GameState;
import net.wouto.tntrun.game.TNTGame;
import net.wouto.tntrun.player.TNTPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class StateMainGame extends GameState {

    public Collection<TNTPlayer> tntPlayers;

    public StateMainGame(TNTGame game, Collection<Player> players) {
        super(game, players);
        this.tntPlayers = new ArrayList<>();
    }

    @Override
    public void initialize() {
        for (Player player : this.getPlayers()) {
            this.tntPlayers.add(new TNTPlayer(player));
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void close() {

    }

    @Override
    public GameState getNextState() {
        return new StateEndGame(this.getGame(), this.getPlayers());
    }
}
