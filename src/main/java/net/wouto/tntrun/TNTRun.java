package net.wouto.tntrun;

import net.wouto.tntrun.game.TNTGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TNTRun extends JavaPlugin {

    private static TNTRun instance;

    private TNTGame game;

    @Override
    public void onEnable() {
        TNTRun.instance = this;
        Config.init(this);

        this.game = new TNTGame();
        Bukkit.getScheduler().runTaskLater(this, () -> {
            System.out.println("[debug] FIXING WORLD");
            Material[] types = new Material[] {
                    Material.COBBLESTONE, Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.WATER, Material.COBBLESTONE
            };
            for (Location l : game.getArea().getAllBlocks()) {
                for (int i = 0; i < 7; i++) {
                    Location p = l.clone().add(0, -i, 0);
                    p.getBlock().setType(types[i]);
                }
            }
        }, 100L);
    }

    private static String createMessage(ChatColor color, String message) {
        return ChatColor.DARK_RED + "[" + ChatColor.RED + "TNT Run" + ChatColor.DARK_RED + "]" + ChatColor.RESET + " " + color + message;
    }

    public static void message(ChatColor color, String message) {
        Bukkit.broadcastMessage(createMessage(color, message));
    }

    public static void messagePlayer(Player p, ChatColor color, String message) {
        p.sendMessage(createMessage(color, message));
    }

    public TNTGame getGame() {
        return game;
    }

    public static TNTRun getInstance() {
        return instance;
    }
}
