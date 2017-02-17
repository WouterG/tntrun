package net.wouto.tntrun.game.jobs;

import net.wouto.tntrun.TNTRun;
import net.wouto.tntrun.game.TNTGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EndGameCountdown extends GameJob {

    private int counter = this.getTotalInvokeCount();

    public EndGameCountdown(TNTGame game) {
        super(game);
    }

    @Override
    public void preStart() {
        List<Player> players = new ArrayList<>(getGame().getGameState().getPlayers());
        List<String> names = new ArrayList<>();
        for (Player player : players) {
            names.add(player.getName());
        }
        String text = String.join(", ", names) + " won the game!";
        TNTRun.message(ChatColor.GOLD, text);
        this.addExitHook(() -> Bukkit.shutdown());
    }

    @Override
    public void run() {
        switch (this.counter) {
            case 10:
            case 5:
            case 3:
            case 2:
            case 1:
                TNTRun.message(ChatColor.RED, "Stopping server in " + this.counter);
                break;
        }
        this.counter--;
    }

    @Override
    public long getIntervalTicks() {
        return 20L;
    }

    @Override
    public long getDelayTicks() {
        return 20L;
    }

    @Override
    public int getTotalInvokeCount() {
        return 10;
    }

    @Override
    public boolean isRepeat() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }
}
