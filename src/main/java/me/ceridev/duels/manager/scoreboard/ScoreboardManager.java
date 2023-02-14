package me.ceridev.duels.manager.scoreboard;

import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.manager.MongoManager;
import me.ceridev.duels.manager.PlayerManager;
import me.ceridev.duels.manager.queue.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Arrays;

public class ScoreboardManager {

    private DuelPlugin plugin;

    private final PlayerManager playerManager;
    private final MongoManager mongoManager;
    private ScoreboardUpdateTask scoreboardUpdateTask;

    public ScoreboardManager(DuelPlugin plugin, PlayerManager playerManager, MongoManager mongoManager) {
        this.plugin = plugin;
        this.mongoManager = mongoManager;
        this.playerManager = playerManager;
        this.scoreboardUpdateTask = new ScoreboardUpdateTask(plugin, mongoManager, playerManager, this);

    }
    public void unregisterTask() { this.scoreboardUpdateTask.cancel(); }

    public void setNewScoreboardUpdateTask() {
        this.scoreboardUpdateTask = new ScoreboardUpdateTask(plugin, mongoManager, playerManager, this);
    }

    public void setScoreboard(Player player) {
        playerManager.getDuelPlayer(player).setScoreboard();
    }

}
