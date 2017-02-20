package net.wouto.tntrun.game.jobs;

import net.wouto.tntrun.TNTRun;
import net.wouto.tntrun.game.GameArea;
import net.wouto.tntrun.game.SimpleLocation;
import net.wouto.tntrun.game.TNTGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.Collection;

public class BlockDropJob extends GameJob implements Listener {

    private static final String FALLING_BLOCK_META = BlockDropJob.class.toString();
    private static final byte[] STAGE_COLOR_META = new byte[]{4, 1, 14};

    private Collection<SimpleLocation> droppedBlocks;

    private GameArea area;

    private Collection<Location> stage1Blocks;
    private Collection<Location> stage2Blocks;
    private Collection<Location> stage3Blocks;

    public BlockDropJob(GameArea area, TNTGame game) {
        super(game);
        this.area = area;
        this.droppedBlocks = new ArrayList<>();
        this.stage1Blocks = new ArrayList<>();
        this.stage2Blocks = new ArrayList<>();
        this.stage3Blocks = new ArrayList<>();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().getY() > getGame().getArea().getLocLow().getY() - 3) {
            return;
        }
        getGame().getGameState().getPlayers().remove(event.getPlayer());
        event.getPlayer().kickPlayer(ChatColor.RED + "You died!");
        if (getGame().getGameState().getPlayers().size() <= 1) {
            this.stop();
        }
    }

    @Override
    public void preStart() {
        this.addExitHook(() -> {
            HandlerList.unregisterAll(BlockDropJob.this);
        });
        Bukkit.getPluginManager().registerEvents(this, TNTRun.getInstance());
    }

    @Override
    public void run() {
        for (Location stage3Block : this.stage3Blocks) {
            this.fallBlock(stage3Block);
        }
        this.stage3Blocks.clear();
        for (Location stage2Block : this.stage2Blocks) {
            setStageBlock(stage2Block, 3);
            this.stage3Blocks.add(stage2Block);
        }
        this.stage2Blocks.clear();
        for (Location stage1Block : this.stage1Blocks) {
            setStageBlock(stage1Block, 2);
            stage2Blocks.add(stage1Block);
        }
        this.stage1Blocks.clear();
        for (Player player : getGame().getGameState().getPlayers()) {
            Location blockLocation = player.getLocation().getBlock().getLocation();
            boolean dropped = false;
            for (int i = 1; i <= 3 && !dropped; i++) {
                Location loc = blockLocation.clone().add(0, -i, 0);
                if (loc.getBlockY() == area.getLocLow().getBlockY()) {
                    if (!isBlockDropped(loc)) {
                        dropped = true;
                        this.droppedBlocks.add(new SimpleLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                        this.stage1Blocks.add(loc);
                    } else {
                        System.out.println("block is dropped -- checking surroundings");
                        for (Location location : getSurroundingLocations(player.getLocation().clone().add(0, -i, 0))) {
                            if (location.getBlock().getType() == Material.COBBLESTONE) {
                                System.out.println(String.format("[x=%d, y=%d, z=%d] is cobblestone", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                                if (!isBlockDropped(location.getBlock().getLocation())) {
                                    System.out.println(String.format("[x=%d, y=%d, z=%d] is not dropped yet", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                                    this.droppedBlocks.add(new SimpleLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                                    this.stage1Blocks.add(location.getBlock().getLocation());
                                    dropped = true;
                                    break;
                                } else {
                                    System.out.println(String.format("[x=%d, y=%d, z=%d] is ALREADY dropped", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                                }
                            } else {
                                System.out.println(String.format("[x=%d, y=%d, z=%d] is NOT cobblestone", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isBlockDropped(Location location) {
        for (SimpleLocation droppedBlock : this.droppedBlocks) {
            if (droppedBlock.is(location)) {
                return true;
            }
        }
        return false;
    }

    private Collection<Location> getSurroundingLocations(Location location) {
        Collection<Location> surroundings = new ArrayList<>();
        boolean xPositive = location.getX() - location.getBlockX() >= 0.7d;
        boolean zPositive = location.getZ() - location.getBlockZ() >= 0.7d;
        boolean xNegative = location.getX() - location.getBlockX() <= 0.3d;
        boolean zNegative = location.getZ() - location.getBlockZ() <= 0.3d;
        // add relative +x/-x blocks if player is close enough
        if (xPositive) {
            surroundings.add(location.clone().add(1, 0, 0));
        } else if (xNegative) {
            surroundings.add(location.clone().add(-1, 0, 0));
        }
        // add relative +z/-z blocks if player is close enough
        if (zPositive) {
            surroundings.add(location.clone().add(0, 0, 1));
        } else if (zNegative) {
            surroundings.add(location.clone().add(0, 0, -1));
        }
        // add relative corner blocks
        if (xPositive && zPositive) {
            surroundings.add(location.clone().add(1, 0, 1));
        } else if (xPositive && zNegative) {
            surroundings.add(location.clone().add(1, 0, -1));
        } else if (xNegative && zPositive) {
            surroundings.add(location.clone().add(-1, 0, 1));
        } else if (xNegative && zNegative) {
            surroundings.add(location.clone().add(-1, 0, -1));
        }
        return surroundings;
    }

    @EventHandler
    public void onBlockLand(EntityChangeBlockEvent event) {
        if (event.getEntityType() != EntityType.FALLING_BLOCK) {
            return;
        }
        if (!event.getEntity().hasMetadata(FALLING_BLOCK_META)) {
            return;
        }
        ((FallingBlock) event.getEntity()).setDropItem(false);
        event.setCancelled(true);
    }

    private void fallBlock(Location location) {
        location.getBlock().setType(Material.AIR);
        FallingBlock block = location.getWorld().spawnFallingBlock(location.clone().add(0.5, 0, 0.5), new MaterialData(Material.TNT));
        block.setMetadata(FALLING_BLOCK_META, new FixedMetadataValue(TNTRun.getInstance(), FALLING_BLOCK_META));
    }

    private void setStageBlock(Location loc, int stage) {
        if (stage > STAGE_COLOR_META.length || stage < 1) {
            return;
        }
        byte meta = STAGE_COLOR_META[stage - 1];
        loc.getBlock().setType(Material.STAINED_CLAY);
        loc.getBlock().setData(meta);
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
        return 20L;
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
