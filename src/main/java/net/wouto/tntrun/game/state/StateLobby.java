package net.wouto.tntrun.game.state;

import net.wouto.tntrun.Config;
import net.wouto.tntrun.TNTRun;
import net.wouto.tntrun.game.GameState;
import net.wouto.tntrun.game.TNTGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;

public class StateLobby extends GameState {

    private BukkitTask gameCountdown;

    public StateLobby(TNTGame game, Collection<Player> players) {
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
        return new StateMainGame(this.getGame(), this.getPlayers());
    }

    public void checkGameCountdown() {
        boolean shouldStart = getPlayers().size() >= Config.MINIMUM_PLAYERS;
        if (shouldStart && gameCountdown == null) {
            TNTRun.message(ChatColor.YELLOW, "Starting game in 10 seconds");
            gameCountdown = Bukkit.getScheduler().runTaskLater(TNTRun.getInstance(), () -> {
                TNTRun.getInstance().getGame().nextGameState();
            }, Config.START_DELAY_SECONDS * 20L);
        } else if (!shouldStart && gameCountdown != null) {
            TNTRun.message(ChatColor.GOLD, "Not enough players to start the game -- waiting for at least " + Config.MINIMUM_PLAYERS + " players.");
            gameCountdown.cancel();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        Player r = null;
        for (Player player : getPlayers()) {
            if (player.getUniqueId().equals(p.getUniqueId())) {
                r = player;
                break;
            }
        }
        if (r != null) {
            getPlayers().remove(r);
        }
        getPlayers().add(p);
        checkGameCountdown();
        p.teleport(Config.SPAWN_LOCATION);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        Player r = null;
        for (Player player : getPlayers()) {
            if (player.getUniqueId().equals(p.getUniqueId())) {
                r = player;
                break;
            }
        }
        if (r != null) {
            getPlayers().remove(r);
        }
        checkGameCountdown();
    }
}
