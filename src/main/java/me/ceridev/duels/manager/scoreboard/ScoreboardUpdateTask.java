package me.ceridev.duels.manager.scoreboard;

import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.DuelsPlayer;
import me.ceridev.duels.manager.MongoManager;
import me.ceridev.duels.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class ScoreboardUpdateTask extends BukkitRunnable {
    private final DuelPlugin plugin;
    private final MongoManager mongoManager;
    private final PlayerManager playerManager;
    private final ScoreboardManager scoreboardManager;
    public ScoreboardUpdateTask(DuelPlugin plugin, MongoManager mongoManager, PlayerManager playerManager, ScoreboardManager scoreboardManager) {
        this.plugin = plugin;
        this.scoreboardManager = scoreboardManager;
        this.mongoManager = mongoManager;
        this.playerManager = playerManager;
        runTaskTimer(plugin, 0, 50);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            scoreboardManager.setScoreboard(player);
        }
    }
}
