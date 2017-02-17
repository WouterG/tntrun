package net.wouto.tntrun.game;

import net.wouto.tntrun.TNTRun;
import net.wouto.tntrun.game.jobs.GameJob;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class JobManager {

    private TNTGame game;

    private Map<GameJob, GameJob.GameJobMeta> jobMetaMap;

    public JobManager(TNTGame game) {
        this.game = game;
        this.jobMetaMap = new HashMap<>();
    }

    public int startJob(GameJob job) {
        BukkitTask task;
        Runnable wrapJob = doJobWrap(job);
        if (job.isAsync()) {
            if (job.isRepeat()) {
                task = Bukkit.getScheduler().runTaskTimerAsynchronously(TNTRun.getInstance(), wrapJob, job.getDelayTicks(), job.getIntervalTicks());
            } else {
                task = Bukkit.getScheduler().runTaskLaterAsynchronously(TNTRun.getInstance(), wrapJob, job.getDelayTicks());
            }
        } else {
            if (job.isRepeat()) {
                task = Bukkit.getScheduler().runTaskTimer(TNTRun.getInstance(), wrapJob, job.getDelayTicks(), job.getIntervalTicks());
            } else {
                task = Bukkit.getScheduler().runTaskLater(TNTRun.getInstance(), wrapJob, job.getDelayTicks());
            }
        }
        GameJob.GameJobMeta meta = new GameJob.GameJobMeta();
        meta.setTaskId(task.getTaskId());
        this.jobMetaMap.put(job, meta);
        return task.getTaskId();
    }

    public GameJob.GameJobMeta getJobMeta(int taskId) {
        GameJob.GameJobMeta meta = null;
        for (GameJob gameJob : this.jobMetaMap.keySet()) {
            GameJob.GameJobMeta m = this.jobMetaMap.get(gameJob);
            if (m.getTaskId() == taskId) {
                meta = m;
                break;
            }
        }
        return meta;
    }

    public GameJob.GameJobMeta stopJob(GameJob job) {
        GameJob.GameJobMeta meta = this.jobMetaMap.get(job);
        if (meta == null) {
            return null;
        }
        int tid = meta.getTaskId();
        if (tid == -1) {
            return meta;
        }
        Bukkit.getScheduler().cancelTask(tid);
        meta.setCurrentlyInvoked(false);
        meta.setFinished(true);
        return meta;
    }

    public GameJob.GameJobMeta getJobMeta(GameJob job) {
        return this.jobMetaMap.get(job);
    }

    private Runnable doJobWrap(GameJob job) {
        return () -> {
            GameJob.GameJobMeta meta = this.jobMetaMap.get(job);
            if (!meta.isStarted()) {
                job.preStart();
                meta.setStarted(true);
            }
            meta.setCurrentlyInvoked(true);
            job.run();
            meta.setCurrentlyInvoked(false);
            meta.setInvokes(meta.getInvokes()+1);
            if (!job.isRepeat()) {
                meta.setFinished(true);
            } else if (job.getTotalInvokeCount() > 0) {
                if (meta.getInvokes() >= job.getTotalInvokeCount()) {
                    meta.setFinished(true);
                    if (!job.getExitHooks().isEmpty()) {
                        for (Runnable runnable : job.getExitHooks()) {
                            Bukkit.getScheduler().runTask(TNTRun.getInstance(), runnable);
                        }
                    }
                    job.stop();
                }
            }
        };
    }

}
