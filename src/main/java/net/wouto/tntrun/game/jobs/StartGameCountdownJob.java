package net.wouto.tntrun.game.jobs;

import net.wouto.tntrun.TNTRun;
import net.wouto.tntrun.game.TNTGame;
import org.bukkit.ChatColor;

public class StartGameCountdownJob extends GameJob {

    private int counter;

    public StartGameCountdownJob(TNTGame game) {
        super(game);
        this.counter = 10;
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

    @Override
    public void run() {
        TNTRun.message(ChatColor.RED, String.format("Starting game in %d seconds", this.counter--));
    }

}
