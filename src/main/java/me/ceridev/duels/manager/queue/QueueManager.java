package me.ceridev.duels.manager.queue;

import jdk.nashorn.internal.objects.annotations.Getter;
import me.ceridev.duels.instance.Arena;
import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.DuelsPlayer;
import me.ceridev.duels.manager.ArenaManager;
import me.ceridev.duels.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class QueueManager {

    private List<DuelsPlayer> classicQueue = new ArrayList<>();
    private final PlayerManager playerManager;
    private final ArenaManager arenaManager;
    private final DuelPlugin plugin;
    private QueueTask queueTask;
    public QueueManager(DuelPlugin plugin, PlayerManager playerManager, ArenaManager arenaManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
        this.arenaManager = arenaManager;
        this.queueTask = new QueueTask(plugin, this, arenaManager);
    }

    public void addPlayerToQueue(Player player) {
        if (classicQueue.contains(playerManager.getDuelPlayer(player))) return;
        classicQueue.add(playerManager.getDuelPlayer(player));
        player.sendMessage("You have been added to the queue!");
        queueTask.run();
    }

    public void removePlayerFromQueue(Player player) {
        if (!classicQueue.contains(playerManager.getDuelPlayer(player))) return;
        classicQueue.remove(playerManager.getDuelPlayer(player));
    }

    @Getter
    public List<DuelsPlayer> getClassicQueue() { return classicQueue; }

    public void execute(DuelsPlayer matchedPlayerOne, DuelsPlayer matchedPlayerTwo) {
        for (Arena arena : arenaManager.getArenas()) {
            if (arena.getPlayers().isEmpty()) {
                arena.addPlayer(matchedPlayerOne.getPlayer());
                arena.addPlayer(matchedPlayerTwo.getPlayer());
            }
        }
        classicQueue.remove(matchedPlayerOne);
        classicQueue.remove(matchedPlayerTwo);
    }

    public void execute(DuelsPlayer player) {
        for (Arena arena : arenaManager.getArenas()) {
            if (arena.getPlayers().size() == 1) {
                arena.addPlayer(player.getPlayer());
            }
        }
        classicQueue.remove(player);
    }
}
