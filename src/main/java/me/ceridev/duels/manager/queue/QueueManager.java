package me.ceridev.duels.manager.queue;

import jdk.nashorn.internal.objects.annotations.Getter;
import me.ceridev.duels.instance.*;
import me.ceridev.duels.instance.bridges.BridgeArena;
import me.ceridev.duels.manager.ArenaManager;
import me.ceridev.duels.manager.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class QueueManager {

    private Map<DuelsPlayer, KitType> queue = new HashMap<>();
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

    public void addPlayerToQueue(Player player, KitType kitType) {
        if (queue.containsKey(playerManager.getDuelPlayer(player))) {
            player.sendMessage(ChatColor.RED + "You are already in " + ChatColor.AQUA + queue.get(playerManager.getDuelPlayer(player)) + ChatColor.RED + " queue");
            return;
        }
        queue.put(playerManager.getDuelPlayer(player), kitType);
        player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Added you to the " + ChatColor.AQUA + kitType.toString().toLowerCase() + ChatColor.GREEN + ChatColor.BOLD + " queue!");
        plugin.getInventoryItemManager().addQueueItemsToPlayer(player);
        if (queue.isEmpty()) {
            queueTask.run();
        }
    }

    public void removePlayerFromQueue(Player player) {
        if (!queue.containsKey(playerManager.getDuelPlayer(player))) return;
        queue.remove(playerManager.getDuelPlayer(player));
        plugin.getInventoryItemManager().addLobbyItemsToPlayer(player);
    }

    @Getter
    public Map<DuelsPlayer, KitType> getQueue() { return queue; }

    public void execute(DuelsPlayer matchedPlayerOne, DuelsPlayer matchedPlayerTwo) {
        for (BasicArena basicArena : arenaManager.getArenas()) {
            if (basicArena.getPlayers().isEmpty()) {
                if (basicArena.getGameState().equals(GameState.RUNNING) || basicArena.getGameState().equals(GameState.COUNTDOWN)) return;
                basicArena.addPlayer(matchedPlayerOne.getPlayer());
                basicArena.addPlayer(matchedPlayerTwo.getPlayer());
            }
        }
        queue.remove(matchedPlayerOne);
        queue.remove(matchedPlayerTwo);
    }

    public void executeBridge(DuelsPlayer matchedPlayerOne, DuelsPlayer matchedPlayerTwo) {
        for (BridgeArena bridgeArena : arenaManager.getBridgeArenas()) {
            if (bridgeArena.getPlayers().isEmpty()) {
                if (bridgeArena.getGameState().equals(GameState.RUNNING) || bridgeArena.getGameState().equals(GameState.COUNTDOWN)) return;
                bridgeArena.addPlayer(matchedPlayerOne.getPlayer());
                bridgeArena.addPlayer(matchedPlayerTwo.getPlayer());
            }
        }
        queue.remove(matchedPlayerOne);
        queue.remove(matchedPlayerTwo);
    }

    public void executeBridge(DuelsPlayer player) {
        for (BridgeArena bridgeArena : arenaManager.getBridgeArenas()) {
            if (bridgeArena.getPlayers().size() == 1) {
                bridgeArena.addPlayer(player.getPlayer());
            }
        }
        queue.remove(player);
    }

    public void execute(DuelsPlayer player) {
        for (BasicArena basicArena : arenaManager.getArenas()) {
            if (basicArena.getPlayers().size() == 1) {
                basicArena.addPlayer(player.getPlayer());
            }
        }
        queue.remove(player);
    }
}
