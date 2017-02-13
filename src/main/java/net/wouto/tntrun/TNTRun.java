package net.wouto.tntrun;

import net.wouto.tntrun.game.TNTGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
