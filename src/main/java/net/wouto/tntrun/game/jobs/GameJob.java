package net.wouto.tntrun.game.jobs;

import net.wouto.tntrun.game.TNTGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class GameJob implements Runnable {

    private final TNTGame game;
    private List<Runnable> exitHooks;

    public GameJob(TNTGame game) {
        this.game = game;
        this.exitHooks = new ArrayList<>();
    }

    public void preStart() {
    }

    public final Collection<Runnable> getExitHooks() {
        return new ArrayList<>(this.exitHooks);
    }

    public final void removeExitHook(Runnable exitHook) {
        this.exitHooks.remove(exitHook);
    }

    public final void addExitHook(Runnable exitHook) {
        this.exitHooks.add(exitHook);
    }

    public final TNTGame getGame() {
        return game;
    }

    public final void stop() {
        game.getJobManager().stopJob(this);
    }

    public abstract long getIntervalTicks();

    public abstract long getDelayTicks();

    public abstract int getTotalInvokeCount();

    public abstract boolean isRepeat();

    public abstract boolean isAsync();

    public static class GameJobMeta {

        private boolean started;
        private boolean currentlyInvoked;
        private boolean finished;
        private int invokes;
        private int taskId;

        public GameJobMeta() {
            this.started = false;
            this.finished = false;
            this.currentlyInvoked = false;
            this.invokes = 0;
            this.taskId = -1;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public boolean isStarted() {
            return started;
        }

        public void setStarted(boolean started) {
            this.started = started;
        }

        public boolean isCurrentlyInvoked() {
            return currentlyInvoked;
        }

        public void setCurrentlyInvoked(boolean currentlyInvoked) {
            this.currentlyInvoked = currentlyInvoked;
        }

        public int getInvokes() {
            return invokes;
        }

        public void setInvokes(int invokes) {
            this.invokes = invokes;
        }

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }
    }

}
