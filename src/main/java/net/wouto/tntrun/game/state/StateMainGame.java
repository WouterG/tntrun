package net.wouto.tntrun.game.state;

import net.wouto.tntrun.game.GameState;
import net.wouto.tntrun.game.TNTGame;
import net.wouto.tntrun.game.jobs.BlockDropJob;
import net.wouto.tntrun.game.jobs.EndGameCountdown;
import net.wouto.tntrun.game.jobs.StartGameCountdownJob;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Collection;

public class StateMainGame extends GameState {

    private StartGameCountdownJob startGameCountdownJob;
    private BlockDropJob blockDropJob;

    public StateMainGame(TNTGame game, Collection<Player> players) {
        super(game, players);
    }

    @Override
    public void initialize() {
        Location startLocation = getGame().getArea().getLocLow().clone().add(
                (getGame().getArea().getLocHigh().getX() - getGame().getArea().getLocLow().getX())/2,
                2,
                (getGame().getArea().getLocHigh().getZ() - getGame().getArea().getLocLow().getZ())/2
        );
        for (Player player : this.getPlayers()) {
            player.teleport(startLocation);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().kickPlayer("Cannot join a running game");
    }

    @Override
    public void start() {
        this.startGameCountdownJob = new StartGameCountdownJob(getGame());
        this.startGameCountdownJob.addExitHook(() -> {
            blockDropJob = new BlockDropJob(getGame().getArea(), getGame());
            blockDropJob.addExitHook(() -> getGame().getJobManager().startJob(new EndGameCountdown(getGame())));
            getGame().getJobManager().startJob(blockDropJob);
        });
        getGame().getJobManager().startJob(this.startGameCountdownJob);
    }

    @Override
    public void close() {
    }

    @Override
    public GameState getNextState() {
        return null;
    }
}
