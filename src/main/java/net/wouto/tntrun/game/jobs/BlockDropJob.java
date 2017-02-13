package net.wouto.tntrun.game.jobs;

import net.wouto.tntrun.game.GameArea;
import net.wouto.tntrun.game.TNTGame;
import org.bukkit.Location;

import java.util.Collection;

public class BlockDropJob extends GameJob {

    private GameArea area;

    private Collection<Location> stage1Blocks;
    private Collection<Location> stage2Blocks;
    private Collection<Location> stage3Blocks;

    public BlockDropJob(GameArea area, TNTGame game) {
        super(game);
        this.area = area;
    }

    @Override
    public void run() {
        Collection<Location> blocks = this.area.getRandomBlocks(this.area.getBlockCount() / 50);
    }

    public GameArea getArea() {
        return area;
    }

    @Override
    public long getIntervalTicks() {
        return 5L;
    }

    @Override
    public long getDelayTicks() {
        return 30L;
    }

    @Override
    public int getTotalInvokeCount() {
        return -1;
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
